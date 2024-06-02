package dev.voltic.volticstore.services;

import dev.voltic.volticstore.domain.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DashboardService {

    @Autowired
    private ProductService productService;


    public Object getDashboardData() {
        // Fetch the dashboard data here
        // For now, we'll return a placeholder object
        return new Object();
    }
    public List<Product> getAllProducts() {
        return productService.listAll();
    }
}