package dev.voltic.volticstore.repo;

import dev.voltic.volticstore.domain.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface CartRepository extends JpaRepository<Cart, Long> {

    // Delete a specific product from the cart
    @Modifying
    @Query("DELETE FROM Cart c WHERE c.id = ?1")
    void deleteProductFromCart(Long id);
}
