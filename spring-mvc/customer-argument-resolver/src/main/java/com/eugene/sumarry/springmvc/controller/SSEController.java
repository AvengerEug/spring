package com.eugene.sumarry.springmvc.controller;

import com.alibaba.fastjson.JSONObject;
import com.eugene.sumarry.springmvc.utils.CurrentUserId;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/sse")
public class SSEController {

    @GetMapping("/index")
    @ResponseBody
    public SseEmitter index(@RequestParam(value = "test") String test) {
        SseEmitter emitter = new SseEmitter();

        // 在新线程中发送事件
        new Thread(() -> {
            try {
                for (int i = 0; i < 5; i++) {
                    Map map = new HashMap();
                    map.put(test + i, i);
                    emitter.send(SseEmitter.event()
                            .id("muyang-sse")
                            .name("muyang")
                            .data(JSONObject.toJSONString(map), MediaType.APPLICATION_JSON_UTF8)
                    ); // 发送数据
                    Thread.sleep(1000); // 每秒发送一次
                }
                emitter.complete(); // 完成发送
            } catch (Exception e) {
                emitter.completeWithError(e); // 发送错误
            }
        }).start();

        return emitter;
    }

    @GetMapping("/index3")
    @ResponseBody
    public String index3() {
        return "index3";
    }
}
