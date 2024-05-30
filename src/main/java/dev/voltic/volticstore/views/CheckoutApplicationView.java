package dev.voltic.volticstore.views;

import com.google.gson.Gson;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.net.Webhook;
import dev.voltic.volticstore.domain.Customer;
import dev.voltic.volticstore.domain.User;
import dev.voltic.volticstore.request.ConfigResponse;
import dev.voltic.volticstore.request.CreatePaymentRequest;
import dev.voltic.volticstore.request.CreatePaymentResponse;
import dev.voltic.volticstore.request.FailureResponse;
import dev.voltic.volticstore.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RequestMapping("/api")
@Controller
public class CheckoutApplicationView {

    @Value("${stripe.public.key}")
    private String stripePublishableKey;

    @Value("${stripe.api.key}")
    private String stripeWebhookSecret;

    private final Gson gson = new Gson();

    @GetMapping("/config")
    @ResponseBody
    public ResponseEntity<ConfigResponse> getConfig() {
        return ResponseEntity.ok(new ConfigResponse(stripePublishableKey));
    }

    @PostMapping("/create-payment-intent")
    @ResponseBody
    public ResponseEntity<?> createPaymentIntent(@RequestBody CreatePaymentRequest createPaymentRequest) {
        try {
            PaymentIntent intent = paymentService.createPaymentIntent(createPaymentRequest);
            return ResponseEntity.ok(new CreatePaymentResponse(intent.getClientSecret()));
        } catch (StripeException e) {
            return ResponseEntity.status(400).body(new FailureResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e);
        }
    }

    @GetMapping("/payment/next")
    public String getNextPayment(@RequestParam("payment_intent") String paymentIntentId) throws StripeException {
        PaymentIntent intent = PaymentIntent.retrieve(paymentIntentId);
        return "redirect:/success?payment_intent_client_secret=" + intent.getClientSecret();
    }

    @GetMapping("/success")
    public String getSuccess() {
        return "redirect:/success";
    }

    @PostMapping("/webhook")
    @ResponseBody
    public ResponseEntity<String> handleWebhook(@RequestBody String payload, @RequestHeader("Stripe-Signature") String sigHeader) {
        try {
            Event event = Webhook.constructEvent(payload, sigHeader, stripeWebhookSecret);

            switch (event.getType()) {
                case "payment_intent.succeeded":
                    System.out.println("💰Payment received!");
                    break;
                case "payment_intent.payment_failed":
                    System.out.println("❌ Payment failed.");
                    break;
                default:
                    return ResponseEntity.status(400).body("");
            }

            return ResponseEntity.ok("");
        } catch (SignatureVerificationException e) {
            return ResponseEntity.status(400).body("");
        }
    }

    @RequestMapping("/cart/checkout")
    public String checkout(Principal principal, Model model) {
        User user = userService.getUserByUsername(principal.getName());
        if (!cartService.isCartEmpty(user)) {
            Customer c = customerService.createCustomer(user);
            model.addAttribute("customer", c);
            model.addAttribute("apiKey", stripePublishableKey);

            model.addAttribute("categories", categoryService.getAllCategories()); // Add products to the model
            return "checkout";
        } else {
            return "redirect:/cart";
        }
    }
/*
    @PostMapping("/customer/save")
    public String saveCustomer(@ModelAttribute Customer customer, Model model) {
        customerService.saveCustomer(customer);
        model.addAttribute("total", cartService.getTotalAmount(customer.getUser().getCart()));

        return "redirect:/api/create-payment-intent";
    }

    @PostMapping("/create-payment-intent")
    public String createPaymentIntent(@RequestParam int amount, Model model) throws StripeException {
        Charge charge = stripeService.charge(stripePublicKey, amount);
        model.addAttribute("charge", charge);
        model.addAttribute("checkoutPaySuccess", true);
        return "redirect:/cart";
    }

    @PostMapping("/charge")
    public Charge charge(@RequestParam String token, @RequestParam int amount) throws StripeException {
        return stripeService.charge(token, amount);
    } */

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

    @Autowired
    private PaymentService paymentService;
}
