package com.sso.service;

import com.sso.dao.MenuDao;
import com.sso.dao.RoleDao;
import com.sso.dao.UserDao;
import com.zbf.pojo.entity.MenuInfo;
import com.zbf.pojo.entity.RoleInfo;
import com.zbf.pojo.entity.UserInfo;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

@Component
public class LoginService {

    @Resource
    private UserDao userDao;

    @Resource
    private RoleDao roleDao;

    @Resource
    private MenuDao menuDao;



    //登录
    public UserInfo getUserByLogin(String loginName){
        //获取用户信息
        UserInfo userInfo = userDao.findByLonginName(loginName);

        if (userInfo!=null){
            //获取用户的角色信息
            RoleInfo roleInfoByUserId = roleDao.forRoleInfoByUserId(userInfo.getId());
            //设置用户的角色信息
            userInfo.setRoleInfo(roleInfoByUserId);

            if (roleInfoByUserId!=null){
                //获取用户的权限信息
                List<MenuInfo> firstMenuInfo = menuDao.getAllMenuByRoleId(roleInfoByUserId.getId());

                List<MenuInfo> parList=new ArrayList<>();

                for (MenuInfo allMenu:firstMenuInfo) {
                    if (allMenu.getLeval()==1) {
                        parList.add(allMenu);
                    }
                }

                //递归的查询子菜单权限
                Map<String,String> authmap=new Hashtable<>();

                this.getForMenuInfo(parList,authmap,firstMenuInfo);


                System.out.println(authmap);
                //设置菜单的子权限
                userInfo.setAuthmap(authmap);
                userInfo.setListMenuInfo(parList);
            }
        }
        return userInfo;
    }


    /**
     * 获取子权限的递归方法
     * @param
     * @return
     */
    public void getForMenuInfo(List<MenuInfo> list,Map<String,String> map,List<MenuInfo> allMenus){
        for (MenuInfo menuInfo : list) {   //menuInfo 是 menu表里每一条数据

            if(menuInfo.getLeval() == 4){  //把leval为4的存在map里
                map.put(menuInfo.getUrl(),"");
            }

            List<MenuInfo> chilMenus = new ArrayList<>();

            for (MenuInfo allMenu : allMenus) {
                if(menuInfo.getId() == allMenu.getParentId()){
                    chilMenus.add(allMenu);
                }
            }
            menuInfo.setMenuInfoList(chilMenus);
            chilMenus = new ArrayList<>();

            if(menuInfo.getMenuInfoList().size() > 0){
                getForMenuInfo(menuInfo.getMenuInfoList(),map,allMenus);
            }
        }

    }

    //根据id查询
    public Map<String,Object> getUserById(String id){
        Map<String, Object> userById = userDao.getUserById(id);
        return userById;
    }


}
