package com.manger.service;

import com.manger.dao.*;
import com.zbf.pojo.entity.MenuInfo;
import com.zbf.pojo.entity.RoleInfo;
import com.zbf.pojo.entity.UserInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class MenuService {

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

    /*添加*/
    public void save(MenuInfo menuInfo) {
        menuMapper.save(menuInfo);
    }

    @Transactional
    public void deleteMenuById(String[] ids) {
        for (String id : ids) {
            roleMenuMapper.deleteByMenuId(Long.parseLong(id));
            menuMapper.deleteById(Long.parseLong(id));
        }
    }

    public int checkedMenuName(String menuName) {
        int i = menuMapper.countByMenuName(menuName);
        return i;
    }

    public Set<RoleInfo> getRoleByMenuId(String[] ids) {

        Set<RoleInfo> set = new HashSet<>();
        for (String id : ids) {
            List<RoleInfo> roleByMenuId = roleMapper.getRoleByMenuId(Long.parseLong(id));
            for (RoleInfo roleInfo : roleByMenuId) {
                set.add(roleInfo);
            }
        }
        return set;
    }

}
