package springdemo.product.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springdemo.product.handlers.ProductRepository;
import springdemo.product.models.Product;


@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @PostMapping
    public Product createProduct(@RequestBody Product product) {
        Product user1 =  productRepository.save(product);
        return user1;
    }

    @GetMapping("/{id}")
    public Product getProduct(@PathVariable("id") Long id) {
        Product product= productRepository.findById(id).orElse(null);
        return product;
    }
}
