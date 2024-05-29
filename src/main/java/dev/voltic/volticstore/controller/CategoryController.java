package dev.voltic.volticstore.controller;

import dev.voltic.volticstore.domain.Category;
import dev.voltic.volticstore.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/categories")
    public String showCategories(Model model) {
        model.addAttribute("categories", categoryService.getAllCategories());
        return "categories-list";
    }

    @GetMapping("/deleteCategory/{id}")
    public String deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategoryById(id);
        return "redirect:/categories";
    }

    @GetMapping("/editCategory/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Category category = categoryService.getCategoryById(id);
        model.addAttribute("category", category);
        return "edit-category";
    }
    @PostMapping("/editCategory/{id}")
    public String editCategory(@PathVariable Long id, Category category) {
        category.setId(id); // Ensure the category has the correct ID
        categoryService.save(category);
        return "redirect:/categories";
    }

    @GetMapping("/addCategory")
    public String showAddForm(Model model) {
        model.addAttribute("category", new Category());
        return "add-category";
    }

    @PostMapping("/addCategory")
    public String addCategory(Category category) {
        categoryService.save(category);
        return "redirect:/categories";
    }
}