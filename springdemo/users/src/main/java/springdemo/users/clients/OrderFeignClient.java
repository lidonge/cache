package springdemo.users.clients;

/**
 * @author lidong@date 2024-06-25@version 1.0
 */

import cache.IVirtualClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import springdemo.users.configs.FeignConfig;

@FeignClient(name = "order-service", configuration = FeignConfig.class)
public interface OrderFeignClient{
    @PostMapping("/orders/cache/prepareDirty/{compKey}")
    void prepareDirty(@PathVariable("compKey") String compKey);
}
