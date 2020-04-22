package com.eugene.sumarry.springmvc.uploadfile;

import org.apache.catalina.startup.Tomcat;
import org.springframework.beans.factory.annotation.Value;

/**
 * 启动项目会报java.lang.ClassNotFoundException: org.apache.jasper.servlet.JspServlet的错
 *
 * 不影响使用, 报这个错只是不能解析jsp文件而已
 */
public class Entry {

    public static void main(String[] args) throws Exception {
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(7890);
        // 设置tomcat访问上下文路径, 以及服务器资源路径
        String classPath = Entry.class.getResource("/").getPath();
        tomcat.addWebapp("/", classPath + "/webapp");
        tomcat.start();
        tomcat.getServer().await();
    }
}
