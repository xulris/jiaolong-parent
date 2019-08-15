package com.zbf.pojo;

import lombok.Data;

@Data
public class ResponseResult {


    private int code;
    //错误信息
    private String error;
    //程序返回结果
    private Object result;
    //成功信息
    private String success;
    //创建实例
    public static ResponseResult getResponseResult(){
        return new ResponseResult();
    }
    //登陆成功的标识(这里存储了一些用户的信息)
    private String token;
    //用来表示token的一个唯一的字符串
    private String tokenkey;

    //选中的需要回显的菜单ID
    private Long[] menuIds;


}
