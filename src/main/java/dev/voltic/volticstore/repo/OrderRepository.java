package dev.voltic.volticstore.repo;

import dev.voltic.volticstore.domain.Order;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface OrderRepository extends CrudRepository<Order, Long> {

    @Query("SELECT o FROM Order o WHERE o.id = :id")
    Order getOrderById(Long id);

    @Query("SELECT o FROM Order o")
    List<Order> getAllOrders();

    List<Order> findByCustomerId(Long customerId);

    long count();
  
    long countByCustomerId(Long customerId);
}