package com.manger.web;


import com.manger.service.UserService;
import com.zbf.pojo.ResponseResult;
import com.zbf.pojo.entity.MenuInfo;
import com.zbf.pojo.entity.RoleInfo;
import com.zbf.pojo.entity.UserInfo;
import com.zbf.utils.MD5;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
public class UserController {

    @Autowired
    UserService userService;

    //分页模糊
    @RequestMapping("getUserList")
    @ResponseBody
    public ResponseResult findUserAll(@RequestBody Map<String,Object> map){
        ResponseResult responseResult = ResponseResult.getResponseResult();
        Page<UserInfo> page = userService.findUserAll(map);//查询出user表
        for (UserInfo userInfo:page){
            System.out.println(userInfo);
        }
        MenuInfo menuInfo = userService.findMenuById(3L);//查出menu表里3L这条信息
        List<RoleInfo> list = userService.findAllRole();//查询role表
       Map<String,Object> map1= new HashMap<>();
       map1.put("page",page);
       map1.put("menuInfo",menuInfo);
       map1.put("roleList",list);
       responseResult.setResult(map1);
        System.out.println(responseResult.getResult()+"qqq");
        return responseResult;
    }

    //添加
    @RequestMapping("addUser")
    @ResponseBody
    public ResponseResult addUser(@RequestBody UserInfo userInfo) {

        ResponseResult responseResult = ResponseResult.getResponseResult();
        //获取要添加的登录名
        String name = userInfo.getLoginName();
        System.out.println("用户名为：" + name);
        //判断数据库中是否有这个登录名
        if (userInfo != null && name != null && name != "") {
            //判断账号是否重复
            boolean b = userService.AddLoginName(name);
            if (b) {
                userInfo.setPassword(MD5.encryptPassword(userInfo.getPassword(), "lcg"));
                userService.addUser(userInfo);
                responseResult.setCode(200);
                responseResult.setSuccess("新增成功");
            }
        } else {
            System.out.println("此账号已注册");
            responseResult.setError("此账号已注册");
        }
        return responseResult;
    }

    //删除
    @RequestMapping("delUserById")
    @ResponseBody
    public ResponseResult delUserById(@RequestBody Map<String,Object> map){
        ResponseResult responseResult = ResponseResult.getResponseResult();
        System.out.println(map.get("id").toString());
        if (map!=null&&map.get("id")!=null){
           userService.delUserById(Long.valueOf(map.get("id").toString()));
           responseResult.setCode(200);
           responseResult.setSuccess("删除成功");
        }else {
            responseResult.setSuccess("删除失败");
        }
        return responseResult;
    }

    //修改
    @RequestMapping("updateUserById")
    @ResponseBody
    public ResponseResult updateUserById(@RequestBody UserInfo userInfo){
        ResponseResult responseResult = ResponseResult.getResponseResult();

        if(userInfo != null){
            userService.addUser(userInfo);
            responseResult.setCode(200);
            responseResult.setSuccess("修改成功");
        }else{
            responseResult.setError("修改失败");
        }
        return responseResult;
    }

    //绑定角色
    @RequestMapping("putRoleByUserId")
    @ResponseBody
    public ResponseResult putRoleByUserId(@RequestBody Map<String,Object> map){
        ResponseResult responseResult = ResponseResult.getResponseResult();

        if(map != null){

            userService.putRoleUserId(Long.parseLong(map.get("userId").toString()),Long.parseLong(map.get("roleId").toString()));

            responseResult.setCode(200);
            responseResult.setSuccess("绑定成功");
        }else{
            responseResult.setError("绑定失败");
        }
        return responseResult;
    }


    //上传图片
    @RequestMapping("uploadImg")
    @ResponseBody
    public void addImg(@Param("file") MultipartFile file) throws IOException {
        String filePath= "F://ruanjian//nginx//IMAGE//" + file.getOriginalFilename();
        File file1 = new File(filePath);
        file.transferTo(file1);
        //可自定义大小    实现缩略图功能
        Thumbnails.of(filePath).scale(0.25f).toFile(file1.getAbsolutePath() + "_25.jpg");
    }


}
