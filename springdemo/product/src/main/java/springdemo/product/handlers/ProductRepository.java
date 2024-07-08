package springdemo.product.handlers;

import org.springframework.data.jpa.repository.JpaRepository;
import springdemo.product.models.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
