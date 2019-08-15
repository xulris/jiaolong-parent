package com.manger.service;


import com.manger.dao.*;
import com.zbf.pojo.entity.MenuInfo;
import com.zbf.pojo.entity.RoleInfo;
import com.zbf.pojo.entity.UserInfo;
import com.zbf.pojo.entity.UserRoleInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
@Component
public class UserService {

    @Resource
    UserMapper userMapper;

    @Resource
    RoleMapper roleMapper;

    @Resource
    MenuMapper menuMapper;

    @Resource
    RoleMenuMapper roleMenuMapper;

    @Resource
    UserRoleMapper userRoleMapper;


    //分页模糊
    public Page<UserInfo> findUserAll(Map<String,Object> map){
       Integer page=0;
       Integer size=5;
       if (map.get("page")!=null && map.get("size")!=null){
           page=Integer.valueOf(map.get("page").toString());
           size=Integer.valueOf(map.get("size").toString());
           System.out.println("page:"+page+"size:"+size);
       }
        Page<UserInfo> all = userMapper.getByUserNameLike("%"+map.get("mohu").toString()+"%",PageRequest.of(page,size,Sort.by(Sort.Order.desc("createTime"))));
        for (UserInfo userInfo:all){
            //把根据用户ID查询出所对应的角色，存到UserInfo实体类里面的roleInfo里
            userInfo.setRoleInfo(roleMapper.getRoleByUserId(userInfo.getId()));
        }
        return all;
    }

    //根据id查询一条
    public MenuInfo findMenuById(Long id){
        MenuInfo byId = menuMapper.getById(id);
        List<MenuInfo> byParentId = menuMapper.getByParentId(byId.getId());
        byId.setMenuInfoList(byParentId);
        return byId;
    }

    //查询role列表
    public List<RoleInfo> findAllRole(){
        List<RoleInfo> all = roleMapper.findAll();
        return all;
    }

    //添加
    public void addUser(UserInfo userInfo){

        userMapper.save(userInfo);
    }

    //删除
    public void delUserById(Long id){
        System.out.println("删除user："+id);
        userMapper.deleteById(id);
    }

    //绑定角色
    public void putRoleUserId(Long userId,Long roleId){
        UserRoleInfo userRoleInfo = new UserRoleInfo();
        userRoleInfo.setRoleId(roleId);
        userRoleInfo.setUserId(userId);
        userRoleMapper.deleteByUserId(userId);//先删除中间表数据
        userRoleMapper.save(userRoleInfo);//保存修改后的数据
    }
    //判断登录名唯一性
    public boolean AddLoginName(String loginName ){
        UserInfo userInfo = userMapper.findByLonginName(loginName);
        if (userInfo==null ){
            return true; //如果数据库没有就是true
        }else {
            return false;  //如果数据库有就是false
        }
    }

    public List<MenuInfo> getBrotherMenusByUrl(String url,String roleId) {
        List<MenuInfo> byUrlAndRoleIdBrotherMenus = menuMapper.getByUrlAndRoleIdBrotherMenus(url, Long.parseLong(roleId));
        return byUrlAndRoleIdBrotherMenus;
    }
}
