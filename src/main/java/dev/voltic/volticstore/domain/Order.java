package dev.voltic.volticstore.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "orders")
@Data
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String orderNumber;
    private String orderDate;
    private String orderStatus;

    @ManyToOne
    private Customer customer;

    @ManyToMany
    private List<Product> products;

    private String shippingAddress;
    private String shippingCity;
    private String shippingState;
    private String shippingZip;
    private String shippingCountry;

    private String paymentMethod;
    private String paymentDate;
    private String paymentStatus;

    private double paymentAmount;
}
