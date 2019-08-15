package com.sso.dao;

import com.zbf.pojo.entity.RoleInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RoleDao extends JpaRepository<RoleInfo,Long> {

    /**
     * 根据用户id获取角色信息
     */
    @Query(value ="select br.* from base_user_role bur INNER JOIN base_role br ON bur.roleId=br.id where bur.userId=?1" ,nativeQuery = true)
    public RoleInfo forRoleInfoByUserId(Long userId);
}
