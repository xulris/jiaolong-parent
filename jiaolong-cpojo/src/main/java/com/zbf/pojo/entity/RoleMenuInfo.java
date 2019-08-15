package com.zbf.pojo.entity;


import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "base_role_menu")
public class RoleMenuInfo {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "roleId")
    private Long roleId;

    @Column(name = "menuId")
    private Long menuId;
}
