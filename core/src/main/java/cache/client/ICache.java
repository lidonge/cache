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
     *
     * @return
     */
    IClient getClient();

    /**
     * Get specified data from cache.
     *
     * @param key
     * @return
     */
    default IClientCacheData get(ICompositeKey key) {
        IPhysicalCache pc = getPhysicalCache();
        String compKey = key.getCompositeKey();
        Object locker = pc.getLocker(compKey);
        IClientCacheData data = null;
        synchronized (locker) {
            if (pc.isKeyInit(compKey)) {
                //It's the first time to get data, register the key to the center
                IClient client = getClient();
                IVirtualCenter virtualCenter = client.getClientRegister();
                boolean isAgreementReached = virtualCenter.registerClient(client.getName(), compKey, client);
                getLogger().info("Client {} regiester {} to center.", getClient().getName(), compKey);
                if (!isAgreementReached)
                    setPrepareDirty(compKey);
            }
            if (isPrepareDirty(compKey)) {
                dirtyIfAgreementReached(compKey);
            }
            refreshIfDirty(compKey);
            data = pc.get(compKey);
        }
        getLogger().info("Client {} get {} value {} from client cache.", getClient().getName(), compKey, data);

        return data;
    }

    /**
     * Should be called if the cache is empty.
     *
     * @param key
     * @param cacheData
     */
    default void put(ICompositeKey key, IClientCacheData cacheData) {
        IPhysicalCache pc = getPhysicalCache();
        String compKey = key.getCompositeKey();
        Object locker = pc.getLocker(compKey);
        synchronized (locker) {
            pc.put(compKey, cacheData);
        }
    }

    /**
     * Get the physical cache implement, maybe local or remote e.g. redis
     *
     * @return
     */
    IPhysicalCache getPhysicalCache();

    /**
     * Make a specified data dirty, called from center.
     *
     * @param key
     */
    default void setPrepareDirty(String key) {
        getLogger().info("Client {} setPrepareDirty {} .", getClient().getName(), key);
        getPhysicalCache().setPrepareDirty(key, true);
    }

    /**
     * Test if the specified data dirty.
     *
     * @param key
     * @return true is dirty.
     */
    default boolean isPrepareDirty(String key) {
        boolean ret = getPhysicalCache().isPrepareDirty(key);
        getLogger().info("Client {}'s data {} prepare dirty flag is {} .", getClient().getName(), key, ret);
        return ret;
    }

    private void refreshIfDirty(String compKey) {
        IPhysicalCache pc = getPhysicalCache();
        IClientCacheData ret = null;
        boolean dirty = pc.isDirty(compKey);
        getLogger().info("Client {}'s {} refreshIfDirty is {}.", getClient().getName(), compKey, dirty);
        if (dirty) {
            // null means dirty or first time read from cache
            pc.refreshFromRemote(compKey);
        }
    }

    private void dirtyIfAgreementReached(String compKey) {
        IPhysicalCache pc = getPhysicalCache();
        boolean isAllAgreed = pc.isAllAgreed(compKey);
        getLogger().info("All agreed flag is {}, if true Refresh client {} .", isAllAgreed, getClient().getName());

        if (isAllAgreed) {
            pc.setPrepareDirty(compKey, false);
            pc.setDirty(compKey);
        }
    }

}
