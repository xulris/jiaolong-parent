package com.sso.dao;

import com.zbf.pojo.entity.MenuInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MenuDao extends JpaRepository<MenuInfo,Long> {

    @Query(value = "select bm.* from base_role_menu brm inner join base_menu bm on brm.menuId = bm.id where brm.roleId = ?1",nativeQuery = true)
    List<MenuInfo> getAllMenuByRoleId(Long id);




}
