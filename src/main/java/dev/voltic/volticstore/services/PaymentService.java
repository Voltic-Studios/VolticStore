package dev.voltic.volticstore.services;

import dev.voltic.volticstore.domain.Cart;
import dev.voltic.volticstore.domain.Customer;
import dev.voltic.volticstore.domain.Order;
import dev.voltic.volticstore.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class PaymentService {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;
    @Autowired
    private CartService cartService;

    public boolean confirmPayment(Customer c, User user) {
        Customer cc = customerService.getCustomerByUser(user.getId());

        if (cc == null) {
            return false;
        }

        cc.setUser(user);

        cc.setAddress(c.getAddress());
        cc.setEmail(c.getEmail());
        cc.setPhone(c.getPhone());
        cc.setName(c.getName());

        cc.setCardCVC(c.getCardCVC());
        cc.setCardExpiry(c.getCardExpiry());
        cc.setCardNumber(c.getCardNumber());

        cc.setOrders(cc.getOrders());

        cartService.clearCart(user);

        userService.save(user);
        customerService.saveCustomer(cc);

        orderService.createOrder(new Order(), cc);

        return true;
    }
}
