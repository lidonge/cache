package springdemo.users.source;

import cache.IVirtualClient;
import cache.util.ILogable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lidong@date 2024-06-26@version 1.0
 */
@Service
public class VirtualClients implements ILogable {
    private Map<String, IVirtualClient> clients = new HashMap<>();
    @Autowired
    private DiscoveryClient discoveryClient;

    public VirtualClients() {
    }

    public IVirtualClient get(String name) {
        IVirtualClient ret = clients.get(name);
        if (ret == null) {
            ret = new VirtualClient(name, discoveryClient);
            clients.put(name, ret);
        }

        return ret;
    }
}
