package com.zbf.pojo.entity;

import com.zbf.pojo.base.BaseAuditable;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.List;

@Data
@Entity
@Table(name = "base_role")
public class RoleInfo extends BaseAuditable {

    @Column(name = "roleName")
    private String roleName;

    @Column(name = "miaoShu")
    private String miaoShu;

    @Transient
    private List<MenuInfo> menuList;

    @Transient
    private List<UserInfo> userList;

}
