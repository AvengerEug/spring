package com.eugene;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.Wrapper;
import org.apache.catalina.startup.Tomcat;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

public class MySpringbootApplication {

    /**
     * 以这种方式启动,
     * 若在浏览器中访问: http://localhost:7890/test-string.do
     *   它能正常在页面中渲染testString内容
     *
     * 若在浏览器中访问: http://localhost:7890/test-map.do
     *   那么会在浏览器中抛出异常:
     *   org.springframework.http.converter.HttpMessageNotWritableException:
     *   No converter found for return value of type: class java.util.HashMap
     *
     *   大致的原因就是没有针对HashMap类型的消息转换器, 此时我们要添加消息转换器
     */
    public static void run() {
        try {
            Tomcat tomcat = new Tomcat();
            tomcat.setPort(7890);
            /**
             * 以tomcat.addWebapp的方式添加应用, 在启动的时候会报错
             *   java.lang.ClassNotFoundException: org.apache.jasper.servlet.JspServlet
             *
             * 可以采用tomcat.addContext的方式解决这个问题
             * 但是呢, 采用这种方式的话, tomcat将不会以启动web应用的方式来启动这个项目, 即我们自己写的实现了
             * WebApplicationInitializer接口的MyWebApplicationInitializer类的onStartup方法不会被执行,
             * 因为没有执行到servlet, 不会回调到WebApplicationInitializer类
             * 为了解决tomcat.addContext方式带来的问题
             *
             * 所以我们要将DispatchServlet也放到这来, 使用tomcat来添加一个servlet
             * 最终代码如下
             */
            // 设置tomcat访问上下文路径, 以及服务器资源路径
            tomcat.addContext("/", "d://");

            // Load Spring web application configuration
            AnnotationConfigWebApplicationContext ac = new AnnotationConfigWebApplicationContext();
            ac.register(AppConfig.class);
            // ac.refresh();

            // Create and register the DispatcherServlet
            DispatcherServlet servlet = new DispatcherServlet(ac);
            Wrapper dispatchServlet = tomcat.addServlet("/", "dispatchServlet", servlet);
            //
            dispatchServlet.setLoadOnStartup(1);
            // 添加url mapping映射, 相当于web.xml中的dispatchServlet的request mapping
            dispatchServlet.addMapping("*.do");

            tomcat.start();
            tomcat.getServer().await();
        } catch (LifecycleException e) {
            e.printStackTrace();
        }

    }
}
