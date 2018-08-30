package com.tc3.sp_mvc.tweet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TweetApplication {

    public static void main(String[] args) {
        System.setProperty("java.net.useSystemProxies", "true");//使用系统代理
        SpringApplication.run(TweetApplication.class, args);
    }
}
