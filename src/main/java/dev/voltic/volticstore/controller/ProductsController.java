package dev.voltic.volticstore.controller;

import dev.voltic.volticstore.domain.Product;
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
import org.springframework.web.bind.annotation.GetMapping;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Controller
public class ProductsController {

    @Autowired
    private ProductService productService;

    @GetMapping("/products")
    public String showProductList(Model model) {
        List<Product> listProducts = productService.listAll();
        model.addAttribute("listProducts", listProducts);
        return "product-list";
    }

    @GetMapping("/downloadProducts")
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

}