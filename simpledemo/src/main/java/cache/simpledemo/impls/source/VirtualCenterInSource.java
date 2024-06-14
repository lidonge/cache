package cache.simpledemo.impls.source;

import cache.IBaseClient;
import cache.ICacheData;
import cache.ILogable;
import cache.ICenterCacheData;
import cache.IClientCacheData;
import cache.IVirtualCenter;

/**
 * @author lidong@date 2023-10-25@version 1.0
 */
public class VirtualCenterInSource implements IVirtualCenter, ILogable {
    Center center;
    public VirtualCenterInSource(Center center) {
        this.center = center;
    }

    @Override
    public void onCacheChanged(String compKey) {
        center.onCacheChanged(compKey);
    }

    @Override
    public ICacheData get(String compKey) {
        return center.get(compKey);
    }

    @Override
    public void put(String compKey, ICenterCacheData cacheData) {
        center.put(compKey,cacheData);
    }

    @Override
    public boolean isAgreementReached(String compKey) {
        return center.isAgreementReached(compKey);
    }

    @Override
    public boolean registerClient(String name, String key, IBaseClient client) {
        return center.registerClient(name,key,client);
    }

    @Override
    public void unregisterClient(String name) {
        center.unregisterClient(name);
    }

    @Override
    public void putToCenter(String compKey, IClientCacheData cacheData) {
        throw new RuntimeException("Should not be called in center.");
    }
}
