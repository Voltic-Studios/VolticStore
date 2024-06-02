package dev.voltic.volticstore.repo;

import dev.voltic.volticstore.domain.Customer;
import dev.voltic.volticstore.domain.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CustomerRepository extends CrudRepository<Customer, Long> {

    @Query("SELECT c FROM Customer c WHERE c.user.id = :id")
    Customer getCustomerById(Long id);

    @Query("SELECT c FROM Customer c")
    List<Customer> getAllCustomers();

    // Añade un método para buscar un cliente por su usuario
    @Query("SELECT c FROM Customer c WHERE c.user = :user")
    Customer findByUser(User user);

    // Get last order
    @Query("SELECT c FROM Customer c ORDER BY c.id DESC")
    Customer getLastOrder();

    // Get customer by user
    @Query("SELECT c FROM Customer c WHERE c.user.id = :id")
    Customer getCustomerByUser(Long id);
}