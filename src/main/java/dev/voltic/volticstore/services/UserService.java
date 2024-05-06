package dev.voltic.volticstore.services;

import dev.voltic.volticstore.domain.User;
import dev.voltic.volticstore.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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
}
