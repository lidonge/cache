package springdemo.users.clients;

/**
 * @author lidong@date 2024-06-25@version 1.0
 */

import feign.Feign;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import org.springframework.cloud.openfeign.support.SpringMvcContract;

public class DynamicFeignClient {

    public OrderFeignClient createOrderServiceClient(String url) {
        return Feign.builder()
                .contract(new SpringMvcContract())
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .target(OrderFeignClient.class, url);
    }
}