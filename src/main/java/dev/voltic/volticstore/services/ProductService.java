package dev.voltic.volticstore.services;

import dev.voltic.volticstore.domain.Order;
import dev.voltic.volticstore.domain.Product;
import dev.voltic.volticstore.repo.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderService orderService;

    public List<Product> getProductsByCategory(String category) {
        return productRepository.getProductsByCategory(category);
    }

    public List<Product> getProductsByCategoryId(Long id) {
        return productRepository.getProductsByCategoryId(id);
    }

    public Product getProductById(Long id) {
        return productRepository.getProductById(id);
    }

    public List<Product> listAll() {
        return productRepository.getAllProducts();
    }



}
