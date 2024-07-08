package springdemo.order.clients;

/**
 * @author lidong@date 2024-06-25@version 1.0
 */

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import springdemo.product.models.Product;
import springdemo.users.models.AppUser;
import springdemo.users.source.VirtualClient;

@FeignClient(name = "user-service")
public interface UserFeignClient {

    @GetMapping("/users/cache/{id}")
    Product getProductById(@PathVariable("id") Long id);

    @PostMapping("/users/cache/put")
    public void put(@RequestBody AppUser user);
    @PostMapping("/users/cache/register/{name}/{key}")
    public boolean registerClient(@PathVariable("name") String name, @PathVariable("key") String key);

    @PostMapping("/users/cache/un-register/{name}")
    public void unregisterClient(@PathVariable("name") String name );
    @GetMapping("/users/cache/get/{compKey}")
    public AppUser getUser(@PathVariable("compKey") String compKey);

    @GetMapping("/users/cache/isAllAgree/{compKey}")
    public boolean isAgreementReached(@PathVariable("compKey") String compKey);
}
