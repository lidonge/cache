package cache.simpledemo.impls.source;

import cache.center.IAsynListener;
import cache.center.IVirtualClient;
import cache.simpledemo.impls.client.Client;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lidong@date 2023-10-25@version 1.0
 */
public class VirtualClient implements IVirtualClient {
    private Client client;

    private Map<String, Boolean> keysMap = new HashMap<>();

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

    @Override
    public void addKey(String key) {
        keysMap.put(key, true);
    }

    @Override
    public boolean hasKey(String key) {
        return keysMap.get(key) != null;
    }
}
