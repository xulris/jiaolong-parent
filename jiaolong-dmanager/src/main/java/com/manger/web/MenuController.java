package com.manger.web;

import com.manger.service.MenuService;
import com.manger.service.RoleService;
import com.manger.service.UserService;
import com.zbf.pojo.ResponseResult;
import com.zbf.pojo.entity.MenuInfo;
import com.zbf.pojo.entity.RoleInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
public class MenuController {

    @Autowired
    private MenuService menuService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private UserService userService;

    @RequestMapping("getMenuList")
    public ResponseResult getMenuList(@RequestBody Map<String,Object> map){
        ResponseResult responseResult = ResponseResult.getResponseResult();
        Map<String,Object> map1 = new HashMap<>();
        if(map!=null && map.get("userId")!=null && map.get("roleId")!=null){
            List<MenuInfo> menuList = userService.getBrotherMenusByUrl("getMenuList",map.get("roleId").toString());
            map1.put("currentPageMenus",menuList);
        }else{
            System.out.println("map!=null && map.get(\"userId\")!=null");
            responseResult.setCode(300);
            responseResult.setError("获取列表错误");
            return responseResult;
        }
        List<MenuInfo> allMenu = roleService.getAllMenu();
        map1.put("allMenu",allMenu);
        responseResult.setCode(200);
        responseResult.setResult(map1);
        return responseResult;
    }

    @RequestMapping("addMenu")
    public ResponseResult addMenu(@RequestBody MenuInfo menuInfo){

        ResponseResult responseResult = ResponseResult.getResponseResult();
        if(menuInfo != null){
            int i = menuService.checkedMenuName(menuInfo.getMenuName());
            if(i > 0){
                responseResult.setCode(300);
                responseResult.setError("菜单名已存在");
                return responseResult;
            }
            menuService.save(menuInfo);
            responseResult.setCode(200);
            responseResult.setSuccess("新增成功");
            responseResult.setResult(menuInfo);

        }else{
            responseResult.setError("新增失败");
        }
        return responseResult;
    }

    @RequestMapping("updateMenuById")
    public ResponseResult updateMenu(@RequestBody MenuInfo menuInfo){
        System.out.println("updatemenuid:::"+menuInfo.getId());
        ResponseResult responseResult = ResponseResult.getResponseResult();
        if(menuInfo != null){
            int i = menuService.checkedMenuName(menuInfo.getMenuName());
            if(i > 1){
                responseResult.setCode(300);
                responseResult.setError("菜单名已存在");
                return responseResult;
            }
            menuService.save(menuInfo);
            responseResult.setCode(200);
            responseResult.setSuccess("修改成功");
            responseResult.setResult(menuInfo);

        }else{
            responseResult.setError("修改失败");
        }
        return responseResult;
    }

    @RequestMapping("deleteMenuById")
    public ResponseResult deleteMenuById(@RequestBody Map<String,Object> map){
        ResponseResult responseResult = ResponseResult.getResponseResult();

        System.out.println(map.get("ids").toString());
        if(map != null && map.get("ids") != null && "1".equals(map.get("flag").toString())){
            String[] ids = map.get("ids").toString().split(",");
            Set<RoleInfo> roleByMenuId = menuService.getRoleByMenuId(ids);
            if(roleByMenuId != null && roleByMenuId.size() > 0){
                for (RoleInfo roleInfo : roleByMenuId) {
                    System.out.println("roleByMenuId///////"+roleInfo);
                }
                responseResult.setCode(300);
                responseResult.setError("此权限正绑定用户");
                responseResult.setResult(roleByMenuId);
            }else{
                menuService.deleteMenuById(ids);
                responseResult.setCode(200);
                responseResult.setSuccess("删除成功");
            }
        }else if(map != null && map.get("ids") != null && "0".equals(map.get("flag").toString())){
            String[] ids = map.get("ids").toString().split(",");
            menuService.deleteMenuById(ids);
            responseResult.setCode(200);
            responseResult.setSuccess("删除成功");
        }
        return responseResult;
    }




}
