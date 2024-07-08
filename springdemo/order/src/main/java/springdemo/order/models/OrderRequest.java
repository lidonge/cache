package springdemo.order.models;
import java.util.List;

/**
 * @author lidong@date 2024-06-24@version 1.0
 */

public class OrderRequest {

    private Order order;
    private List<OrderItem> items;

    // Getters and Setters

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }
}
