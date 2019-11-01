package com.eugene;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;

import javax.servlet.ServletException;

public class MySpringbootApplication {

    public static void run() {
        try {
            Tomcat tomcat = new Tomcat();
            tomcat.setPort(7890);
            // 设置tomcat访问上下文路径, 以及服务器资源路径
            tomcat.addWebapp("/", "d://eugene");
            tomcat.start();
            tomcat.getServer().await();
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (LifecycleException e) {
            e.printStackTrace();
        }

    }
}
