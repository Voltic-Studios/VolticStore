package dev.voltic.volticstore.controller;

import dev.voltic.volticstore.domain.Order;
import dev.voltic.volticstore.domain.Product;
import dev.voltic.volticstore.domain.User;
import dev.voltic.volticstore.domain.Customer;
import dev.voltic.volticstore.repo.CustomerRepository;
import dev.voltic.volticstore.repo.OrderRepository;
import dev.voltic.volticstore.repo.UserRepository;
import dev.voltic.volticstore.services.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@RequestMapping("/panel")
@Controller
public class PanelController {

    @Autowired
    private DashboardService dashboardService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private OrderRepository orderRepository;

    @GetMapping
    public String showPanel(Model model, Principal principal) {
        Object dashboardData = dashboardService.getDashboardData();
        model.addAttribute("dashboardData", dashboardData);
        model.addAttribute("orderCount", orderService.count());
        model.addAttribute("userCountByRole", userService.countUsersByRole());
        model.addAttribute("productList", productService.listAll());
        String username = principal.getName();
        User user = userRepository.findByUsername(username);
        model.addAttribute("userId", user.getId());
        model.addAttribute("username", username);
        Customer customer = customerRepository.findByUser(user);

        if (customer != null) {
            model.addAttribute("customerId", customer.getId());
            long orderCount = orderService.countByCustomerId(customer.getId());
            model.addAttribute("myOrderCount", orderCount);
        }

        return "panel";
    }

    @GetMapping("/myOrders")
    public String showMyOrders(Model model, Principal principal) {
        String username = principal.getName();
        User user = userRepository.findByUsername(username);
        Customer customer = customerRepository.findByUser(user);

        if (customer != null) {
            List<Order> orders = orderRepository.findByCustomerId(customer.getId());
            model.addAttribute("orders", orders);
        }

        return "myOrders";
    }

    @GetMapping("/myProducts")
    public String showMyProducts(Model model, Principal principal) {
        String username = principal.getName();
        User user = userRepository.findByUsername(username);
        Customer customer = customerRepository.findByUser(user);

        if (customer != null) {
            List<Product> products = productService.findByCustomerId(customer.getId());
            model.addAttribute("products", products);
        }

        return "myProducts";
    }

    @GetMapping("/logout")
    public String logoutPage(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/";
    }
}