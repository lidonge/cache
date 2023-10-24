package cache;

/**
 * @author lidong@date 2023-10-24@version 1.0
 * A cache managing center, may be a stand-alone aplication, or implemented by each source side.
 */
public interface IVirtualCenter {
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
}
