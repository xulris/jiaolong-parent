package com.manger.dao;

import com.zbf.pojo.entity.RoleMenuInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;

@Transactional
public interface RoleMenuMapper extends JpaRepository<RoleMenuInfo,Long> {
    //根据roleId删除role表
    public void deleteByRoleId(Long roleId);

    public void deleteByMenuId(Long MenuId);
}
