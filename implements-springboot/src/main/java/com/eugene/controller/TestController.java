package com.eugene.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestController {

    @GetMapping("/test.do")
    public void test() {
        System.out.println("test");
    }
}
