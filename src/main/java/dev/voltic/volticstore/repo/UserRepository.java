package dev.voltic.volticstore.repo;

import dev.voltic.volticstore.domain.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends CrudRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE u.username = :username")
    User getUserByUsername(String username);

    @Query("SELECT u FROM User u WHERE u.email = :email")
    User getUserByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.role.name = 'USER'")
    List<User> getAllUsers();

    @Query("SELECT COUNT(u) FROM User u WHERE u.role.name = 'ADMIN'")
    long countAdmins();


    @Query("SELECT u FROM User u WHERE u.role.name = :role")
    List<User> getUsersByRole(@Param("role") String role);

    @Query("SELECT u FROM User u WHERE u.id = :id")
    User getUserById(@Param("id") Long id);
}
