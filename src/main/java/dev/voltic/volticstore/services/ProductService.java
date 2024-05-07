package dev.voltic.volticstore.services;

import dev.voltic.volticstore.domain.Product;
import dev.voltic.volticstore.repo.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<Product> getProductsByCategory(String category) {
        return productRepository.getProductsByCategory(category);
    }

    public List<Product> getProductsByCategoryId(Long id) {
        return productRepository.getProductsByCategoryId(id);
    }


}
