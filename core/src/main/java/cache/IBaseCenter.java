package cache;

import cache.center.ICenterCacheData;

import java.util.Map;

/**
 * @author lidong@date 2023-10-25@version 1.0
 */
public interface IBaseCenter {
    /**
     * Called when cached content specified by a composite key changed.
     * @param compKey
     */
    void onCacheChanged(String compKey);
    /**
     * Get cached data from this managing center, if there is not existed, fetch from source side.
     * @param compKey
     * @return
     */
    ICacheData get(String compKey);

    /**
     * Test if the specified agreement is reached.
     * @param compKey
     * @return true for reached
     */
    boolean isAgreementReached(String compKey);
    /**
     * Registe a client.
     * @param name client name
     * @param client
     */
    void registerClient(String name, IBaseClient client);

    /**
     * Unregiste a client
     * @param name client name
     */
    void unrigister(String name );
}
