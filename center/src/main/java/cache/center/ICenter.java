package cache.center;

import cache.*;
import cache.util.ILogable;
import cache.util.IRetryHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lidong@date 2023-10-24@version 1.0
 * The local or remote center should implement this interface to manage cache.
 */
public interface ICenter extends IBaseCenter, ILogable {
    /**
     * Return the physical implement of this center. There may be several physical implements,
     * e.g. local center, remote center.
     * @return
     */
    IPhysicalCenter getPhysicalCenter();

    @Override
    default ICenterCacheData get(String compKey){
        IPhysicalCenter pc = getPhysicalCenter();
        Object locker = pc.getLocker(compKey);
        synchronized (locker) {
            ICenterCacheData ret = null;
            //Here need to consider the situation of newly registered clients， if the agreement is not reached
            //So add all-agreement flag to the ICenterCacheData
            boolean dirty = pc.isDirty(compKey);
            if (!dirty ) {
                //The data is ready
                ret = pc.getFromLocalCache(compKey);
            }
            getLogger().info("Get data {} from center, value is {}, dirty flag is {}." ,compKey,ret, dirty);
            //TODO Concurrency for a specific key may cause efficiency problems, but this is very rare.
            return ret;
        }
    }

    @Override
    default void put(String compKey, ICenterCacheData cacheData) {
        IPhysicalCenter pc = getPhysicalCenter();
        Object locker = pc.getLocker(compKey);
        synchronized (locker) {
            if(pc.isDirty(compKey)) {
                if(pc.isAgreementReached(compKey)) {
                    pc.setDirty(compKey, false);
                    pc.putToLocalCache(compKey, cacheData);
                    getLogger().info("Put data {} to center, value is {}." ,compKey,cacheData);
                }else{
                    throw new RuntimeException("Unexpected assignment error of key:" + compKey);
                }
            }
        }
    }

   @Override
    default void onCacheChanged(String compKey){
        IPhysicalCenter pc = getPhysicalCenter();
        Object locker = pc.getLocker(compKey);
        synchronized (locker) {
            //This is used to optimize the situation where the source is continuously updated.
            if(!pc.isDirty(compKey) && !pc.isOnChanging()) {
                pc.setOnChanging(true);
                //clear agreement marker of the center
                pc.clearAgreementFlag(compKey);
//                pc.setDirty(compKey, true);
                notifyAllPrepareDirty(compKey);
            }
        }
    }

    /**
     * Called when an agreement of all client is reached.
     * @param compKey
     */
    default void onAgreementReached(String compKey){
        IPhysicalCenter pc = getPhysicalCenter();
        Object locker = pc.getLocker(compKey);
        synchronized (locker) {
            //clear cache
            pc.setDirty(compKey,true);
//            pc.putToLocalCache(compKey, null);
            //set agreement marker of the center
            pc.setAgreementFlag(compKey);
            pc.setOnChanging(false);
        }
    }

    @Override
    default boolean registerClient(String name, String key, IBaseClient client){
        Map<String, IVirtualClient> clients = getPhysicalCenter().getClients();
        IVirtualClient  vc = clients.get(name);
        if(vc == null){
            vc = (IVirtualClient) client;
            clients.put(name, vc);
        }
        vc.addKey(key);
        //TODO here need register key to source?
        return isAgreementReached(key);
    }

    @Override
    default void unregisterClient(String name ){
        Map<String, IVirtualClient> clients = getPhysicalCenter().getClients();
        clients.remove(name);
    }

    class CallbackStatus {
        boolean hasTimeout = false;
        boolean finished = false;
    };
    private void notifyAllPrepareDirty(String compKey){
        IPhysicalCenter pc = getPhysicalCenter();
        Map<String, IVirtualClient> clients = pc.getClients();
        CallbackHandler callbackHandler = new CallbackHandler(compKey);
        Map<IPrepareDirtyHandler, IAsynListener> allClients = getPrepareDirtyHandlers(compKey, callbackHandler, clients, pc.getMultiCenter());
        CallbackStatus status = new CallbackStatus();
        createAsynCallbackable(callbackHandler, status, pc, allClients);

        pc.getRetryTool().retry(() -> sendToClientsSync(compKey, allClients, pc, callbackHandler, status));
    }

    private void createAsynCallbackable(CallbackHandler callbackHandler, CallbackStatus status, IPhysicalCenter pc, Map<IPrepareDirtyHandler, IAsynListener> allClients) {
        callbackHandler.setCallbackable(compKey -> {
            status.finished = true;
            if(pc.isConsistencyFirst()){
                for(IPrepareDirtyHandler key: allClients.keySet()){
                    IAsynListener listener = allClients.get(key);
                    switch (listener.getStatus()){
                        case timeout :
                            status.hasTimeout = true;
                            break;
                        case error:
                            allClients.remove(key);
                            if(key instanceof IVirtualClient)
                                unregisterClient(((IVirtualClient) key).getName());
                            break;
                        case success:
                            allClients.remove(key);
                            break;
                    }
                }
            }else
                onAgreementReached(compKey);

            status.notify();
        });
    }

    private void sendToClientsSync(String compKey, Map<IPrepareDirtyHandler, IAsynListener> allClients, IPhysicalCenter pc, CallbackHandler callbackHandler, CallbackStatus status) {
        prepareDirty(compKey, allClients, pc);
        callbackHandler.start(pc.getAgreeTimeout());
        while(!status.finished){
            synchronized (status){
                try {
                    status.wait(1000);
                } catch (InterruptedException e) {
                    getLogger().error("Error found with call-back monitor",e);
                    status.finished = true;
                }
            }
        }
    }

    private Map<IPrepareDirtyHandler, IAsynListener> getPrepareDirtyHandlers(String compKey,
                                                                             CallbackHandler callbackHandler,
                                                                             Map<String, IVirtualClient> clients,
                                                                             IPrepareDirtyHandler multiCenter) {
        Map<IPrepareDirtyHandler, IAsynListener> ret = new HashMap<>();
        for(IVirtualClient client: clients.values()) {
            if (client.hasKey(compKey)) {
                IAsynListener iAsynListener = callbackHandler.addOne();
                ret.put( client,iAsynListener);
            }
        }
        if(multiCenter != null) {
            IAsynListener iAsynListener = callbackHandler.addOne();
            ret.put(multiCenter, iAsynListener);
        }
        return ret;
    }

    private void prepareDirty(String compKey, Map<IPrepareDirtyHandler, IAsynListener> clients, IPhysicalCenter pc) {
        Map<IPrepareDirtyHandler, IAsynListener> ret = new HashMap<>();
        for(IPrepareDirtyHandler client: clients.keySet()){
            getLogger().info("Prepare {}'s {} to dirty!",client.toString(), compKey);
            IAsynListener iAsynListener = clients.get(client);
            client.prepareDirty(compKey, iAsynListener);
        }
    }
}
