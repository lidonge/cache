package cache;

/**
 * @author lidong@date 2023-10-24@version 1.0
 * A client side of cacher
 */
public interface IVirtualClient extends IBaseClient, IPrepareDirtyHandler {
    /**
     * Add a new key to the client.
     * @param key
     */
    void addKey(String key);

    /**
     * Test if the key is registered by the client.
     * @param key
     * @return
     */
    boolean hasKey(String key);
}
