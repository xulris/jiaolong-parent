package com.sso.dao;

import com.zbf.pojo.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Map;


public interface UserDao extends JpaRepository<UserInfo,Long> {

    //登录
    @Query(value = "select * from base_user where loginName=?1",nativeQuery = true)
    public UserInfo findByLonginName(String loginName);

    //根据ID查询用户信息
    @Query(value = "select * from base_user where id=?1",nativeQuery = true)
    public Map<String,Object> getUserById(String userid);



}
