package cache;

import cache.center.ICenterCacheData;

/**
 * @author lidong@date 2023-10-25@version 1.0
 */
public interface IBaseCenter {
    /**
     * Called when cached content specified by a composite key changed.
     * This results in dirty cached data and a clear agreement flag.
     * And notify all client to prepare dirty
     * @param compKey
     */
    void onCacheChanged(String compKey);

    /**
     * Get cached data from this managing center.
     * if there is not existed, return null(If agreement is not reached, return old one).
     * @param compKey
     * @return null if dirty.
     */
    ICacheData get(String compKey);

    /**
     * Put the new data to the local cache if the status is in dirty.
     * And set the dirty status to false.
     * @param compKey
     * @param cacheData
     */
    void put(String compKey, ICenterCacheData cacheData);

    /**
     * Test if the specified agreement is reached.
     * @param compKey
     * @return true for reached
     */
    boolean isAgreementReached(String compKey);
    /**
     * Register a client.
     * @param name client name
     * @param client
     * @return true if is agreement reached for the key
     */
    boolean registerClient(String name, String key, IBaseClient client);

    /**
     * Unregiste a client
     * @param name client name
     */
    void unregisterClient(String name );
}
