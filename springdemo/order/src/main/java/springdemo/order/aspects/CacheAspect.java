package springdemo.order.aspects;

import cache.IClientCacheData;
import cache.ICompositeKey;
import cache.impls.client.ClientCacheData;
import com.alicp.jetcache.anno.Cached;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import springdemo.order.clients.UserFeignClient;
import springdemo.order.clients.cacheclient.ClientCache;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Aspect
@Component
public class CacheAspect {

    private static final Logger logger = LoggerFactory.getLogger(CacheAspect.class);
    @Autowired
    private UserFeignClient userFeignClient;
    @Autowired
    private ClientCache clientCache;
    Map<String, Optional> localCache = new HashMap<>();
    @Around("@annotation(cached)")
    public Object aroundCachedMethod(ProceedingJoinPoint joinPoint, Cached cached) throws Throwable {
        logger.info("Before executing method: " + joinPoint.getSignature().getName() +",cache: "+ cached.name() + "," +cached.key() +","+cached.cacheType());
        Optional result = null;
        String key = cached.name() + joinPoint.getArgs()[0];

        if(cached.name().equals("userCache-")){
            IClientCacheData cacheData = clientCache.get(new ICompositeKey() {
                @Override
                public String getCompositeKey() {
                    return key;
                }

                @Override
                public String getSeparator() {
                    return ":";
                }
            });
            if (cacheData == null || cacheData.getValue() == null) {
                result = (Optional) joinPoint.proceed();
                cacheData = new ClientCacheData().setValue(result.orElse(null));

                clientCache.putToLocalAndCenter(key, cacheData );
            }
            result = Optional.of(cacheData.getValue());
        }else {
            // Proceed with the original method call
            result = localCache.get(key);
            if (result == null) {
                result = (Optional) joinPoint.proceed();
                localCache.put(key, result);
            }
        }

        logger.info("After executing method: " + joinPoint.getSignature().getName());
        return result;
    }
}
