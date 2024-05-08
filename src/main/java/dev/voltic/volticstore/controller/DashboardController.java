package dev.voltic.volticstore.controller;

import dev.voltic.volticstore.services.CustomerService;
import dev.voltic.volticstore.services.DashboardService;
import dev.voltic.volticstore.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private CustomerService customerService;


    @GetMapping("/dashboard")
    public String showDashboard(Model model) {
        // Assuming the DashboardService has a method getDashboardData()
        Object dashboardData = dashboardService.getDashboardData();
        model.addAttribute("dashboardData", dashboardData);
        model.addAttribute("orderCount", orderService.count());
        model.addAttribute("orderCount", customerService.count());
        return "dashboard";
    }
}