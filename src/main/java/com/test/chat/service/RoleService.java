package com.test.chat.service;

import com.test.chat.entity.Role;

import java.util.List;

/**
 * Service to manage roles
 */
public interface RoleService {
    /**
     * Returns all roles
     * @return List of all roles
     */
    List<Role> getAllRoles();
}
