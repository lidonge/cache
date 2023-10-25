package cache.simpledemo.impls.source;

import cache.center.IAsynListener;
import cache.center.IVirtualClient;
import cache.simpledemo.impls.client.Client;

/**
 * @author lidong@date 2023-10-25@version 1.0
 */
public class VirtualClient implements IVirtualClient {
    private Client client;

    public VirtualClient(Client client) {
        this.client = client;
    }

    @Override
    public String getName() {
        return client.getName();
    }

    @Override
    public IAsynListener prepareDirty(String compKey) {
        client.prepareDirty(compKey);
        return new IAsynListener() {
        };
    }
}
