package dev.voltic.volticstore.views;

import dev.voltic.volticstore.domain.User;
import dev.voltic.volticstore.repo.UserRepository;
import dev.voltic.volticstore.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AuthApplicationView {

    @Autowired
    public UserService userRepository;

    @RequestMapping("/register")
    public String register(Model model) {
        model.addAttribute("NewUser", new User());
        return "register";
    }

    @PostMapping("/register/submit")
    public String registerSubmit(@ModelAttribute User user) {
        System.out.println(user.toString());
        if (userRepository.save(user)) {
            return "redirect:/";
        } else {
            return "redirect:/register?alreadyExists=true";
        }
    }
}
