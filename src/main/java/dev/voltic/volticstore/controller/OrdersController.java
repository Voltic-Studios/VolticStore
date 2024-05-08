package dev.voltic.volticstore.controller;

import dev.voltic.volticstore.domain.Order;
import dev.voltic.volticstore.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class OrdersController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/orders")
    public String showOrderList(Model model) {
        List<Order> listOrders = orderService.listAll();
        model.addAttribute("listOrders", listOrders);
        return "order-list";
    }
    @GetMapping("/api/orders/count")
    @ResponseBody
    public Long countOrders() {
        return orderService.count();
    }
}