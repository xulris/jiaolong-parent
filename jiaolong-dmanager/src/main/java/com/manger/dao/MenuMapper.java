package com.manger.dao;

import com.zbf.pojo.entity.MenuInfo;
import com.zbf.pojo.entity.UserInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MenuMapper  extends JpaRepository<MenuInfo,Long> {

    //根据id查询menu表
    public MenuInfo getById(Long id);

    //根据parentId查询menu
    public List<MenuInfo> getByParentId(Long id);

   //根据roleId查询menu
    @Query(value = "select bm.* from base_role_menu brm INNER JOIN base_menu bm on brm.menuId = bm.id where brm.roleId = ?1",nativeQuery = true)
    public List<MenuInfo> getByRoleId(Long id);


    public int countByMenuName(String name);

    public List<MenuInfo> getByUrl(String url);

    @Query(value = "SELECT * FROM base_role_menu brm INNER JOIN ( SELECT b.* FROM base_menu b, ( SELECT parentId FROM base_menu WHERE url = ?1 ) a WHERE b.parentId = a.parentId ) bm ON brm.menuId = bm.id WHERE brm.roleId = ?2",nativeQuery = true)
    public List<MenuInfo> getByUrlAndRoleIdBrotherMenus(String url,Long roleId);

}
