package dev.voltic.volticstore.views;


import dev.voltic.volticstore.domain.Category;
import dev.voltic.volticstore.domain.Product;
import dev.voltic.volticstore.services.CategoryService;
import dev.voltic.volticstore.services.LoginService;
import dev.voltic.volticstore.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class MainApplicationView {

    @Autowired
    private CategoryService categoryService;


    @RequestMapping("/")
    public String index(Model model) {
        model.addAttribute("categories", categoryService.getAllCategories()); // Add products to the model

        return "index";
    }

    @ModelAttribute
    public void addAttributes(Model model) {
        model.addAttribute("categories", categoryService.getAllCategories());
    }

}
