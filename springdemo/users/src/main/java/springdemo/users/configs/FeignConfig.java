package springdemo.users.configs;

/**
 * @author lidong@date 2024-07-01@version 1.0
 */

import org.springframework.cloud.openfeign.support.SpringMvcContract;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {

    @Bean
    public SpringMvcContract feignContract() {
        return new SpringMvcContract();
    }
}