package dev.voltic.volticstore.repo;

import dev.voltic.volticstore.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT p FROM Product p WHERE p.category = :category")
    List<Product> getProductsByCategory(@Param("category") String category);

    // Category id
    @Query("SELECT p FROM Product p WHERE p.category.id = :id")
    List<Product> getProductsByCategoryId(@Param("id") Long id);
}
