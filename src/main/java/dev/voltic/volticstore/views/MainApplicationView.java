package dev.voltic.volticstore.views;


import dev.voltic.volticstore.services.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainApplicationView {

    @Autowired
    private LoginService loginService;

    @RequestMapping("/")
    public String index(Model model) {
        model.addAttribute("users", loginService.getAllUsers());
        return "index";
    }
}
