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
  * spring mvc的启动入口就是在DispatchServlet类, 那么这个类到底做了什么呢
    ```text
        1. 以java config的方法启动mvc顺序
          1.1 入口: 创建DispatchServlet实例做的事情
              DispatcherServlet servlet = new DispatcherServlet(webApplicationContext);
              
              >调用GenericServlet的无参构造方法 => 没有干啥事
                >调用HttpServlet的无参构造方法 => 没有干啥事           => 这里调用无参构造方法原因: 因为他们没有带参构造方法, 所以使用默认的构造方法(无参构造方法)
                  >调用HttpServletBean的无参构造方法 => 没有做啥事
                    >调用FrameworkServlet有参构造方法 => 在此处维护了一个webApplicationContext, 它就是spring web应用的上下文
                      >调用DispatcherServlet有参构造方法 => 标识了可以分发options请求
          1.2 标识dispatchServlet的LoadOnStartup为1 => 表示启动tomcat后会首先加载DispatchServlet的init方法
          1.3 执行DispatchServlet的init方法,
              >自己没有重写init方法, 进而找父类的init方法, 发现HttpServletBean父类才有init方法, 进而执行它
                >调用initServletBean方法, 但此时的当前对象是DispatchServlet，所以它又先看自己有没有initServletBean方法, 
                 发现自己没有, 但父类FrameworkServlet有, 则调用FrameworkServlet的initServletBean方法, 在此处调用了initWebApplicationContext方法,
                   >同理, 自己没有initWebApplicationContext方法，父类FrameworkServlet才有, 即调用父类FrameworkServlet的initWebApplicationContext方法
                     >在initWebApplicationContext方法中通过传入的webApplicationContext执行了refresh方法, 他在refresh方法中对bean工厂进行了初始化 
                      这与AnnotationConfigApplicationContext上下文bean工厂的初始化顺序不一样, 一个是在父类默认构造方法初始化另一个在refresh中初始化
                        >进入invokeBeanFactoryPostProcessors方法开始处理后置处理器, 扫描包
                          >在执行loadBeanDefinitions方法之前, 只有一个AppConfig类和一些扫描出来的Controller以及最重要的DelegatingWebMvcConfiguration类
                           此类的作用会将spring mvc的一些bean全部注册到bean工厂, 因为在它的父类WebMvcConfigurationSupport中有大量的@Bean注解(18个),
                           org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping
                           org.springframework.util.PathMatcher
                           org.springframework.web.util.UrlPathHelper
                           org.springframework.web.accept.ContentNegotiationManager
                           org.springframework.web.servlet.handler.AbstractHandlerMapping  =>  viewControllerHandlerMapping
                           org.springframework.web.servlet.handler.BeanNameUrlHandlerMapping
                           org.springframework.web.servlet.handler.AbstractHandlerMapping => resourceHandlerMapping
                           org.springframework.web.servlet.resource.ResourceUrlProvider
                           org.springframework.web.servlet.HandlerMapping => defaultServletHandlerMapping
                           org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter
                           org.springframework.format.support.FormattingConversionService
                           org.springframework.validation.Validator
                           org.springframework.web.method.support.CompositeUriComponentsContributor
                           org.springframework.web.servlet.mvc.HttpRequestHandlerAdapter
                           org.springframework.web.servlet.mvc.SimpleControllerHandlerAdapter
                           org.springframework.web.servlet.HandlerExceptionResolver
                           org.springframework.web.servlet.ViewResolver
                           org.springframework.web.servlet.handler.HandlerMappingIntrospector
                           最终在实例化bean时通过一些扩展点，比如InitializingBean、BeanPostProcessor扩展点来调用bean的业务逻辑
        2. http请求入口, 
           统一进入DispatchServlet的service方法中, 由request的一些信息和spring bean扫描得到的request mapping来决定调用具体的方法
    ```