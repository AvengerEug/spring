package com.eugene.sumarry.springmvc;

import com.eugene.sumarry.springmvc.config.AppConfig;
import org.apache.catalina.Wrapper;
import org.apache.catalina.startup.Tomcat;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

public class Application {

    public static void main(String[] args) throws Exception {
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(7890);
        tomcat.addContext("/", "D://eugene");

        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        context.register(AppConfig.class);

        DispatcherServlet dispatcherServlet = new DispatcherServlet(context);

        Wrapper wrapper = tomcat.addServlet("/", "dispatchServlet", dispatcherServlet);
        wrapper.addMapping("/");
        wrapper.setLoadOnStartup(1);

        tomcat.start();
        tomcat.getServer().await();
    }
}
