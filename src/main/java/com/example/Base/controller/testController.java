package com.example.Base.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class testController {

    @PostMapping("/logintest")
    public String aaa(@RequestBody String email, String password){
        String c = email + password;
        return "hello";
    }
}
