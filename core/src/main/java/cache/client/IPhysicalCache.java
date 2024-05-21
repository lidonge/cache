package cache.client;

/**
 * @author lidong@date 2023-10-24@version 1.0
 * A physical cache implement by client application.
 */
public interface IPhysicalCache {
    /**
     * Test if all client reached the specified agreement.
     * @param key
     * @return
     */
    boolean isAllAgreed(String key);

    /**
     * Refresh local cache from remote source side or redis.
     * @param key
     * @return
     */
    void refreshFromRemote(String key);

    /**
     * Set the prepare-dirty flag for the specified data.
     *
     * @param key
     */
    void setPrepareDirty(String key, boolean prepare);

    /**
     * Test if current status is prepare-dirty of the specified data.
     * @param key
     * @return
     */
    boolean isPrepareDirty(String key);

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
