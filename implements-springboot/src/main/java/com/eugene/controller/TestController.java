package com.eugene.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import sun.nio.cs.ext.MacArabic;

import java.util.HashMap;
import java.util.Map;

@Controller
public class TestController {

    @GetMapping("/test-string.do")
    @ResponseBody
    public String testString() {
        return "testString";
    }

    @GetMapping("/test-map.do")
    @ResponseBody
    public Map<String, Object> testMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("key1", "value1");

        return map;
    }

}
