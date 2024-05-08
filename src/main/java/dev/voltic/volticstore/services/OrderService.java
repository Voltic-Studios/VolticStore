package dev.voltic.volticstore.services;

import dev.voltic.volticstore.domain.Order;
import dev.voltic.volticstore.repo.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {
    @Autowired
    private OrderRepository repo;

    public Order getOrderById(Long id) {
        return repo.getOrderById(id);
    }

    public List<Order> listAll() {
        return repo.getAllOrders();
    }

    public Long count() {
        return repo.count();
    }
}