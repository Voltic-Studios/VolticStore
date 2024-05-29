package dev.voltic.volticstore.views;

import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.PaymentIntent;
import dev.voltic.volticstore.domain.Customer;
import dev.voltic.volticstore.domain.User;
import dev.voltic.volticstore.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@RequestMapping("/api")
@Controller
public class CheckoutApplicationView {

    @Autowired
    private UserService userService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CartService cartService;

    @Autowired
    private StripeService stripeService;

    @Autowired
    private CategoryService categoryService;


    @Value("${stripe.public.key}")
    private String stripePublicKey;

    @RequestMapping("/cart/checkout")
    public String checkout(Principal principal, Model model) {
        User user = userService.getUserByUsername(principal.getName());
        if (!cartService.isCartEmpty(user)) {
            Customer c = customerService.createCustomer(user);
            model.addAttribute("customer", c);
            model.addAttribute("apiKey", stripePublicKey);

            model.addAttribute("categories", categoryService.getAllCategories()); // Add products to the model
            return "checkout";
        } else {
            return "redirect:/cart";
        }
    }

    @PostMapping("/customer/save")
    public String saveCustomer(@ModelAttribute Customer customer, Model model) {
        customerService.saveCustomer(customer);
        model.addAttribute("total", cartService.getTotalAmount(customer.getUser().getCart()));

        return "redirect:/api/create-payment-intent";
    }

    @PostMapping("/create-payment-intent")
    public String createPaymentIntent(@RequestParam int amount, Model model) throws StripeException {
        Charge charge = stripeService.charge(amount);
        model.addAttribute("charge", charge);
        model.addAttribute("checkoutPaySuccess", true);
        return "redirect:/cart";
    }

    @PostMapping("/charge")
    public Charge charge(@RequestParam int amount) throws StripeException {
        return stripeService.charge(amount);
    }
}
