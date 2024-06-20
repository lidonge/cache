package cache;

/**
 * @author lidong@date 2023-10-24@version 1.0
 * A cache managing center, may be a stand-alone aplication, or implemented by each source side.
 */
public interface IVirtualCenterInSource {
    /**
     * Called when cached content specified by a composite key changed.
     * This results in dirty cached data and a clear agreement flag.
     * And notify all client to prepare dirty
     * @param compKey
     */
    void onCacheChanged(String compKey);

}
