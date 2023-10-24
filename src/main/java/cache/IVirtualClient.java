package cache;

/**
 * @author lidong@date 2023-10-24@version 1.0
 * A client side of cacher
 */
public interface IVirtualClient {
    /**
     * Called if a specified data is preparing dirty.
     * @param compKey
     */
    void prepareDirty(String compKey);
}
