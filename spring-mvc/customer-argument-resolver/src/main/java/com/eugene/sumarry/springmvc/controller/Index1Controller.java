package com.eugene.sumarry.springmvc.controller;

import com.eugene.sumarry.springmvc.utils.CurrentUserId;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/index")
public class Index1Controller {

    @GetMapping
    public String index(@RequestParam(value = "xxx") String xxx, @CurrentUserId Integer x) {
        return "[xxx]: " + xxx + ", [x]: " + x;
    }

    @GetMapping("/index3")
    @ResponseBody
    public String index3() {
        return "index3";
    }
}
