package dev.voltic.volticstore.views;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainApplicationView {

    @GetMapping("/")
    public String index() {
        return "index";
    }
}
