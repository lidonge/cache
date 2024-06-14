package cache.client;

import cache.IClientCacheData;
import cache.ICompositeKey;

/**
 * @author lidong@date 2024-06-12@version 1.0
 */
public interface IBusinessService {
    IClientCacheData getData(ICompositeKey key);
}
