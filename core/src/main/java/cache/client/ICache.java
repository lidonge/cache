package cache.client;

import cache.ICacheData;
import cache.ICompositeKey;
import cache.ILogable;

/**
 * @author lidong@date 2023-10-24@version 1.0
 * A cache which store the master data.
 */
public interface ICache extends ILogable {
    /**
     * Get the client which the cache belong to.
     * @return
     */
    IClient getClient();
    /**
     * Get specified data from cache.
     * @param key
     * @return
     */
    default IClientCacheData get(ICompositeKey key){
        IPhysicalCache pc = getPhysicalCache();
        String compKey = key.getCompositeKey();

        IClientCacheData data = refresh(compKey);
        getLogger().info("Client {} get {} value {} from client cache.", getClient().getName(),compKey,data);

        return data;
    }

    /**
     * Get the physical cache implement, maybe local or remote e.g. redis
     * @return
     */
    IPhysicalCache getPhysicalCache();

    /**
     * Make a specified data dirty, called from center.
     * @param key
     */
    default void setPrepareDirty(String key){
        getLogger().info("Client {} setPrepareDirty {} .", getClient().getName(),key);
        getPhysicalCache().setPrepareDirty(key);
    }

    /**
     * Test if the specified data dirty.
     * @param key
     * @return true is dirty.
     */
    default boolean isPrepareDirty(String key){
        boolean ret = getPhysicalCache().isPrepareDirty(key);
        getLogger().info("Client {}'s data {} prepare dirty flag is {} .",getClient().getName(), key,ret);
        return ret;
    }

    private IClientCacheData refresh(String compKey){
        ICacheData ret = null;
        if(isPrepareDirty(compKey)){
            refreshIfAgreementReached(compKey);
        }
        return refreshIfDirty(compKey);
    }

    private IClientCacheData refreshIfDirty(String compKey) {
        IPhysicalCache pc = getPhysicalCache();
        Object locker = pc.getLocker(compKey);
        synchronized (locker) {
            IClientCacheData ret = pc.get(compKey);
            boolean dirty = ret == null;
            getLogger().info("Client {}'s {} refreshIfDirty is {}.", getClient().getName(),compKey,dirty);
            if (dirty) {
                // null means dirty or first time read from cache
                ret = pc.refreshFromRemote(compKey);
            }
            return ret;
        }
    }

    private void refreshIfAgreementReached(String compKey) {
        IPhysicalCache pc = getPhysicalCache();
        Object locker = pc.getLocker(compKey);
        synchronized (locker) {
            boolean isAllAgreed =  pc.isAllAgreed(compKey);
            getLogger().info("All agreed flag is {}, if true Refresh client {} .", isAllAgreed,getClient().getName());

            if (isAllAgreed) {
//                pc.setPrepareDirty(compKey, false);
                pc.setDirty(compKey);
            }
        }
    }

}
