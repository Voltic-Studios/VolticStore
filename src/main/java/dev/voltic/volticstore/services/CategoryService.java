package dev.voltic.volticstore.services;

import dev.voltic.volticstore.domain.Category;
import dev.voltic.volticstore.repo.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id).orElse(null);
    }

    //listAll
    public List<Category> listAll() {
        return categoryRepository.findAll();
    }

    public void deleteCategoryById(Long id) {
        categoryRepository.deleteById(id);
    }
    //save
    public void save(Category category) {
        categoryRepository.save(category);
    }
}
