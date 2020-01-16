# 模拟实现springboot

## 一、spring mvc的启动方式

### 1.1 xml配置方法
  * 要使用xml, 我内心是拒绝的

### 1.2 java config方式
  * 官网推荐方法

    1. 依赖相关jar包
       
        * tomcat 8.0.x版本的jar包, 千万别依赖8.5以上版本的, 不然会失败
        * spring-context: spring上下文包
        * spring-mvc: mvc功能包
        * spring-webmvc: web mvc包
    2. 添加自定义类实现WebApplicationInitializer接口, 并在其中引入DispatchServlet
        ```java
           public class MyWebApplicationInitializer implements WebApplicationInitializer {
           
               @Override
               public void onStartup(ServletContext servletCxt) {
           
                   // Load Spring web application configuration
                   AnnotationConfigWebApplicationContext ac = new AnnotationConfigWebApplicationContext();
                   ac.register(AppConfig.class);
                   ac.refresh();
           
                   // Create and register the DispatcherServlet
                   DispatcherServlet servlet = new DispatcherServlet(ac);
                   ServletRegistration.Dynamic registration = servletCxt.addServlet("app", servlet);
                   registration.setLoadOnStartup(1);
                   registration.addMapping("*.do");
               }
           }
        ```
    3. 在main方法中启动tomcat, 代码如下
        ```java
           Tomcat tomcat = new Tomcat();
           tomcat.setPort(7890);
           // 设置tomcat访问上下文路径, 以及服务器资源路径
           tomcat.setWebApp("/", "d://");    
           tomcat.start();
           tomcat.getServer().await();
        ```
    4. 在d盘放入一个txt文件并访问它, 即完成了以java config的方式启动一个web项目, 其中也可以编写Controller来请求它

    5. 存在的问题
    
        *. 启动时会报错:  java.lang.ClassNotFoundException: org.apache.jasper.servlet.JspServlet
           原因: 因为tomcat是以addWebApp的方式启动, 所以它会以一个web应用来启动, 它会去加载jsp相关的jar
           包, 项目中没依赖, 所以报错. 但，这并不影响项目运行
        
        *. 当使用@ResponseBody注解时, 若返回一个list, 则页面会报
           No converter found for return value of type: class java.util.ArrayList
           的错误。
           原因: 因为spring中没有内置针对list的消息转换器
           
  * 解决官网推荐做法存在的问题
    1. 针对扫描jsp相关jar包找不到类的问题, 我们可以不让tomcat调用setWebApp的api来启动tomcat, 可以使用
       addContext的方式来启动tomcat, 但是这种方式的tomcat启动不会去加载相关的servlet容器, 所以我们要
       修改引用DispatchServlet的入口, 不在MyWebApplicationInitializer中引入DispatchServlet，在启动
       tomcat处利用tomcat的addServlet的方式来添加servlet
        ```java
           Tomcat tomcat = new Tomcat();
           tomcat.setPort(7890);
           // 设置tomcat访问上下文路径, 以及服务器资源路径
           tomcat.addContext("/", "d://");

           // Load Spring web application configuration
           AnnotationConfigWebApplicationContext ac = new AnnotationConfigWebApplicationContext();
           ac.register(AppConfig.class);
    
           // 此处不刷新, 刷新的入口将放入DispatcherServlet类中
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
        ```
    2. 针对无list转换器的问题:
       添加一个转换器即可, 此时我们需要借助@EnableWebMvc注解, 来手动添加一个消息转换器
       同时我们还需要依赖fastjson的jar包
        ```java
           @Component
           public class MyWebMvcConfigurer implements WebMvcConfigurer {
               @Override
               public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
                   System.out.println("load FastJsonHttpMessageConverter----------------------------");
                   FastJsonHttpMessageConverter fastJsonHttpMessageConverter = new FastJsonHttpMessageConverter();
                   converters.add(fastJsonHttpMessageConverter);
               }
           }
        ```
       

## 二、DispatchServlet到底做了什么事
DispatchServlet, 要调用到它有两种方式:
1. 使用tomcat的addWebApp的方式添加上下文路径, 这样我们就可以和官网一样自定义实现WebApplicationInitializer的类
   来启动web应用, 最终将DispatchServlet添加至Servlet上下文中, 进而从DispatchServlet入口开始启动spring mvc

2. 使用tomcat的addContext方式启动, 但此时tomcat不会去调用servlet容器, 所以我们还要添加@EnableWebMvc注解,
   但是呢@EnableWebMvc注解的执行会依赖于一个ServletContext上下文对象, 所以我们得注册一个ServletContext的bean
   到spring, 这种方式可能行得通(没试过)。 但有一个更高级的方式, 就是将spring上下文传入DispatcherServlet类中,
   在DispatchServlet中对spring上下文进行refresh操作和实例化ServletContext上下文对象