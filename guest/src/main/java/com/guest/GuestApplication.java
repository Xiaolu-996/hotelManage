package com.guest;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@EnableTransactionManagement(proxyTargetClass = true)
@SpringBootApplication
@MapperScan("com.guest.mapper")
public class GuestApplication {
    @PostConstruct
    void started() {//设置时区
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Shanghai"));
    }
    public static void main(String[] args) {
        SpringApplication.run(GuestApplication.class, args);
    }

}
