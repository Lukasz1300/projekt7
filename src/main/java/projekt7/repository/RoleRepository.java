package projekt7.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import projekt7.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByName(String name);
}
