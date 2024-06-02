package dev.voltic.volticstore.domain;


import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "customers")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String phone;
    private String address;

    private String cardNumber;
    private String cardExpiry;
    private String cardCVC;

    @ManyToOne
    private User user;

    @OneToMany
    private List<Product> orders;

}
