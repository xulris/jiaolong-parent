package com.manger.dao;

import com.zbf.pojo.entity.UserInfo;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface UserMapper extends JpaRepository<UserInfo,Long> {

    //分页查询，带模糊
    public Page<UserInfo> getByUserNameLike(String userName, Pageable pageable);

    //根据roleId查询user表
    @Query(value = "select bu.* from base_user_role bur INNER JOIN base_user bu ON bur.userId = bu.id where bur.roleId = ?1",nativeQuery = true)
    public List<UserInfo> getUserInfoByRoleId(Long roleId);


    @Query(value = "select * from base_user where loginName=?1",nativeQuery = true)
    public UserInfo findByLonginName( String loginName);

}
