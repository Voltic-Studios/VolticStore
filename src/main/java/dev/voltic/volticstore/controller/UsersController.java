package dev.voltic.volticstore.controller;

import dev.voltic.volticstore.domain.User;
import dev.voltic.volticstore.repo.UserRepository;
import dev.voltic.volticstore.services.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
@Controller
public class UsersController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @GetMapping("/dashboard/users")
    public String showUserList(Model model) {
        List<User> listUsers = userService.listAll();
        model.addAttribute("listUsers", listUsers);
        return "user-list";
    }

    @Transactional
    @GetMapping("/dashboard/delete/{id}")
    public String deleteUser(@PathVariable(name = "id") Long id) {
        userRepository.deleteById(id);
        return "redirect:/dashboard/users";
    }

    @Transactional
    @PostMapping("/dashboard/delete/{id}")
    public String deleteUserPost(@PathVariable(name = "id") Long id) {
        userRepository.deleteById(id);
        return "redirect:/dashboard/users";
    }

    @GetMapping("/dashboard/edit/{id}")
    public String showEditUserForm(@PathVariable(name = "id") Long id, Model model) {
        User user = userRepository.findById(id).get();
        model.addAttribute("user", user);
        return "edit-user";
    }

    @PostMapping("/dashboard/edit/{id}")
    public String updateUser(@PathVariable(name = "id") Long id, @ModelAttribute("user") User updatedUser) {
        User existingUser = userRepository.findById(id).get();
        existingUser.setUsername(updatedUser.getUsername());
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setRole(updatedUser.getRole()); // Agrega esta línea

        userRepository.save(existingUser);
        return "redirect:/dashboard/users";
    }

    @GetMapping("/dashboard/downloadUsers")
    public ResponseEntity<byte[]> downloadUsers(HttpServletResponse response) throws IOException {
        List<User> users = userService.listAll();

        try (
                Workbook workbook = new XSSFWorkbook();
                ByteArrayOutputStream out = new ByteArrayOutputStream();
        ) {
            Sheet sheet = workbook.createSheet("Users");

            // Header
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("ID");
            headerRow.createCell(1).setCellValue("Username");
            headerRow.createCell(2).setCellValue("Email");
            headerRow.createCell(3).setCellValue("Role");

            // Data
            int rowIdx = 1;
            for (User user : users) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(user.getId());
                row.createCell(1).setCellValue(user.getUsername());
                row.createCell(2).setCellValue(user.getEmail());
                row.createCell(3).setCellValue(user.getRole().getName());
            }

            workbook.write(out);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=users.xlsx")
                    .body(out.toByteArray());
        }
    }

    @GetMapping("/dashboard/addUser")
    public String showAddUserForm(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "add-user";
    }

    @PostMapping("/dashboard/addUser")
    public String saveUser(@ModelAttribute("user") User user) {
        userService.saveUser(user);
        return "redirect:/dashboard/users";
    }

    @GetMapping("/panel/profile")
    public String showProfileForm(Model model, Authentication authentication) {
        String currentUsername = authentication.getName();
        User currentUser = userRepository.getUserByUsername(currentUsername);
        model.addAttribute("user", currentUser);
        return "edit-profile";
    }

    @PostMapping("/panel/profile")
    public String updateUserProfile(@ModelAttribute("user") User updatedUser, Authentication authentication) {
        String currentUsername = authentication.getName();
        User currentUser = userRepository.getUserByUsername(currentUsername);
        currentUser.setEmail(updatedUser.getEmail());

        // Check if password field is not empty, then update the password
        if (!updatedUser.getPassword().isEmpty()) {
            // Encrypt the password before saving
            String encodedPassword = passwordEncoder.encode(updatedUser.getPassword());
            currentUser.setPassword(encodedPassword);
        }

        userRepository.save(currentUser);
        return "redirect:/panel/profile";
    }

}
