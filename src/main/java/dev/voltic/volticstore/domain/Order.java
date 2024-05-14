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

    private String orderNumber; //x
    private String orderDate; // x
    private String orderStatus; // x

    @ManyToOne
    private Customer customer; // x

    @ManyToMany
    private List<Product> products; // quantity

    private String shippingAddress;
    private String shippingCity;
    private String shippingState;
    private String shippingZip;
    private String shippingCountry;

    private String billingAddress;
    private String billingCity;
    private String billingState;
    private String billingZip;
    private String billingCountry;

    private int paymentAmount;

    private String paymentMethod;
    private String paymentDate;
    private String paymentStatus; // x
}
