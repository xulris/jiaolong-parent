package com.sso;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableJpaAuditing
@RestController
@EntityScan(basePackages = {"com.zbf.pojo.**"})
public class SsoServer {
    public static void main(String[] args) {
        SpringApplication.run(SsoServer.class,args);
    }

    @RequestMapping("health")
    public String health(){
        System.out.println("======SSO-SERVER OK!=====");
        return "ok";
    }
}
