package com.manger;

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
public class MangerServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MangerServiceApplication.class,args);
    }

    //健康检查
    @RequestMapping("serverhealth")
    public String serverhealth(){
        System.out.println("=========健康检查 OK====");
        return "ok";
    }
}
