package cache.impls.sourcecenter;

import cache.ICenterCacheData;
import cache.util.ILogable;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lidong@date 2023-10-25@version 1.0
 */
public class SimplePhysicalCenter extends AbstractPhysicalCenter implements ILogable {
    private Map<String, ICenterCacheData> localCache = new HashMap<>();
//    private Map<String, Boolean> agreementFlags = new ConcurrentHashMap<>();

    public ICenterCacheData getFromLocalCache(String compKey) {
        return localCache.get(compKey);
    }

    @Override
    public void putToLocalCache(String compKey, ICenterCacheData cacheData) {
        localCache.put(compKey,cacheData);
    }

}
