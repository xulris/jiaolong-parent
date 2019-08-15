package com.gateway;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/*
* GateWay启动
* */

@SpringBootApplication
@RestController
public class TestGateWayServer {

    public static void main(String[] args) {
        SpringApplication.run(TestGateWayServer.class);
    }

    @RequestMapping("serverhealth")
    public String serverhealth(){
        System.out.println("--GateWay serverhealth is ok--");
        return "ok";
    }
}
