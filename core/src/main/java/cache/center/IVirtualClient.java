package cache.center;

import cache.IBaseClient;
import cache.center.IAsynListener;

/**
 * @author lidong@date 2023-10-24@version 1.0
 * A client side of cacher
 */
public interface IVirtualClient extends IBaseClient {
    /**
     * Called asynchronized if a specified data is preparing dirty.
     * @param compKey
     * @return finish listener
     */
    void prepareDirty(String compKey,IAsynListener listener);

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
