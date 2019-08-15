package com.zbf.pojo.entity;

import com.zbf.pojo.base.BaseAuditable;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.List;
import java.util.Map;

@Data
@Entity
@Table(name = "base_user")
public class UserInfo extends BaseAuditable {

    @Column(name = "userName")
    private String userName;

    @Column(name = "loginName")
    private String loginName;

    @Column(name = "password")
    private String password;

    @Column(name = "tel")
    private String tel;

    @Column(name = "sex")
    private int sex;

    @Column(name = "parentId")
    private Long parentId;

    @Column(name = "imageUrl")
    private String imageUrl;

    @Transient
      private List<MenuInfo> listMenuInfo;

    @Transient
    private RoleInfo roleInfo;//用来存储查询出来的角色的

    @Transient
    private Map<String,String> authmap;



}
