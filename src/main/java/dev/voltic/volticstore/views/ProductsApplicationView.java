package dev.voltic.volticstore.views;

import dev.voltic.volticstore.services.CategoryService;
import dev.voltic.volticstore.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ProductsApplicationView {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @RequestMapping("/products/{category}")
    public String category(@PathVariable("category") Long category, Model model) {
        model.addAttribute("currentCategory", categoryService.getCategoryById(category)); // Add current category to the model
        model.addAttribute("categories", categoryService.getAllCategories()); // Add products to the model
        model.addAttribute("products", productService.getProductsByCategoryId(category));
        return "products";
    }

    @RequestMapping("/products/{category}/{product}")
    public String product(@PathVariable("category") Long category, @PathVariable("product") Long product, Model model) {
        model.addAttribute("currentCategory", categoryService.getCategoryById(category)); // Add current category to the model
        model.addAttribute("categories", categoryService.getAllCategories()); // Add products to the model
        model.addAttribute("product", productService.getProductById(product));
        return "product";
    }
}