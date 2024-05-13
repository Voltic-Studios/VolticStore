package dev.voltic.volticstore.controller;

import dev.voltic.volticstore.domain.User;
import dev.voltic.volticstore.repo.UserRepository;
import dev.voltic.volticstore.services.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Controller
public class UsersController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/users")
    public String showUserList(Model model) {
        List<User> listUsers = userService.listAll();
        model.addAttribute("listUsers", listUsers);
        return "user-list";
    }

    @Transactional
    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable(name = "id") Long id) {
        userRepository.deleteById(id);
        return "redirect:/users";
    }

    @Transactional
    @PostMapping("/delete/{id}")
    public String deleteUserPost(@PathVariable(name = "id") Long id) {
        userRepository.deleteById(id);
        return "redirect:/users";
    }

    @GetMapping("/edit/{id}")
    public String showEditUserForm(@PathVariable(name = "id") Long id, Model model) {
        User user = userRepository.findById(id).get();
        model.addAttribute("user", user);
        return "edit-user";
    }

    @PostMapping("/edit/{id}")
    public String updateUser(@PathVariable(name = "id") Long id, @ModelAttribute("user") User updatedUser) {
        User existingUser = userRepository.findById(id).get();
        existingUser.setUsername(updatedUser.getUsername());
        existingUser.setEmail(updatedUser.getEmail());
        // Actualiza aquí más campos según sea necesario
        userRepository.save(existingUser);
        return "redirect:/users";
    }

}
