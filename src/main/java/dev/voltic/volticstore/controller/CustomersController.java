package dev.voltic.volticstore.controller;

import dev.voltic.volticstore.domain.Customer;
import dev.voltic.volticstore.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class CustomersController {

    @Autowired
    private CustomerService customerService;

    @GetMapping("/customers")
    public String showCustomerList(Model model) {
        List<Customer> listCustomers = customerService.listAll();
        model.addAttribute("listCustomers", listCustomers);
        return "customer-list";
    }
}