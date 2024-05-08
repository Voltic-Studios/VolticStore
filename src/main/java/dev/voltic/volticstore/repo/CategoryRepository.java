package dev.voltic.volticstore.repo;

import dev.voltic.volticstore.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
