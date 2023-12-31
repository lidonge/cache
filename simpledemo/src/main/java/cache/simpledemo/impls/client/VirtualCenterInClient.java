package cache.simpledemo.impls.client;

import cache.IBaseClient;
import cache.ICacheData;
import cache.client.IVirtualCenter;
import cache.simpledemo.impls.source.VirtualCenterInSource;
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
    public boolean isAgreementReached(String compKey) {
        return virtualCenterInSource.isAgreementReached(compKey);
    }

    @Override
    public void registerClient(String name, IBaseClient client) {
        virtualCenterInSource.registerClient(name,new VirtualClient((Client) client));
    }

    @Override
    public void unrigister(String name) {
        virtualCenterInSource.unrigister(name);
    }
}
