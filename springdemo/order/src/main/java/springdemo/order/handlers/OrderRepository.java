package springdemo.order.handlers;
import org.springframework.data.jpa.repository.JpaRepository;
import springdemo.order.models.Order;

/**
 * @author lidong@date 2024-06-24@version 1.0
 */

public interface OrderRepository extends JpaRepository<Order, Long> {

}
