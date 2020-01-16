package com.eugene;

import org.springframework.web.WebApplicationInitializer;

import javax.servlet.ServletContext;

/**
 * 若tomcat是使用
 * tomcat.addWebApp的方式启动的, 那么就会走servlet,
 * 最终会回调这个类的onStartup方法
 *
 * 若使用的是tomcat.addContext的方式, 那么就不会走servlet,
 * 最终不会回调这个类的onStartup方法
 */
public class MyWebApplicationInitializer implements WebApplicationInitializer {

    /**
     * 为了解决tomcat启动时不加载jsp相关的jar包, 所以我们以addContext的方式启动tomcat
     * 但是这种方式会导致项目不会以web项目启动, 导致此类不会被回调,
     *
     * 要解决这个问题, 我们需要在启动tomcat的时候, 使用tomcat的api来将dispatchServlet
     * 添加到tomcat中, 所以此处加载spring上下文和DispatchServlet的代码将移至到启动tomcat处
     * 即MySpringbootApplication.java中
     *
     */
    @Override
    public void onStartup(ServletContext servletCxt) {

        System.out.println("=====================================start=====================================");
        // Load Spring web application configuration
       /* AnnotationConfigWebApplicationContext ac = new AnnotationConfigWebApplicationContext();
        ac.register(AppConfig.class);
        ac.refresh();

        // Create and register the DispatcherServlet
        DispatcherServlet servlet = new DispatcherServlet(ac);
        ServletRegistration.Dynamic registration = servletCxt.addServlet("app", servlet);
        registration.setLoadOnStartup(1);
        // 添加url mapping映射, 相当于web.xml中的dispatchServlet的request mapping
        registration.addMapping("*.do");*/
    }

}

