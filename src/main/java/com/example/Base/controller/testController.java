package com.example.Base.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@Slf4j
@PropertySource("classpath:application.properties")
public class testController {
    @Value("${secretkey}")
    private String secretkey;


    @PostMapping("/logintest")
    public String aaa(@RequestBody String email, String password){
        String c = email + password;
        return "hello";
    }
}
