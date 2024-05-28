package dev.voltic.volticstore.views;

import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.PaymentIntent;
import dev.voltic.volticstore.domain.Customer;
import dev.voltic.volticstore.domain.User;
import dev.voltic.volticstore.services.CartService;
import dev.voltic.volticstore.services.CustomerService;
import dev.voltic.volticstore.services.StripeService;
import dev.voltic.volticstore.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @RequestMapping("/cart/checkout")
    public String checkout(Principal principal, Model model) {
        User user = userService.getUserByUsername(principal.getName());
        if (!cartService.isCartEmpty(user)) {
            Customer c = customerService.createCustomer(user);
            model.addAttribute("customer", c);
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
    public PaymentIntent createPaymentIntent(@RequestParam int amount) throws StripeException {
        return stripeService.createPaymentIntent(amount, "EUR");
    }

    @PostMapping("/charge")
    public Charge charge(@RequestParam String token, @RequestParam int amount) throws StripeException {
        return stripeService.charge(token, amount);
    }
}
