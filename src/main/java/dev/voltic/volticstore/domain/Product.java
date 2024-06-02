package dev.voltic.volticstore.domain;


import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "packages")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private String image;
    private double price;
    private int stock;
    private int orders;

    @ManyToOne
    private Category category;

    public void setOrders(int orders) {
        this.orders = orders;
    }

}
