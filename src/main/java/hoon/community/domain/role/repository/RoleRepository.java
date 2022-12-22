package hoon.community.domain.role.repository;

import hoon.community.domain.role.entity.Role;
import hoon.community.domain.role.entity.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRoleType(RoleType roleType);
}
