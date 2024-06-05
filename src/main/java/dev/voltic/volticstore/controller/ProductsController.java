package dev.voltic.volticstore.controller;

import dev.voltic.volticstore.domain.Category;
import dev.voltic.volticstore.domain.Product;
import dev.voltic.volticstore.services.CategoryService;
import dev.voltic.volticstore.services.ProductService;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Controller
public class ProductsController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/dashboard/products")
    public String showProductList(Model model) {
        List<Product> listProducts = productService.listAll();
        model.addAttribute("listProducts", listProducts);
        return "product-list";
    }

    @GetMapping("/api/downloadProducts")
    public ResponseEntity<byte[]> downloadProducts() throws IOException {
        List<Product> products = productService.listAll();

        try (
                Workbook workbook = new XSSFWorkbook();
                ByteArrayOutputStream out = new ByteArrayOutputStream();
        ) {
            Sheet sheet = workbook.createSheet("Products");

            // Header
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("ID");
            headerRow.createCell(1).setCellValue("Name");
            headerRow.createCell(2).setCellValue("Price");

            // Data
            int rowIdx = 1;
            for (Product product : products) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(product.getId());
                row.createCell(1).setCellValue(product.getName());
                row.createCell(2).setCellValue(product.getPrice());
            }

            workbook.write(out);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=products.xlsx")
                    .body(out.toByteArray());
        }
    }

    @GetMapping("/dashboard/addProduct")
    public String showAddProductForm(Model model) {
        List<Category> categories = categoryService.listAll();
        model.addAttribute("categories", categories);
        Product product = new Product();
        product.setOrders(0); // Establecer el valor por defecto de orders a 0
        model.addAttribute("product", product);
        return "add-product";
    }

    @PostMapping("/dashboard/addProduct")
    public String addProduct(@Valid @ModelAttribute("product") Product product, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "add-product";
        }
        productService.save(product);
        return "redirect:/dashboard/products";
    }

    @GetMapping("/dashboard/deleteProduct/{id}")
    public String deleteProduct(@PathVariable(name = "id") Long id) {
        productService.deleteById(id);
        return "redirect:/dashboard/products";
    }

    @GetMapping("/dashboard/editProduct/{id}")
    public String showEditProductForm(@PathVariable(name = "id") Long id, Model model) {
        Product product = productService.getProductById(id);
        List<Category> categories = categoryService.listAll();
        model.addAttribute("product", product);
        model.addAttribute("categories", categories);
        return "edit-product";
    }

    @PostMapping("/dashboard/editProduct/{id}")
    public String updateProduct(@PathVariable(name = "id") Long id, @ModelAttribute("product") Product updatedProduct) {
        Product existingProduct = productService.getProductById(id);
        existingProduct.setName(updatedProduct.getName());
        existingProduct.setDescription(updatedProduct.getDescription());
        existingProduct.setImage(updatedProduct.getImage());
        existingProduct.setPrice(updatedProduct.getPrice());
        existingProduct.setStock(updatedProduct.getStock());
        existingProduct.setOrders(updatedProduct.getOrders());
        existingProduct.setCategory(updatedProduct.getCategory());

        productService.save(existingProduct);
        return "redirect:/dashboard/products";
    }

}