package cache.center;

import cache.IBaseCenter;
import cache.IBaseClient;
import cache.ILogable;
import cache.client.IVirtualCenter;
import cache.impls.util.ITimeoutListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author lidong@date 2023-10-24@version 1.0
 * The local or remote center should implement this interface to manage cache.
 */
public interface ICenter extends IBaseCenter, ITimeoutListener, ILogable {
    /**
     * Return the physical implement of this center. There may be several physical implements,
     * e.g. local center, remote center.
     * @return
     */
    IPhysicalCenter getPhysicalCenter();
    @Override
    default ICenterCacheData get(String compKey){
        IPhysicalCenter pc = getPhysicalCenter();
        ICenterCacheData ret = pc.getFromLocalCache(compKey);
        Object locker = pc.getLocker(compKey);
        synchronized (locker) {
            boolean dirty = ret == null;
            if (dirty) {
                //The data is dirty
                pc.makeNewDataAvailable(compKey);
                ret = pc.getFromLocalCache(compKey);
            }
            getLogger().info("Get data {} from center, value is {}, dirty flag is {}." ,compKey,ret, dirty);
            return ret;
        }
    }

   @Override
    default void onCacheChanged(String compKey){
        IPhysicalCenter pc = getPhysicalCenter();
        //clear agreement marker of the center
        pc.clearAgreementFlag(compKey);
        //start agreement timeout monitor
//        TimeoutUtil.getInstance().addMonitor(this,compKey,pc.getAgreeTimeout());
        //notify all client to prepare dirty
        notifyAllPrepareDirty(compKey);
    }

    /**
     * Called when an agreement of all client is reached.
     * @param compKey
     */
    default void onAgreementReached(String compKey){
        //make new data available
        getPhysicalCenter().makeNewDataAvailable(compKey);
        //set agreement marker of the center
        getPhysicalCenter().setAgreementFlag(compKey);
    }

    /**
     * Called when an agreement is not reached before timeout.
     * @param compKey
     */
    @Override
    default void onTimeout(String compKey){
        IPhysicalCenter pc = getPhysicalCenter();
        boolean isConsistencyFirst = pc.isConsistencyFirst();
        if(isConsistencyFirst){
            if(!pc.keepWaitAgreement(compKey)){
                forceAgree(compKey,pc);
            }
        }else {
            forceAgree(compKey, pc);
        }
    }

    @Override
    default void registerClient(String name, IBaseClient client){
        Map<String, IVirtualClient> clients = getPhysicalCenter().getClients();
        clients.put(name, (IVirtualClient) client);
    }

    @Override
    default void unrigister(String name ){
        Map<String, IVirtualClient> clients = getPhysicalCenter().getClients();
        clients.remove(name);
    }

    private void notifyAllPrepareDirty(String compKey){
        Map<String, IVirtualClient> clients = getPhysicalCenter().getClients();
        List<IAsynListener> asynListeners = new ArrayList<>();
        for(IVirtualClient client:clients.values()){
            getLogger().info("Prepare {}'s {} to dirty!",client.getName(), compKey);
            asynListeners.add(client.prepareDirty(compKey));
        }

        getPhysicalCenter().waitAllClientFinish(asynListeners);
        onAgreementReached(compKey);
    }

    private void forceAgree(String compKey, IPhysicalCenter pc) {
        onAgreementReached(compKey);
        pc.logAgreementTimeout(compKey);
    }
}
