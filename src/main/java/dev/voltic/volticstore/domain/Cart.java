package dev.voltic.volticstore.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Table(name = "cart")
@Entity
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany
    private List<Product> items;

    private double totalAmount;

    @OneToOne(mappedBy = "cart")
    private User user;
}
