package dev.voltic.volticstore.views;

import dev.voltic.volticstore.domain.User;
import dev.voltic.volticstore.security.user.CustomUserDetails;
import dev.voltic.volticstore.services.CartService;
import dev.voltic.volticstore.services.CategoryService;
import dev.voltic.volticstore.services.ProductService;
import dev.voltic.volticstore.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
public class ProductsApplicationView {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CartService cartService;

    @Autowired
    private UserService userService;

    @RequestMapping("/products/{category}")
    public String category(@PathVariable("category") Long category, Model model) {
        model.addAttribute("currentCategory", categoryService.getCategoryById(category)); // Add current category to the model
        model.addAttribute("categories", categoryService.getAllCategories()); // Add products to the model
        model.addAttribute("products", productService.getProductsByCategoryId(category));
        return "products";
    }

    @RequestMapping("/products/{category}/{product}")
    public String product(@PathVariable("category") Long category, @PathVariable("product") Long product, Model model) {
        model.addAttribute("currentCategory", categoryService.getCategoryById(category)); // Add current category to the model
        model.addAttribute("categories", categoryService.getAllCategories()); // Add products to the model
        model.addAttribute("product", productService.getProductById(product));
        return "product";
    }

    @RequestMapping("/api/cart/add/{id}")
    public String addToCart(@PathVariable("id") Long id, Principal principal) {
        User user = userService.getUserByUsername(principal.getName());
        cartService.addToCart(id, user);
        return "redirect:/";
    }

    @RequestMapping("/api/cart/remove/{id}")
    public String removeFromCart(@PathVariable("id") Long id, Principal principal) {
        User user = userService.getUserByUsername(principal.getName());
        cartService.removeFromCart(id, user);
        return "redirect:/cart";
    }

    @RequestMapping("/cart")
    public String cart(Model model, Principal principal) {
        User user = userService.getUserByUsername(principal.getName());
        model.addAttribute("cartItems", user.getCart().getItems());
        model.addAttribute("totalAmount", cartService.getTotalAmount(user.getCart()));
        return "cart";
    }
}