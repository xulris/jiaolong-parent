package com.manger.dao;

import com.zbf.pojo.entity.RoleInfo;
import com.zbf.pojo.entity.UserInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface RoleMapper  extends JpaRepository<RoleInfo,Long> {

    //根据用户ID查询所对应的角色
    @Query(value = "select br.* from base_user_role bur INNER JOIN base_role br ON bur.roleId=br.id where bur.userId=?1",nativeQuery = true)
    RoleInfo getRoleByUserId(Long id);

    //role列表，模糊分页
    @Query
    public Page<RoleInfo> getByRoleNameLike(String roleName, Pageable pageable);

    public int countByRoleName(String name);

    @Query(value = "select br.* from base_role_menu brm INNER JOIN base_role br on brm.roleId = br.id where brm.menuId = ?1",nativeQuery = true)
    public List<RoleInfo> getRoleByMenuId(Long id);

    @Query(value = "select bm.id from base_role_menu brm INNER JOIN base_menu bm on brm.menuId = bm.id where brm.roleId = ?1",nativeQuery = true)
    public List<Map<String,Long>> getMenuIdByRoleId(Long roleId);
}
