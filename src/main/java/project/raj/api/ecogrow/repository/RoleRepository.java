package project.raj.api.ecogrow.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.raj.api.ecogrow.models.Role;

import java.util.Collection;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Collection<Role> findByNameIn(Collection<String> names);

    Role getByName(String name);
}
