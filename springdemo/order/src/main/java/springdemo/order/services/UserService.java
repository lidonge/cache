package springdemo.order.services;

import com.alicp.jetcache.anno.Cached;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import springdemo.product.models.Product;
import springdemo.users.models.AppUser;

import java.util.Optional;

import static com.alicp.jetcache.anno.CacheType.LOCAL;

@Service
public class UserService {

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private ApplicationContext applicationContext;
    private final String userServiceUrl = "http://localhost:8081/users";

    @Cached(name="userCache-", key="#id", expire = 3600, cacheType = LOCAL)
    public Optional<AppUser> findById(Long id) {
        try {
            AppUser product = restTemplate.getForObject(userServiceUrl + "/" + id, AppUser.class);
            return Optional.ofNullable(product);
        } catch (Exception e) {
            // Handle the exception (e.g., log it)
            return Optional.empty();
        }
    }
}
