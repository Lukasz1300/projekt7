package projekt7.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import projekt7.entity.MenuItem;
import projekt7.entity.MenuItemCategory;

import java.util.List;

@Repository
public interface MenuItemCategoryRepository extends JpaRepository<MenuItemCategory, Long> {
    List<MenuItemCategory> findByMenuItem(MenuItem menuItem);
}

