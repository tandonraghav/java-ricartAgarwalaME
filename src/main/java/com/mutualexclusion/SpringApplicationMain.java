package com.mutualexclusion;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.*")
public class SpringApplicationMain {

    public static void main(String[] args) {
        SpringApplication.run(SpringApplicationMain.class,args);
    }
}
