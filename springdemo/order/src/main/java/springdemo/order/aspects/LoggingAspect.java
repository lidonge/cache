package springdemo.order.aspects;


import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author lidong@date 2024-06-25@version 1.0
 */

@Aspect
@Component
public class LoggingAspect {

//    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);
//
//    @Before("execution(* springdemo.order.services.OrderService.getOrder(..)) && args(id)")
//    public void logBefore(Long id) {
//        logger.info("Fetching Order with ID: " + id);
//    }
//
//    @AfterReturning(pointcut = "execution(* springdemo.order.services.OrderService.getOrder(..))", returning = "result")
//    public void logAfterReturning(Object result) {
//        logger.info("Order fetched successfully: " + result);
//    }
//
//    @After("execution(* springdemo.order.services.OrderService.getOrder(..))")
//    public void logAfter() {
//        logger.info("Completed fetching order");
//    }
//
//    @Around("execution(* springdemo.order.services.OrderService.getOrder(..))")
//    public Object logAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
//        logger.info(" Executing getOrder method with ID " + proceedingJoinPoint.getArgs()[0]);
//
//        // Call the advised method
//        return proceedingJoinPoint.proceed();
//    }
}
