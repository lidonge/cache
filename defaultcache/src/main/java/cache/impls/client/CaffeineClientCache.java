package cache.impls.client;

import cache.IClientCacheData;
import cache.client.IPhysicalCache;
import cache.util.ILogable;
import cache.util.IterativeSizeFetcher;
import cache.util.LockerByName;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Weigher;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lidong@date 2023-10-25@version 1.0
 */
public class CaffeineClientCache extends AbstractClientCache {
    private Cache<String, IClientCacheData> cache;

    public CaffeineClientCache() {
        cache = Caffeine.newBuilder()
                .maximumWeight(800)
                .weigher(new Weigher<String, IClientCacheData>() {
                    @Override
                    public int weigh(String key, IClientCacheData value) {
                        int ret = key.getBytes().length + IterativeSizeFetcher.getDeepObjectSize(value);
                        getLogger().info("The key + value {} size is:{}",key,ret);
                        return ret;
                    }
                })
                .build();
    }

    @Override
    public IClientCacheData get(String key) {
        return cache.getIfPresent(key);
    }

    @Override
    public void put(String compKey, IClientCacheData cacheData) {
        cache.put(compKey,cacheData);
    }

    @Override
    public void setDirty(String key) {
        cache.invalidate(key);
    }
}
