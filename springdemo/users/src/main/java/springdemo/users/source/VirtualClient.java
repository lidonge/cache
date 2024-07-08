package springdemo.users.source;

import cache.IAsynListener;
import cache.IVirtualClient;
import cache.util.ILogable;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import springdemo.users.clients.DynamicFeignClient;
import springdemo.users.clients.OrderFeignClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lidong@date 2023-10-25@version 1.0
 */
public class VirtualClient implements IVirtualClient, ILogable {

    private DiscoveryClient discoveryClient;

    private Map<String, Boolean> keysMap = new HashMap<>();

    private String name ;

    public VirtualClient(String name, DiscoveryClient discoveryClient) {
        this.name = name;
        this.discoveryClient = discoveryClient;
    }


    @Override
    public String getName() {
        return name;
    }

    @Override
    public void prepareDirty(String compKey, IAsynListener listener) {
        dynFeignClient(compKey).prepareDirty(compKey);
        listener.onSuccess();
    }

    private OrderFeignClient dynFeignClient(String compKey) {
        List<ServiceInstance> instances = discoveryClient.getInstances(name);
        if (instances == null || instances.isEmpty()) {
            throw new IllegalArgumentException("No instances found for service: " + name);
        }
        String url = instances.get(0).getUri().toString();  // 获取第一个实例的URL
        getLogger().info("Get {}'s url:{}", name,url);
        DynamicFeignClient dynamicFeignClient = new DynamicFeignClient();
        return dynamicFeignClient.createOrderServiceClient(url);
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
