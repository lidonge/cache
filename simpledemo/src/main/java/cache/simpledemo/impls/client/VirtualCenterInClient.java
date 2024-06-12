package cache.simpledemo.impls.client;

import cache.IBaseClient;
import cache.ICacheData;
import cache.center.ICenterCacheData;
import cache.client.IClientCacheData;
import cache.client.IVirtualCenter;
import cache.simpledemo.impls.source.VirtualClient;

/**
 * @author lidong@date 2023-10-25@version 1.0
 */
public class VirtualCenterInClient implements IVirtualCenter {
    private IVirtualCenter virtualCenterInSource;
    private Client client;


    public VirtualCenterInClient(Client client) {
        this.client = client;
        client.setVirtualCenter(this);
    }

    public void setVirtualCenterInSource(IVirtualCenter virtualCenterInSource) {
        this.virtualCenterInSource = virtualCenterInSource;
    }

    //Called from center to client side.
    @Override
    public void onCacheChanged(String compKey) {
        client.prepareDirty(compKey);
    }

    @Override
    public ICacheData get(String compKey) {
        return virtualCenterInSource.get(compKey);
    }

    @Override
    public void put(String compKey, ICenterCacheData cacheData) {
        virtualCenterInSource.put(compKey,cacheData);
    }

    @Override
    public boolean isAgreementReached(String compKey) {
        return virtualCenterInSource.isAgreementReached(compKey);
    }

    @Override
    public boolean registerClient(String name, String key, IBaseClient client) {
        return virtualCenterInSource.registerClient(name,key, new VirtualClient((Client) client));
    }

    @Override
    public void unregisterClient(String name) {
        virtualCenterInSource.unregisterClient(name);
    }

    @Override
    public void putToCenter(String compKey, IClientCacheData cacheData) {
        this.put(compKey, (ICenterCacheData) cacheData);
    }
}
