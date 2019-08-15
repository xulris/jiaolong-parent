package com.manger.web;

import com.manger.service.RoleService;
import com.manger.service.UserService;
import com.zbf.pojo.ResponseResult;
import com.zbf.pojo.entity.MenuInfo;
import com.zbf.pojo.entity.RoleInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class RoleController {

    @Autowired
    RoleService roleService;

    @Autowired
    UserService userService;


    //role列表
    @RequestMapping("getRoleList")
    public ResponseResult getRoleList(@RequestBody Map<String,Object> map){
        ResponseResult responseResult = new ResponseResult();
        Page<RoleInfo> page = roleService.getAllRole(map);//查出role列表数据
        MenuInfo menuInfo = userService.findMenuById(5L);
        Map<String,Object> map1=new HashMap<>();
        List<MenuInfo> allMenu = roleService.getAllMenu();
        map1.put("page",page);
        map1.put("allMenu",allMenu);
        map1.put("menuInfo",menuInfo);
        responseResult.setResult(map1);
        System.out.println("role列表："+responseResult.getResult());
        return responseResult;
    }


    //添加角色
    @RequestMapping("addRole")
    public ResponseResult addRole(@RequestBody RoleInfo roleInfo){
        ResponseResult responseResult =  ResponseResult.getResponseResult();
        if (roleInfo!=null){
            roleService.addRole(roleInfo);
            responseResult.setCode(200);
            responseResult.setSuccess("添加成功");
        }else {
            responseResult.setSuccess("添加失败");
        }
        return responseResult;
    }


    //修改
    @RequestMapping("upRole")
    public ResponseResult upRoleById(@RequestBody RoleInfo roleInfo){
        ResponseResult responseResult = new ResponseResult();
        if (roleInfo!=null){
            responseResult.setCode(200);
            responseResult.setSuccess("修改成功");
        }else {
            responseResult.setSuccess("修改失败");
        }
        return responseResult;
    }



    //删除
    @RequestMapping("deleteRoleById")
    public ResponseResult deleteRoleById(@RequestBody Map<String,Object> map){
        ResponseResult responseResult = ResponseResult.getResponseResult();

        System.out.println("删除的id："+map.get("id").toString());
        if(map != null && map.get("id") != null){
            roleService.delRoleById(Long.parseLong(map.get("id").toString()));
            responseResult.setCode(200);
            responseResult.setSuccess("删除成功");
        }else{
            responseResult.setError("删除失败");
        }
        return responseResult;
    }


    @RequestMapping("putMenuByRoleId")
    public ResponseResult putMenuByRoleId(@RequestBody Map<String,Object> map){

        System.out.println("---!!进入putmenubyroleid方法---！！");
        ResponseResult responseResult = ResponseResult.getResponseResult();

        if(map != null){
            roleService.putMenuByRoleId(map);
            responseResult.setCode(200);
            responseResult.setSuccess("保存成功");
        }else{
            responseResult.setError("保存失败");
        }
        return responseResult;
    }


}
