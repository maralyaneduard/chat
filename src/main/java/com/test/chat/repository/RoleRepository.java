package com.test.chat.repository;

import com.test.chat.entity.Role;
import com.test.chat.enumeration.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository to manage Role entities
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByRole(RoleType role);
}
