package dev.voltic.volticstore.controller;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import dev.voltic.volticstore.domain.Order;
import dev.voltic.volticstore.domain.Product;
import dev.voltic.volticstore.domain.User;
import dev.voltic.volticstore.services.OrderService;
import dev.voltic.volticstore.services.UserService;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class OrdersController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @GetMapping("/orders")
    public String showOrderList(Model model) {
        List<Order> listOrders = orderService.listAll();
        model.addAttribute("listOrders", listOrders);
        return "order-list";
    }
    @GetMapping("/api/orders/count")
    @ResponseBody
    public Long countOrders() {
        return orderService.count();
    }

    @GetMapping("/downloadOrders")
    public ResponseEntity<byte[]> downloadOrders() throws IOException {
        List<Order> orders = orderService.listAll();

        try (
                Workbook workbook = new XSSFWorkbook();
                ByteArrayOutputStream out = new ByteArrayOutputStream();
        ) {
            Sheet sheet = workbook.createSheet("Orders");

            // Header
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("ID");
            headerRow.createCell(1).setCellValue("Order Number");
            headerRow.createCell(2).setCellValue("Order Date");
            headerRow.createCell(3).setCellValue("Order Status");
            headerRow.createCell(4).setCellValue("Customer");
            headerRow.createCell(5).setCellValue("Products");
            headerRow.createCell(6).setCellValue("Payment Status");

            // Data
            int rowIdx = 1;
            for (Order order : orders) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(order.getId());
                row.createCell(1).setCellValue(order.getOrderNumber());
                row.createCell(2).setCellValue(order.getOrderDate().toString());
                row.createCell(3).setCellValue(order.getOrderStatus());
                row.createCell(4).setCellValue(order.getCustomer().getName());
                // Assuming getProducts() returns a List<Product>
                row.createCell(5).setCellValue(order.getProducts().stream().map(Product::getName).collect(Collectors.joining(", ")));
                row.createCell(6).setCellValue(order.getPaymentStatus());
            }

            workbook.write(out);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=orders.xlsx")
                    .body(out.toByteArray());
        }
    }

    @GetMapping("/downloadOrder/{id}")
    public ResponseEntity<byte[]> downloadOrder(@PathVariable("id") Long id) throws DocumentException {
        Order order = orderService.getOrderById(id);

        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            // Add content to the document using the Order object
            document.add(new Paragraph("Order ID: " + order.getId()));
            document.add(new Paragraph("Order Number: " + order.getOrderNumber()));
            document.add(new Paragraph("Order Date: " + order.getOrderDate()));
            document.add(new Paragraph("Order Status: " + order.getOrderStatus()));
            document.add(new Paragraph("Customer: " + order.getCustomer().getName()));
            document.add(new Paragraph("Email: " + order.getCustomer().getEmail()));
            document.add(new Paragraph("Address: " + order.getShippingAddress()));
            document.add(new Paragraph("City: " + order.getShippingCity()));
            document.add(new Paragraph("State: " + order.getShippingState()));
            document.add(new Paragraph("Zip: " + order.getShippingZip()));
            document.add(new Paragraph("Country: " + order.getShippingCountry()));
            document.add(new Paragraph("Payment Amount: " + order.getPaymentAmount()));
            document.add(new Paragraph("Payment Method: " + order.getPaymentMethod()));
            document.add(new Paragraph("Payment Date: " + order.getPaymentDate()));
            document.add(new Paragraph("Payment Status: " + order.getPaymentStatus()));
            document.add(new Paragraph("Products: " + order.getProducts().stream().map(Product::getName).collect(Collectors.joining(", "))));
            document.add(new Paragraph("Total Price: " + (int) order.getProducts().stream().mapToDouble(Product::getPrice).sum() + "€"));


            document.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=order.pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(out.toByteArray());
    }



    @GetMapping("/viewOrder/{id}")
    public String viewOrder(@PathVariable("id") Long id, Model model) {
        Order order = orderService.getOrderById(id);
        model.addAttribute("order", order);
        return "order-details";
    }

}