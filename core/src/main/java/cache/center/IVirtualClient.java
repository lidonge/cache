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
    IAsynListener prepareDirty(String compKey);
}
