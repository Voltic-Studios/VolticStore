package dev.voltic.volticstore.services;

import dev.voltic.volticstore.domain.Cart;
import dev.voltic.volticstore.domain.Product;
import dev.voltic.volticstore.domain.User;
import dev.voltic.volticstore.repo.CartRepository;
import dev.voltic.volticstore.repo.ProductRepository;
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

        if (cart == null) {
            cart = new Cart();
            user.setCart(cart);
        }

        List<Product> items = cart.getItems();

        if (items == null) {
            items = new ArrayList<>();
            cart.setItems(items);
        }

        Product product = productRepo.findById(productId).orElseThrow(() -> new RuntimeException("Product not found"));
        items.add(product);

        cartRepo.save(cart);
    }

    public void removeFromCart(Long productId, User user) {
        // cartRepo.deleteProductFromCart(productId);
        Cart cart = user.getCart();
        List<Product> items = cart.getItems();
        items.removeIf(product -> product.getId().equals(productId));
        cartRepo.save(cart);
    }

    public void clearCart(User user) {
        Cart cart = user.getCart();
        cart.setItems(new ArrayList<>());
        cart.setTotalAmount(0);
        cartRepo.save(cart);
    }

    public List<Product> getCartItems(User user) {
        return user.getCart().getItems();
    }

    public double getTotalAmount(Cart cart) {
        for (Product product : cart.getItems()) {
            cart.setTotalAmount(cart.getTotalAmount() + product.getPrice());
        }

        return cart.getTotalAmount();
    }
}
