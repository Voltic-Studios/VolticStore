package dev.voltic.volticstore.services;

import dev.voltic.volticstore.domain.Cart;
import dev.voltic.volticstore.domain.Product;
import dev.voltic.volticstore.domain.User;
import dev.voltic.volticstore.repo.CartRepository;
import dev.voltic.volticstore.repo.ProductRepository;
import dev.voltic.volticstore.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepo;

    @Autowired
    private ProductRepository productRepo;


    public void addToCart(Long productId, User user) {
        Cart cart = user.getCart();
        List<Product> items = cart.getItems();

        for (Product product : items) {
            if (product.getId().equals(productId)) {
                // Product already exists in the cart, so return early
                return;
            }
        }

        items.add(productRepo.getProductById(productId));
        cart.setTotalAmount(cart.getTotalAmount() + productRepo.getProductById(productId).getPrice());

        cartRepo.save(cart);
    }

    public boolean isCartEmpty(User user) {
        return user.getCart().getItems().isEmpty();
    }

    public void removeFromCart(Long productId, User user) {
        Cart cart = user.getCart();
        List<Product> items = cart.getItems();

        items.removeIf(product -> product.getId().equals(productId));
        cart.setTotalAmount(cart.getTotalAmount() - productRepo.getProductById(productId).getPrice());

        cartRepo.save(cart);
    }

    public double getTotalAmount(Cart cart) {
        return cart.getTotalAmount();
    }
}
