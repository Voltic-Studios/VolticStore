package dev.voltic.volticstore.services;

import dev.voltic.volticstore.domain.Product;
import dev.voltic.volticstore.repo.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    @Autowired
    private ProductRepository repo;

    public Product getProductById(Long id) {
        return repo.getProductById(id);
    }

    public List<Product> listAll() {
        return repo.getAllProducts();
    }
}