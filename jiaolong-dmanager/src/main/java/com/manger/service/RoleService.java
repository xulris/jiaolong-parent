package com.manger.service;


import com.manger.dao.MenuMapper;
import com.manger.dao.RoleMapper;
import com.manger.dao.RoleMenuMapper;
import com.manger.dao.UserMapper;
import com.zbf.pojo.entity.MenuInfo;
import com.zbf.pojo.entity.RoleInfo;
import com.zbf.pojo.entity.RoleMenuInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class RoleService {

    @Autowired
    RoleMapper roleMapper;

    @Autowired
    MenuMapper menuMapper;

    @Autowired
    UserMapper userMapper;

    @Autowired
    RoleMenuMapper roleMenuMapper;

    //role列表
    public Page<RoleInfo> getAllRole(Map<String,Object> map){
        Integer page=0;
        Integer size=5;
        if (map.get("page")!=null && map.get("size")!=null){
            page=Integer.valueOf(map.get("page").toString());
            size=Integer.valueOf(map.get("size").toString());
            System.out.println("page:"+page+"size:"+size);
        }
        Page<RoleInfo> all = roleMapper.getByRoleNameLike("%" + map.get("mohu").toString() + "%", PageRequest.of(page, size));
       for (RoleInfo roleInfo:all){
           roleInfo.setMenuList(menuMapper.getByRoleId(roleInfo.getId()));
           roleInfo.setUserList(userMapper.getUserInfoByRoleId(roleInfo.getId()));
       }
       return all;
    }

    //根据id删除role表
    public void delRoleById(Long id){
        roleMapper.deleteById(id);
    }

    //添加role表
    public void addRole(RoleInfo roleInfo){
        roleMapper.save(roleInfo);
    }

    //
    public List<MenuInfo> getAllMenu(){
        List<MenuInfo> allMenus = menuMapper.findAll();
       List<MenuInfo> list = new ArrayList<>();

       for (MenuInfo menuInfo:allMenus){
           if (menuInfo.getLeval()==1){
               list.add(menuInfo);
           }
       }

       this.getForMenuInfo(list,allMenus);

       return list;
    }

    public void getForMenuInfo(List<MenuInfo> list,List<MenuInfo> allMenus){

        for (MenuInfo menuInfo:list){
            List<MenuInfo> chilMenus=new ArrayList<>();
            for (MenuInfo allMenu:allMenus){
                if (menuInfo.getId()== allMenu.getParentId()){
                    chilMenus.add(allMenu);
                }
            }

            menuInfo.setMenuInfoList(chilMenus);
            chilMenus = new ArrayList<>();
            if(menuInfo.getMenuInfoList().size() > 0){
                getForMenuInfo(menuInfo.getMenuInfoList(),allMenus);
            }
        }

        }
    public void putMenuByRoleId(Map<String,Object> map){
        if (map.get("roleId")!=null && map.get("menuIds")!=null){

            roleMenuMapper.deleteByRoleId(Long.valueOf(map.get("roleId").toString()));

            String[] menuIds = map.get("menuIds").toString().split(",");
            for (String menuId:menuIds){
                RoleMenuInfo roleMenuInfo = new RoleMenuInfo();
                roleMenuInfo.setRoleId(Long.parseLong(map.get("roleId").toString()));
                roleMenuInfo.setMenuId(Long.parseLong(menuId));
                roleMenuMapper.save(roleMenuInfo);
            }
        }
    }



}
