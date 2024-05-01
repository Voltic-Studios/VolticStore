package dev.voltic.volticstore.views;

import dev.voltic.volticstore.domain.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AuthApplicationView {

    @RequestMapping("/register")
    public String register(Model model) {
        model.addAttribute("user", new User());

        return "register";
    }

    @PostMapping("/register/submit")
    public String registerSubmit(@ModelAttribute User user) {
        System.out.println(user.toString());
        return "redirect:/login";
    }
}
