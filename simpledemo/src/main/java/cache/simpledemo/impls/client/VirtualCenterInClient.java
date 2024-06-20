package cache.simpledemo.impls.client;

import cache.*;
import cache.simpledemo.impls.source.Center;
import cache.simpledemo.impls.source.VirtualClient;

/**
 * @author lidong@date 2023-10-25@version 1.0
 */
public class VirtualCenterInClient implements IVirtualCenterInClient {
    private Center center;
    private Client client;


    public VirtualCenterInClient(Client client) {
        this.client = client;
        client.setVirtualCenter(this);
    }

    public void setCenter(Center center) {
        this.center = center;
    }

    @Override
    public boolean registerClient(String name, String key, IBaseClient client) {
        return center.registerClient(name,key, new VirtualClient((Client) client));
    }

    @Override
    public void unregisterClient(String name) {
        center.unregisterClient(name);
    }

    @Override
    public ICacheData get(String compKey) {
        return center.get(compKey);
    }

    @Override
    public void put(String compKey, ICacheData cacheData) {
        center.put(compKey, (ICenterCacheData) cacheData);
    }

    @Override
    public boolean isAgreementReached(String compKey) {
        return center.isAgreementReached(compKey);
    }
}
