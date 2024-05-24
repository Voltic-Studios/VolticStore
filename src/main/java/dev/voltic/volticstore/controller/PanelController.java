package dev.voltic.volticstore.controller;

import dev.voltic.volticstore.domain.Order;
import dev.voltic.volticstore.domain.Product;
import dev.voltic.volticstore.domain.User;
import dev.voltic.volticstore.domain.Customer;
import dev.voltic.volticstore.repo.CustomerRepository;
import dev.voltic.volticstore.repo.OrderRepository;
import dev.voltic.volticstore.repo.UserRepository;
import dev.voltic.volticstore.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;

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

    @GetMapping("/panel")
    public String showPanel(Model model, Principal principal) {
        Object dashboardData = dashboardService.getDashboardData();
        model.addAttribute("dashboardData", dashboardData);
        model.addAttribute("orderCount", orderService.count());
        model.addAttribute("customerCount", customerService.count());
        model.addAttribute("userCountByRole", userService.countUsersByRole());
        model.addAttribute("productList", productService.listAll());
        // Obtén el nombre de usuario del usuario logueado
        String username = principal.getName();


        // Busca el usuario en la base de datos
        User user = userRepository.findByUsername(username);

        // Añade el ID del usuario al modelo
        model.addAttribute("userId", user.getId());
        model.addAttribute("username", username);

        // Busca el cliente asociado al usuario
        Customer customer = customerRepository.findByUser(user);

        // Añade el ID del cliente al modelo
        model.addAttribute("customerId", customer.getId());

        // Añade el recuento de pedidos del cliente al modelo
        long orderCount = orderService.countByCustomerId(customer.getId());
        model.addAttribute("myOrderCount", orderCount);

        return "panel";
    }

    @GetMapping("/myOrders")
    public String showMyOrders(Model model, Principal principal) {
        // Obtén el nombre de usuario del usuario logueado
        String username = principal.getName();

        // Busca el usuario en la base de datos
        User user = userRepository.findByUsername(username);

        // Busca el cliente asociado al usuario
        Customer customer = customerRepository.findByUser(user);

        // Busca las órdenes que pertenecen a este cliente
        List<Order> orders = orderRepository.findByCustomerId(customer.getId());

        // Añade las órdenes al modelo
        model.addAttribute("orders", orders);

        return "myOrders";
    }

    @GetMapping("/myProducts")
    public String showMyProducts(Model model, Principal principal) {
        // Obtén el nombre de usuario del usuario logueado
        String username = principal.getName();
        // Busca el usuario en la base de datos
        User user = userRepository.findByUsername(username);
        // Busca el cliente asociado al usuario
        Customer customer = customerRepository.findByUser(user);
        // Busca los productos que pertenecen a este cliente
        List<Product> products = productService.findByCustomerId(customer.getId());
        // Añade los productos al modelo
        model.addAttribute("products", products);
        return "myProducts";
    }
}