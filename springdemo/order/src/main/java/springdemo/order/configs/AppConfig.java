package springdemo.order.configs;
import com.alicp.jetcache.anno.config.EnableCreateCacheAnnotation;
import com.alicp.jetcache.anno.config.EnableMethodCache;
import com.alicp.jetcache.anno.support.ConfigProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @author lidong@date 2024-06-24@version 1.0
 */


@Configuration
@EnableMethodCache(basePackages = "springdemo.order")
@EnableCreateCacheAnnotation
public class AppConfig {

    @Bean
    public ConfigProvider configProvider() {
        return new ConfigProvider();
    }
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
