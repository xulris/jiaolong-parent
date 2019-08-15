package com.sso.web;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sso.service.LoginService;
import com.zbf.jwt.JWTUtils;
import com.zbf.pojo.ResponseResult;
import com.zbf.pojo.entity.UserInfo;
import com.zbf.randm.VerifyCodeUtils;
import com.zbf.utils.MD5;
import com.zbf.utils.UID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.security.auth.login.LoginException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Controller
public class AuthController {

    @Autowired
    private LoginService loginService;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;


    /**
     * 登录操作
     */
    @ResponseBody
    @RequestMapping("login")
    public ResponseResult toLogin(@RequestBody Map<String,Object> map) throws LoginException {

        ResponseResult responseResult = ResponseResult.getResponseResult();
        //进行用户密码的校验
        if (map!=null && map.get("loginname")!=null){
            //根据用户名获取用户信息
            UserInfo user = loginService.getUserByLogin(map.get("loginname").toString());
            if (user!=null){
                //对比密码
                String password = MD5.encryptPassword(map.get("password").toString(), "lcg");

                if (user.getPassword().equals(password)){

                    //将用户信息转存为JSON串
                    String userinfo = JSON.toJSONString(user);

                    //将用户信息使用JWT进行加密，将加密信息作为票据
                    String token = JWTUtils.generateToken(userinfo);

                    //将加密信息存入statuInfo
                    responseResult.setToken(token);

                    //将生成的token存储到redis库
                    redisTemplate.opsForValue().set("USERINFO"+user.getId().toString(),token);

                    //将该用户的数据访问权限信息存入缓存中
                   redisTemplate.opsForHash().putAll("USERDATAAUTH"+user.getId().toString(), user.getAuthmap());

                    //设置token过期 30分钟Z
                    redisTemplate.expire("USERINFO"+user.getId().toString(),600, TimeUnit.SECONDS);
                    //设置返回值
                    responseResult.setResult(user);
                    responseResult.setCode(200);
                    //设置成功信息
                    responseResult.setSuccess("登录成功    …_…");
                    System.out.println("登录成功"+responseResult.getResult());
                    System.out.println(responseResult);
                    return responseResult;
                }else {
                    throw new LoginException("用户名或密码错误1");
                }
            }else {
               throw new LoginException("用户名或密码错误2");
            }
        }else {
           throw new LoginException("用户名或密码错误3");
        }
    }


    /**
     * 检测token 是否超时 ，检查token 是否还可用
     */
    /*public ResponseResult toCheckLogin(HttpServletRequest request, HttpServletResponse response){

    }*/

    /**
     * 获取滑动验证的验证码
     */
    @RequestMapping("getCode")
    @ResponseBody
    public ResponseResult getCode(HttpServletRequest request,HttpServletResponse response){
        System.out.println("！！！！getCode方法");
        Cookie[] cookies = request.getCookies();
        //生成一个长度为5 的随机字符串
        String code = VerifyCodeUtils.generateVerifyCode(5);
        ResponseResult responseResult = ResponseResult.getResponseResult();
        System.out.println(code+"------获取code验证码值----");
        responseResult.setResult(code);
        String uidCode="CODE"+ UID.getUUID16();
        //将生成的随机字符串标识后存入redis
        redisTemplate.opsForValue().set(uidCode,code);
        //设置过期时间
        redisTemplate.expire(uidCode,1,TimeUnit.MINUTES);
        //回写cookie
        Cookie cookie = new Cookie("authcode", uidCode);
        cookie.setPath("/");
        cookie.setDomain("localhost");
        response.addCookie(cookie);

        return responseResult;
    }

    /**
     * 手动加载密码
     */
    public static void main(String[] args) {
        System.out.println(MD5.encryptPassword("123456","lcg"));

        JSONObject jsonObject = JWTUtils.decodeJwtTocken("eyJhbGciOiJIUzUxMiJ9.eyJjcmVhdGVkIjoxNTY0OTEzMzIzMzU4LCJleHAiOjE1NjQ5MTMzODMsInVzZXJpbmZvIjoie1wiaWRcIjpcIjY0NTU2NDY1NFwifSJ9.iT-NmNBkbjK29t4DLtyJvsAwp770QyYkUpEGB-Lmy-xDVH2NWUtPqQJmovV7PZV46IGPVUMvYMOAaEhbJ6voaA");

        System.out.println(jsonObject.get("id"));
    }


    //退出登录
    @RequestMapping("loginout")
    @ResponseBody
    public ResponseResult loginout(@RequestBody Map<String,Object> map){
        //   Claim claims= (Claim) request.getAttribute("userinfo");
                //清除用户信息
        redisTemplate.delete("USERINFO"+map.get("id").toString());
        ResponseResult responseResult=ResponseResult.getResponseResult();
        responseResult.setSuccess("ok");
        responseResult.setCode(200);
        return responseResult;
    }

    //根据ID查询用户信息
    @RequestMapping("getUserById")
    public ResponseResult getUserById(@RequestBody Map<String,Object> map){
        ResponseResult result = ResponseResult.getResponseResult();
        System.out.println(map.get("userid")+"========");
        if (map.get("userid")!=null){
            Map<String, Object> userid = loginService.getUserById(map.get("userid").toString());
            result.setResult(userid);
        }
        return result;
    }

}
