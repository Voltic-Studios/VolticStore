package dev.voltic.volticstore.controller;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfWriter;
import dev.voltic.volticstore.domain.Order;
import dev.voltic.volticstore.domain.Product;
import dev.voltic.volticstore.domain.User;
import dev.voltic.volticstore.services.OrderService;
import dev.voltic.volticstore.services.UserService;
import dev.voltic.volticstore.views.FooterEvent;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
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
import java.net.MalformedURLException;
import java.util.List;
import java.util.stream.Collectors;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.PdfPTable;

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
            PdfWriter writer = PdfWriter.getInstance(document, out);
            writer.setPageEvent(new FooterEvent()); // Agregar el evento de pie de página
            document.open();
            // Add image to the document
            String imagePath = new ClassPathResource("static/img/main_logo.png").getURI().getPath();
            Image img = Image.getInstance(imagePath);

            // Convertir cm a puntos y ajustar el tamaño de la imagen
            float cmToPoints = 28.35f;
            img.scaleAbsolute(5 * cmToPoints, 5 * cmToPoints);

            // Posicionar la imagen en la esquina superior derecha
            float x = document.getPageSize().getWidth() - img.getScaledWidth();
            float y = document.getPageSize().getHeight() - img.getScaledHeight();
            img.setAbsolutePosition(x, y);

            document.add(img);

            // Crear una nueva fuente en negrita y de tamaño 20
            Font boldFont = new Font(Font.FontFamily.HELVETICA, 20, Font.BOLD);

            // Crear un nuevo párrafo con la fuente especificada
            Paragraph title = new Paragraph("VOLTIC STORE ", boldFont);

            // Añadir el párrafo al documento
            document.add(title);

            // Add content to the document using the Order object
            document.add(new Paragraph(" ")); // Salto de línea
            document.add(new Paragraph("Order ID: " + order.getId()));
            document.add(new Paragraph("Order Date: " + order.getOrderDate()));
            document.add(new Paragraph("Order Status: " + order.getOrderStatus()));
            document.add(new Paragraph(" ")); // Salto de línea

            Font boldFont2 = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
            document.add(new Paragraph("Customer: ", boldFont2));
            document.add(new Paragraph(order.getCustomer().getName()));
            document.add(new Paragraph(order.getCustomer().getEmail()));
            document.add(new Paragraph(order.getShippingAddress() + ", " + order.getShippingZip()));
            document.add(new Paragraph(order.getShippingCity()));
            document.add(new Paragraph(order.getShippingCountry()));
            document.add(new Paragraph(order.getShippingState()));
            document.add(new Paragraph(" ")); // Salto de línea

            Paragraph orderNumber = new Paragraph("Order Number: " + order.getOrderNumber());
            orderNumber.setAlignment(Element.ALIGN_CENTER);
            document.add(orderNumber);
            document.add(new Paragraph(" ")); // Salto de línea

            // Crear una nueva tabla con 2 columnas
            PdfPTable table = new PdfPTable(2);

            // Ajustar el ancho de las columnas
            float[] columnWidths = {80f, 20f};
            table.setWidths(columnWidths);

            // Crear las frases con la fuente en negrita
            Phrase productPhrase = new Phrase("Product", boldFont2);
            Phrase pricePhrase = new Phrase("Price", boldFont2);

            // Agregar las frases a las celdas de la tabla
            table.addCell(productPhrase);
            table.addCell(pricePhrase);

            // Agregar los datos de los productos a la tabla
            for (Product product : order.getProducts()) {
                table.addCell(product.getName());
                table.addCell(String.valueOf(product.getPrice()) + "€");
            }

            // Agregar la tabla al documento
            document.add(table);

            document.add(new Paragraph(" ")); // Salto de línea
            double total = 0;
            for (Product product : order.getProducts()) {
                total += product.getPrice();
            }
            // Convertir cm a puntos
            float cmToPoints2 = 28.35f;

            // Crear un nuevo párrafo y alinearlo a la derecha
            Paragraph totalParagraph = new Paragraph("Total Price: " + total + "€");
            totalParagraph.setAlignment(Element.ALIGN_RIGHT);

            // Mover el párrafo 2 cm a la izquierda
            totalParagraph.setIndentationRight(2 * cmToPoints2);

            // Agregar el párrafo al documento
            document.add(totalParagraph);
            document.add(new Paragraph(" ")); // Salto de línea
            Paragraph paymentParagraph = new Paragraph("Payment Method: " + order.getPaymentMethod() + "    Payment Date: " + order.getPaymentDate() + "    Payment Status: " + order.getPaymentStatus());
            paymentParagraph.setAlignment(Element.ALIGN_CENTER);
            document.add(paymentParagraph);

            document.close();

        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
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