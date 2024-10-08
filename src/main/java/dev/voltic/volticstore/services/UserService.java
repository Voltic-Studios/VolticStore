package dev.voltic.volticstore.services;

import dev.voltic.volticstore.domain.Role;
import dev.voltic.volticstore.domain.User;
import dev.voltic.volticstore.repo.CartRepository;
import dev.voltic.volticstore.repo.RoleRepository;
import dev.voltic.volticstore.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private UserRepository repo;

    @Autowired
    private RoleRepository roleRepo;

    @Autowired
    private CartRepository cartRepo;

    public User getUserByUsername(String username) {
        return repo.getUserByUsername(username);
    }
    public List<User> listAll() {
        return repo.getAllUsers();
    }

    public Map<String, Long> countUsersByRole() {
        List<User> users = repo.getAllUsers();
        return users.stream()
                .collect(Collectors.groupingBy(user -> user.getRole().getName(), Collectors.counting()));
    }

    // /delete/{id}
    public void deleteUser(Long id) {
        repo.deleteById(id);
    }

    public void saveUser(User user) {
        repo.save(user);
    }


    public boolean save(User user) {
        if (repo.checkIfUserExists(user.getUsername()) == 0) {
            user.setRole(roleRepo.getRoleByName("USER"));
            user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
            user.setCart(cartRepo.createCart(user));

            repo.save(user);
            return true;
        } else {
            return false;
        }
    }
}
