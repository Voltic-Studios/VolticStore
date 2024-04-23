package dev.voltic.volticstore.services;

import dev.voltic.volticstore.domain.User;
import dev.voltic.volticstore.repo.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoginService {

    private static final Logger logger = LoggerFactory.getLogger(LoginService.class);

    @Autowired
    public UserRepository userRepository;

    public boolean login(String username, String password) {
        try {
            return userRepository.getUserByUsername(username).getPassword().equals(password);
        } catch (Exception e) {
            logger.error("Error during login for user: " + username, e);
            return false;
        }
    }

    public List<User> getAllUsers() {
        return userRepository.getAllUsers();
    }
}