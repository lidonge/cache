package springdemo.order.clients.cacheclient;

import cache.client.ICache;
import cache.client.IClient;
import cache.client.IPhysicalCache;
import cache.impls.client.SimplePhysicalClientCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author lidong@date 2023-10-25@version 1.0
 */
@Service
public class ClientCache implements ICache {
//    @Autowired
    private IPhysicalCache physicalCache;
    @Autowired
    private IClient client;

    public ClientCache() {
        this.physicalCache = new SimplePhysicalClientCache();
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
