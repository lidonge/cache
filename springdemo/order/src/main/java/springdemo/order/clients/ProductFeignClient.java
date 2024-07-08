package springdemo.order.clients;

/**
 * @author lidong@date 2024-06-25@version 1.0
 */

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import springdemo.product.models.Product;

@FeignClient(name = "product-service")
public interface ProductFeignClient {

    @GetMapping("/products/{id}")
    Product getProductById(@PathVariable("id") Long id);
}
