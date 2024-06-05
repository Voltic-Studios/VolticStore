package dev.voltic.volticstore.controller;

import dev.voltic.volticstore.domain.Product;
import dev.voltic.volticstore.services.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class DashboardController {

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


    @GetMapping("/dashboard")
    public String showDashboard(Model model) {
        // Assuming the DashboardService has a method getDashboardData()
        Object dashboardData = dashboardService.getDashboardData();
        List<Product> productList = dashboardService.getAllProducts();
        model.addAttribute("dashboardData", dashboardData);
        model.addAttribute("orderCount", orderService.count());
        model.addAttribute("orderCount", customerService.count());
        model.addAttribute("userCountByRole", userService.countUsersByRole());
        model.addAttribute("productList", productList);
        return "dashboard";
    }

    @GetMapping("/dashboard/logout")
    public String logout(HttpServletRequest request) {
        request.getSession().invalidate();
        return "redirect:/";
    }


}