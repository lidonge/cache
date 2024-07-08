package cache.simpledemo.impls.client;

import cache.client.ICache;
import cache.client.IClient;
import cache.client.IPhysicalCache;

/**
 * @author lidong@date 2023-10-25@version 1.0
 */
public class ClientCache implements ICache {
    private IPhysicalCache physicalCache;
    private IClient client;

    public ClientCache(IPhysicalCache physicalCache) {
        this.physicalCache = physicalCache;
    }

    public void setClient(IClient client) {
        this.client = client;
    }

    @Override
    public IClient getClient() {
        return client;
    }

    @Override
    public IPhysicalCache getPhysicalCache() {
        return physicalCache;
    }
}
