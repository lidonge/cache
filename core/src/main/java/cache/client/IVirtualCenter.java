package cache.client;

import cache.IBaseCenter;

/**
 * @author lidong@date 2023-10-24@version 1.0
 * A cache managing center, may be a stand-alone aplication, or implemented by each source side.
 */
public interface IVirtualCenter extends IBaseCenter {
    /**
     * Put the new data to the local cache if the status is in dirty.
     * And set the dirty status to false.
     * @param compKey
     * @param cacheData
     */
    void putToCenter(String compKey, IClientCacheData cacheData);
}
