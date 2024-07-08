package springdemo.order.services;

import com.alicp.jetcache.anno.Cached;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import springdemo.order.clients.ProductFeignClient;
import springdemo.product.models.Product;
/**
 * @author lidong@date 2024-06-24@version 1.0
 */


import java.util.Optional;

import static com.alicp.jetcache.anno.CacheType.LOCAL;

@Service
public class ProductService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ProductFeignClient productFeignClient;

    @Autowired
    private ApplicationContext applicationContext;
    private final String productServiceUrl = "http://localhost:8083/products";
    // 通过代理对象调用缓存方法
    public Optional<Product> findProductById(Long id) {
        ProductService proxy = applicationContext.getBean(ProductService.class);
        return proxy.findById(id);
    }

    @Cached(name="productCache-", key="#id", expire = 3600, cacheType = LOCAL)
    public Optional<Product> findById(Long id) {
        try {
            Product product = null;
            if(false)
                product = restTemplate.getForObject(productServiceUrl + "/" + id, Product.class);
            else
                product = productFeignClient.getProductById(id);
            return Optional.ofNullable(product);
        } catch (Exception e) {
            // Handle the exception (e.g., log it)
            return Optional.empty();
        }
    }
}
