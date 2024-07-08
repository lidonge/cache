package springdemo.product.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

/**
 * @author lidong@date 2024-06-24@version 1.0
 */


@Entity
public class Product {
    @Id
    private Long id;
    private String name;
    private String category;
    private String descn;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescn() {
        return descn;
    }

    public void setDescn(String descn) {
        this.descn = descn;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", descn='" + descn + '\'' +
                '}';
    }
}