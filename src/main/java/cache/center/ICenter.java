package cache.center;

import cache.IVirtualCenter;
import cache.IVirtualClient;

import java.util.Map;

/**
 * @author lidong@date 2023-10-24@version 1.0
 * The local or remote center should implement this interface to manage cache.
 */
public interface ICenter extends IVirtualCenter {
    /**
     * Return the physical implement of this center. There may be several physical implements,
     * e.g. local center, remote center.
     * @return
     */
    IPhysicalCenter getPhysicalCenter();

    @Override
    default void onCacheChanged(String compKey){
        //make center dirty
        getPhysicalCenter().setDirty(compKey);
        //clear agreement marker of the center
        getPhysicalCenter().clearAgreementFlag(compKey);
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
    default void onAgreementTimeout(String compKey){
        IPhysicalCenter pc = getPhysicalCenter();
        boolean isConsistencyFirst = pc.isConsistencyFirst();
        if(isConsistencyFirst){
            if(!pc.keepWaitAgreement()){
                forceAgree(compKey,pc);
            }
        }else {
            forceAgree(compKey, pc);
        }
    }

    /**
     * Registe a client.
     * @param name client name
     * @param client
     */
    default void registerClient(String name, IVirtualClient client){
        Map<String, IVirtualClient> clients = getPhysicalCenter().getClients();
        clients.put(name,client);
    }

    /**
     * Unregiste a client
     * @param name client name
     */
    default void unrigister(String name ){
        Map<String, IVirtualClient> clients = getPhysicalCenter().getClients();
        clients.remove(name);
    }

    private void notifyAllPrepareDirty(String compKey){
        Map<String, IVirtualClient> clients = getPhysicalCenter().getClients();
        for(IVirtualClient client:clients.values()){
            client.prepareDirty(compKey);
        }
    }

    private void forceAgree(String compKey, IPhysicalCenter pc) {
        onAgreementReached(compKey);
        pc.logAgreementTimeout(compKey);
    }
}
