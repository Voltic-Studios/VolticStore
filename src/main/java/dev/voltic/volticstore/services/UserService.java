package dev.voltic.volticstore.services;

import dev.voltic.volticstore.domain.User;
import dev.voltic.volticstore.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private UserRepository repo;

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
}
