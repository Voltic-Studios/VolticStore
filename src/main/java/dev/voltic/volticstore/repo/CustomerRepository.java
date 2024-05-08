package dev.voltic.volticstore.repo;

import dev.voltic.volticstore.domain.Customer;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CustomerRepository extends CrudRepository<Customer, Long> {

    @Query("SELECT c FROM Customer c WHERE c.id = :id")
    Customer getCustomerById(Long id);

    @Query("SELECT c FROM Customer c")
    List<Customer> getAllCustomers();
}