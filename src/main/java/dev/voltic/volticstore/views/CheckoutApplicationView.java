package dev.voltic.volticstore.views;

import dev.voltic.volticstore.domain.Customer;
import dev.voltic.volticstore.domain.User;
import dev.voltic.volticstore.services.CartService;
import dev.voltic.volticstore.services.CustomerService;
import dev.voltic.volticstore.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
public class CheckoutApplicationView {

    @Autowired
    private UserService userService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CartService cartService;

    @RequestMapping("/api/cart/checkout")
    public String checkout(Principal principal, Model model) {
        User user = userService.getUserByUsername(principal.getName());

        Customer c = customerService.createCustomer(user);

        // cartService.clearCart(user);

        model.addAttribute("customer", c);

        return "checkout";
    }

    @PostMapping("/api/customer/save")
    public String saveCustomer(Customer customer) {
        customerService.saveCustomer(customer);
        return "redirect:/api/pay/paypal";
    }

    @GetMapping("/api/pay/paypal")
    public String payWithPaypal() {
        return "redirect:/";
    }
}
