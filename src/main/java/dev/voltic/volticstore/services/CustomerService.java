package dev.voltic.volticstore.services;

import dev.voltic.volticstore.domain.Customer;
import dev.voltic.volticstore.domain.User;
import dev.voltic.volticstore.repo.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepository repo;

    public Customer getCustomerById(Long id) {
        return repo.getCustomerById(id);
    }

    public List<Customer> listAll() {
        return repo.getAllCustomers();
    }

    public Long count() {
        return repo.count();
    }

    public void saveCustomer(Customer customer) {
        repo.save(customer);
    }

    public Customer createCustomer(User customer) {
        if (repo.getCustomerById(customer.getId()) != null) {
            return repo.getCustomerById(customer.getId());
        } else {
            Customer c = new Customer();
            c.setUser(customer);
            return repo.save(c);
        }
    }

    public Customer getCustomerByUser(Long id) {
        return repo.getCustomerByUser(id);
    }
}