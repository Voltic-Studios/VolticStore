package dev.voltic.volticstore.services;

import dev.voltic.volticstore.domain.Customer;
import dev.voltic.volticstore.domain.Order;
import dev.voltic.volticstore.domain.Product;
import dev.voltic.volticstore.repo.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
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

    public void save(Order order) {
        repo.save(order);
    }

    public void createOrder(Order order, Customer c) {
        order.setCustomer(c);
        order.setOrderDate(new Date().toString());
        order.setOrderStatus("Delivered");

        List<Product> products = new ArrayList<>(c.getUser().getCart().getItems());
        order.setProducts(products);

        order.setShippingAddress(c.getAddress());

        order.setOrderNumber("ORD-" + count());

        order.setPaymentMethod("Card");
        order.setPaymentDate(new Date().toString());
        order.setPaymentStatus("Paid");

        order.setPaymentAmount(c.getUser().getCart().getTotalAmount());

        save(order);
    }
}