package cache.impls.sourcecenter;

import cache.ICenterCacheData;
import cache.IClientCacheData;
import cache.util.ILogable;
import cache.util.IterativeSizeFetcher;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Weigher;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lidong@date 2023-10-25@version 1.0
 */
public class CaffeinePhysicalCenter extends AbstractPhysicalCenter implements ILogable {
    private Cache<String, ICenterCacheData> cache;

    public CaffeinePhysicalCenter() {
        cache = Caffeine.newBuilder()
                .maximumWeight(800)
                .weigher(new Weigher<String, ICenterCacheData>() {
                    @Override
                    public int weigh(String key, ICenterCacheData value) {
                        int ret = key.getBytes().length + IterativeSizeFetcher.getDeepObjectSize(value);
                        getLogger().info("The key + value {} size is:{}",key,ret);
                        return ret;
                    }
                })
                .build();
    }


    public ICenterCacheData getFromLocalCache(String compKey) {
        return cache.getIfPresent(compKey);
    }

    @Override
    public void putToLocalCache(String compKey, ICenterCacheData cacheData) {
        cache.put(compKey,cacheData);
    }

}
