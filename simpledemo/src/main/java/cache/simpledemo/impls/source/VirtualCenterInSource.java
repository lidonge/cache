package cache.simpledemo.impls.source;

import cache.IBaseClient;
import cache.ICacheData;
import cache.ILogable;
import cache.center.IAsynListener;
import cache.center.IVirtualClient;
import cache.client.IVirtualCenter;

import java.util.Map;

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
    public boolean isAgreementReached(String compKey) {
        return center.isAgreementReached(compKey);
    }

    @Override
    public void registerClient(String name, IBaseClient client) {
        center.registerClient(name,client);
    }

    @Override
    public void unrigister(String name) {
        center.unrigister(name);
    }
}
