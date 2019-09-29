package com.test.chat.entity;

import com.test.chat.enumeration.RoleType;

import javax.persistence.*;

/**
 * Role entity
 */
@Entity
@Table(name = "role")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private RoleType role;

    public Role() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RoleType getRole() {
        return role;
    }

    public void setRole(RoleType role) {
        this.role = role;
    }
}
