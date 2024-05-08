package dev.voltic.volticstore.repo;

import dev.voltic.volticstore.domain.Product;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ProductRepository extends CrudRepository<Product, Long> {

    @Query("SELECT p FROM Product p WHERE p.id = :id")
    Product getProductById(Long id);

    @Query("SELECT p FROM Product p")
    List<Product> getAllProducts();
}