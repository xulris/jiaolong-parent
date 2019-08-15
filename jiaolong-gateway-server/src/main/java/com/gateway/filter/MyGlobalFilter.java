package com.gateway.filter;


import com.alibaba.fastjson.JSONObject;
import com.zbf.jwt.JWTUtils;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
/*
* 全局过滤器
* */
@Component
public class MyGlobalFilter implements GlobalFilter {

    @Value("${my.auth.urls}")
    private String[] urls;

    @Value("${my.auth.loginPath}")
    private String loginpage;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        String string = request.getURI().toString();

        String replace = string.replace("http://localhost:10001/", "");
        System.out.println("本次请求的地址："+replace);

        List<String> strings = Arrays.asList(urls);
        if (strings.contains(string)){
            return chain.filter(exchange);
        }else {
            List<String> token = request.getHeaders().get("token");
            JSONObject jsonObject=null;
            try {
                jsonObject= JWTUtils.decodeJwtTocken(token.get(0));
                String s=JWTUtils.generateToken(jsonObject.toJSONString());
                response.getHeaders().set("token",s);
            }catch (JwtException e){
                e.printStackTrace();
                response.getHeaders().set("Location",loginpage);
                response.setStatusCode(HttpStatus.SEE_OTHER);
                return exchange.getResponse().setComplete();
            }
            string=string.substring(string.indexOf("/"));

            String id = jsonObject.get("id").toString();
            System.out.println(jsonObject.get("userName"));
            Boolean aBoolean = redisTemplate.opsForHash().hasKey("USERDATAAUTH" + id,replace);
            if (aBoolean){
                return chain.filter(exchange);
            }else {
                throw new RuntimeException("不能访问该资源！");
            }
        }
    }
}
