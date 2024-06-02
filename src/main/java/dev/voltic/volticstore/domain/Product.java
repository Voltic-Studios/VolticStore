package dev.voltic.volticstore.domain;


import jakarta.persistence.*;
import lombok.Data;
import lombok.Setter;

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
    @Setter
    private int orders;

    @ManyToOne
    private Category category;

}
