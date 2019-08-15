package com.manger.dao;

import com.zbf.pojo.entity.UserRoleInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;

@Transactional
public interface UserRoleMapper extends JpaRepository<UserRoleInfo,Long> {
    //根据userId删除中间表的数据
    public void deleteByUserId(Long userId);
}
