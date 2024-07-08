package cache.center;

import cache.*;
import cache.util.ILogable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lidong@date 2023-10-24@version 1.0
 * The local or remote center should implement this interface to manage cache.
 */
public interface ICenter extends IVirtualCenterInClient, IVirtualCenterInSource, ILogable {
    /**
     * Return the physical implement of this center. There may be several physical implements,
     * e.g. local center, remote center.
     * @return
     */
    IPhysicalCenter getPhysicalCenter();

    @Override
    default ICenterCacheData get(String compKey){
        getLogger().info("get compKey= {}", compKey);
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
    default void put( String compKey, ICacheData cacheData) {
        getLogger().info("put compKey= {}, data={}", compKey, cacheData);
        IPhysicalCenter pc = getPhysicalCenter();
        Object locker = pc.getLocker(compKey);
        synchronized (locker) {
            if(pc.isDirty(compKey)) {
                if(pc.isAgreementReached(compKey)) {
                    pc.setDirty(compKey, false);
                    pc.putToLocalCache(compKey, (ICenterCacheData) cacheData);
                    getLogger().info("Put data {} to center, value is {}." ,compKey,cacheData);
                }else{
                    throw new RuntimeException("Unexpected assignment error of key:" + compKey);
                }
            }
        }
    }

   @Override
    default void onCacheChanged(String compKey){
       getLogger().info("onCacheChanged compKey= {}", compKey);
        IPhysicalCenter pc = getPhysicalCenter();
        Object locker = pc.getLocker(compKey);
        synchronized (locker) {
            //This is used to optimize the situation where the source is continuously updated.
            if(!pc.isDirty(compKey) && !pc.isOnChanging(compKey)) {
                pc.setOnChanging(compKey,true);
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
        getLogger().info("onAgreementReached compKey= {}", compKey);
        IPhysicalCenter pc = getPhysicalCenter();
        Object locker = pc.getLocker(compKey);
        synchronized (locker) {
            //clear cache
            pc.setDirty(compKey,true);
//            pc.putToLocalCache(compKey, null);
            //set agreement marker of the center
            pc.setAgreementFlag(compKey);
            pc.setOnChanging(compKey,false);
        }
    }

    @Override
    default boolean registerClient(String name, String key, IBaseClient client){
        getLogger().info("register client name= {}, key = {} , client = {}", name,key,client);
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
        getLogger().info("un- register client name= {}", name);
        Map<String, IVirtualClient> clients = getPhysicalCenter().getClients();
        clients.remove(name);
    }
    @Override
    default boolean isAgreementReached(String compKey) {
        return getPhysicalCenter().isAgreementReached(compKey);
    }
    class CallbackStatus {
        boolean hasTimeout = false;
        boolean finished = false;
    };
    private void notifyAllPrepareDirty(String compKey){
        getLogger().info("notifyAllPrepareDirty compKy={}", compKey);
        IPhysicalCenter pc = getPhysicalCenter();
        Map<String, IVirtualClient> clients = pc.getClients();
        CallbackHandler callbackHandler = new CallbackHandler(compKey);
        Map<IPrepareDirtyHandler, IAsynListener> allClients = getPrepareDirtyHandlers(compKey, callbackHandler, clients, pc.getMultiCenter());
        CallbackStatus status = new CallbackStatus();
        createAsynCallbackable(callbackHandler, status, pc, allClients);

        pc.getRetryTool().retry(() -> sendToClientsSync(compKey, allClients, pc, callbackHandler, status));
    }

    private void createAsynCallbackable(CallbackHandler callbackHandler, CallbackStatus status,
                                        IPhysicalCenter pc, Map<IPrepareDirtyHandler, IAsynListener> allClients) {
        callbackHandler.setCallbackable(compKey -> {
            boolean consistencyFirst = pc.isConsistencyFirst();
            status.finished = true;
            getLogger().info("All finished compKey={} and isConsistencyFirst ={}." , compKey,consistencyFirst);
            if(consistencyFirst){
                List<Map.Entry<IPrepareDirtyHandler, IAsynListener>> list = new ArrayList<>(allClients.entrySet());
                for(Map.Entry<IPrepareDirtyHandler, IAsynListener> entry: list){
                    IAsynListener listener = entry.getValue();
                    switch (listener.getStatus()){
                        case timeout :
                            status.hasTimeout = true;
                            break;
                        case error:
                            allClients.remove(entry.getKey());
                            if(entry.getKey() instanceof IVirtualClient)
                                unregisterClient(((IVirtualClient) entry.getKey()).getName());
                            break;
                        case success:
                            allClients.remove(entry.getKey());
                            break;
                    }
                }
            }else
                allClients.clear();

            if(allClients.size() == 0)
                onAgreementReached(compKey);

            synchronized (status) {
                status.notify();
            }
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
            try {
                client.prepareDirty(compKey, iAsynListener);
            }catch (Throwable e){
                getLogger().error("Error while prepare dirty of .", e );
                iAsynListener.onError();
            }
        }
    }
}
