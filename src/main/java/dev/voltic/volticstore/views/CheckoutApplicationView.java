package dev.voltic.volticstore.views;

import dev.voltic.volticstore.domain.Customer;
import dev.voltic.volticstore.domain.User;
import dev.voltic.volticstore.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Scanner;

@RequestMapping("/api")
@Controller
public class CheckoutApplicationView {

    @RequestMapping("/cart/checkout")
    public String checkout(Principal principal, Model model) {
        User user = userService.getUserByUsername(principal.getName());
        if (!cartService.isCartEmpty(user)) {
            Customer c = customerService.createCustomer(user);
            model.addAttribute("customer", c);

            model.addAttribute("categories", categoryService.getAllCategories()); // Add products to the model
            return "checkout";
        } else {
            return "redirect:/cart";
        }
    }

    @PostMapping("/submit/payment")
    public String submitPayment(@ModelAttribute Customer customer, Principal principal) {
        User user = userService.getUserByUsername(principal.getName());
        System.out.println(customer.toString());

        if (paymentService.confirmPayment(customer, user)) {
            return "success";
        } else {
            return "redirect:/cart?error=true";
        }
    }

    @Autowired
    private UserService userService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CartService cartService;


    @Autowired
    private CategoryService categoryService;

    @Autowired
    private PaymentService paymentService;
}
