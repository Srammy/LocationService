package com.buaa.locationservice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
@EnableTransactionManagement
//@MapperScan("com.buaa.locationservice.dao")
public class LocationserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(LocationserviceApplication.class, args);
    }

}
