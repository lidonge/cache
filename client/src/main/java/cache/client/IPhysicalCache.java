package cache.client;

import cache.IClientCacheData;

/**
 * @author lidong@date 2023-10-24@version 1.0
 * A physical cache implement by client application.
 */
public interface IPhysicalCache {

    /**
     * Get specified data from local.
     * @param compKey
     * @return null means not registered.
     */
    IClientCacheData get(String compKey);

    /**
     * Put the new data to the local cache.
     * @param compKey
     * @param cacheData
     */
    void put(String compKey,IClientCacheData cacheData);

    /**
     * Set specified local data to dirty, remove specified data from local.
     * @param key
     */
    void setDirty(String key);

    /**
     * Test if the specified local data is dirty.
     * @param key
     */
    boolean isDirty(String key);

    /**
     * Get specified locker for atomic operations
     * @param compKey
     * @return
     */
    Object getLocker(String compKey);

    boolean isKeyInit(String compKey);
}
