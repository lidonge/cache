package cache.impls.client;

import cache.IClientCacheData;
import cache.client.IPhysicalCache;
import cache.util.ILogable;
import cache.util.LockerByName;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lidong@date 2023-10-25@version 1.0
 */
public class SimplePhysicalClientCache extends AbstractClientCache {
    private Map<String, IClientCacheData> map = new HashMap<>();

    public SimplePhysicalClientCache() {
    }

    @Override
    public IClientCacheData get(String key) {
        return map.get(key);
    }

    @Override
    public void put(String compKey, IClientCacheData cacheData) {
        map.put(compKey,cacheData);
    }

    @Override
    public void setDirty(String key) {
        map.remove(key);
    }

}
