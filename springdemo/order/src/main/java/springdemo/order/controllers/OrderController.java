package springdemo.order.controllers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springdemo.order.models.OrderRequest;
import springdemo.order.services.OrderService;
import springdemo.order.models.Order;
import springdemo.order.models.OrderItem;

import java.util.List;

/**
 * @author lidong@date 2024-06-24@version 1.0
 */


@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping("/{id}")
    public Order getOrder(@PathVariable("id") Long id) {
        return orderService.getOrder(id);
    }

    @PostMapping
    public void placeOrder(@RequestBody OrderRequest orderRequest) {
        orderService.placeOrder(orderRequest.getOrder(), orderRequest.getItems());
    }
}