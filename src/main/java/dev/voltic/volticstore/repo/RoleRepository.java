package dev.voltic.volticstore.repo;

import dev.voltic.volticstore.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
}