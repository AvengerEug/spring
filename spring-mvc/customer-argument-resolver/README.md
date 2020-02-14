实现自定义参数解析器

## 一、实现功能
  * 对Controller的参数添加`@CurrentUserId`注解时, 将值`111`添加方法参数中
  
  * eg: 
    
    ```java
    @GetMapping(/index)
    public String index(@RequestParam(value = "xxx") String xxx, @CurrentUserId Integer x) {
        return "[xxx]: " + xxx + ", [x]: " + x;
    }
    ```
    
    当在浏览器请求至此api时，x的值会被填充成`111`

## 二、测试案例

* 浏览器输入: `http://localhost:7890/index?xxx=123`
* 浏览器响应: `[xxx]: 123, [x]: 111`

## 三、实现步骤和遇到的坑

* 背景

  ```markdown
  根据SpringMvc的启动原理和一个请求到获取handler和adapter原理，最终在反射调用Controller方法时的填充参数步骤时，看到了springmvc中有许多内置的`HandlerMethodArgumentResolver`(存放在HandlerMethodArgumentResolverComposite类的argumentResolvers属性中), 其中包含处理@RequestParam注解的`RequestParamMethodArgumentResolver`参数解析器。出于好奇心，自己实现了一个参数解析器: `CurrentUserArgumentResolver`, 具体代码如下: 
  ```

  ```java
  public class CurrentUserArgumentResolver implements HandlerMethodArgumentResolver {
  
      @Override
      public boolean supportsParameter(MethodParameter parameter) {
          return parameter.hasParameterAnnotation(CurrentUserId.class);
      }
  
      @Override
      public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
          return 111;
      }
  }
  ```
  
* 出现的问题:

  * 我要如何将自定义的参数解析器存放在HandlerMethodArgumentResolverComposite类的argumentResolvers属性中

* 如何解决问题:

   * 要解决上述的问题，我首先得了解argumentResolvers的属性是什么时候被填充的，于是我将HandlerMethodArgumentResolverComposite类所有对argumentResolvers属性有add操作的地方都打了断点。最终发现是在初始化`org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter` bean时对属性进行了填充(因为它实现了**InitializingBean**接口，所以调用了**afterPropertiesSet**方法)。具体代码如下:

   * ```java
     public void afterPropertiesSet() {
         // Do this first, it may add ResponseBody advice beans
         initControllerAdviceCache();
     
         if (this.argumentResolvers == null) {
             List<HandlerMethodArgumentResolver> resolvers = getDefaultArgumentResolvers();
             this.argumentResolvers = new HandlerMethodArgumentResolverComposite().addResolvers(resolvers);
         }
         if (this.initBinderArgumentResolvers == null) {
             List<HandlerMethodArgumentResolver> resolvers = getDefaultInitBinderArgumentResolvers();
             // 此处，初始化了HandlerMethodArgumentResolverComposite类，并填充了内部的argumentResolvers属性
             this.initBinderArgumentResolvers = new HandlerMethodArgumentResolverComposite().addResolvers(resolvers);
         }
         if (this.returnValueHandlers == null) {
             List<HandlerMethodReturnValueHandler> handlers = getDefaultReturnValueHandlers();
             this.returnValueHandlers = new HandlerMethodReturnValueHandlerComposite().addHandlers(handlers);
         }
     }
     ```

  *  可是问题又来了，HandlerMethodArgumentResolverComposite类argumentResolvers属性的填充都是在spring自带的类中去完成的，我们无法修改`RequestMappingHandlerAdapter`类的**afterPropertiesSet**方法来添加我们自定义的**argumentResolver**。脑阔疼，我要如何添加呢？ 最终在百度和官网的帮助下，得知了要使用@EnableWebMvc注解来添加一些配置。。。[官网链接：https://docs.spring.io/spring/docs/current/spring-framework-reference/web.html#mvc-config-advanced-java](https://docs.spring.io/spring/docs/current/spring-framework-reference/web.html#mvc-config-advanced-java)。 最终改进AppConfig类为如下代码:

     ```java
     @Configuration
     @ComponentScan("com.eugene.sumarry.springmvc")
     @EnableWebMvc
     public class AppConfig implements WebMvcConfigurer {
     
         @Override
         public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
             resolvers.add(new CurrentUserArgumentResolver());
         }
     }
     ```

     即完成了自定义参数解析器的功能。此时的我很好奇，为什么spring做的耦合这么低，一个注解就能获取到spring类的一些对象并操作它们。于是，我还想了解下@EnableWebMvc注解的执行原理

## 四、@EnableWebMvc注解执行原理

* 查看@EnableWebMvc注解源码

  ```java
  @Retention(RetentionPolicy.RUNTIME)
  @Target({ElementType.TYPE})
  @Documented
  @Import({DelegatingWebMvcConfiguration.class})
  public @interface EnableWebMvc {
  }
  ```

  易知，它利用了spring@Import注解的扩展点，导入了一个**DelegatingWebMvcConfiguration** bean, 所以现在需要转换研究的对象。

* **DelegatingWebMvcConfiguration**类源码

  ```java
  @Configuration
  public class DelegatingWebMvcConfiguration extends WebMvcConfigurationSupport {
  
     private final WebMvcConfigurerComposite configurers = new WebMvcConfigurerComposite();
  
     @Autowired(required = false)
     public void setConfigurers(List<WebMvcConfigurer> configurers) {
        if (!CollectionUtils.isEmpty(configurers)) {
           this.configurers.addWebMvcConfigurers(configurers);
        }
     }
  
  
     @Override
     protected void configurePathMatch(PathMatchConfigurer configurer) {
        this.configurers.configurePathMatch(configurer);
     }
  
     @Override
     protected void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        this.configurers.configureContentNegotiation(configurer);
     }
  
     @Override
     protected void configureAsyncSupport(AsyncSupportConfigurer configurer) {
        this.configurers.configureAsyncSupport(configurer);
     }
  
     @Override
     protected void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        this.configurers.configureDefaultServletHandling(configurer);
     }
  
     @Override
     protected void addFormatters(FormatterRegistry registry) {
        this.configurers.addFormatters(registry);
     }
  
     @Override
     protected void addInterceptors(InterceptorRegistry registry) {
        this.configurers.addInterceptors(registry);
     }
  
     @Override
     protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        this.configurers.addResourceHandlers(registry);
     }
  
     @Override
     protected void addCorsMappings(CorsRegistry registry) {
        this.configurers.addCorsMappings(registry);
     }
  
     @Override
     protected void addViewControllers(ViewControllerRegistry registry) {
        this.configurers.addViewControllers(registry);
     }
  
     @Override
     protected void configureViewResolvers(ViewResolverRegistry registry) {
        this.configurers.configureViewResolvers(registry);
     }
  
     @Override
     protected void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        this.configurers.addArgumentResolvers(argumentResolvers);
     }
  
     @Override
     protected void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> returnValueHandlers) {
        this.configurers.addReturnValueHandlers(returnValueHandlers);
     }
  
     @Override
     protected void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        this.configurers.configureMessageConverters(converters);
     }
  
     @Override
     protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        this.configurers.extendMessageConverters(converters);
     }
  
     @Override
     protected void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers) {
        this.configurers.configureHandlerExceptionResolvers(exceptionResolvers);
     }
  
     @Override
     protected void extendHandlerExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers) {
        this.configurers.extendHandlerExceptionResolvers(exceptionResolvers);
     }
  
     @Override
     @Nullable
     protected Validator getValidator() {
        return this.configurers.getValidator();
     }
  
     @Override
     @Nullable
     protected MessageCodesResolver getMessageCodesResolver() {
        return this.configurers.getMessageCodesResolver();
     }
  
  }
  ```

* **DelegatingWebMvcConfiguration**类扩展了WebMvcConfigurationSupport类，内部维护了spring 上下文对象和Servlet上下文对象，且父类维护了20个bean，其中在创建name为requestMappingHandlerAdapter的bean时，里面有一段设置自定义参数解析器的代码. 如下

  ```java
  @Bean
  public RequestMappingHandlerAdapter requestMappingHandlerAdapter() {
      RequestMappingHandlerAdapter adapter = createRequestMappingHandlerAdapter();
      adapter.setContentNegotiationManager(mvcContentNegotiationManager());
      adapter.setMessageConverters(getMessageConverters());
      adapter.setWebBindingInitializer(getConfigurableWebBindingInitializer());
      // 添加了自定义的参数解析器
      adapter.setCustomArgumentResolvers(getArgumentResolvers());
      adapter.setCustomReturnValueHandlers(getReturnValueHandlers());
  
      if (jackson2Present) {
          adapter.setRequestBodyAdvice(Collections.singletonList(new JsonViewRequestBodyAdvice()));
          adapter.setResponseBodyAdvice(Collections.singletonList(new JsonViewResponseBodyAdvice()));
      }
  
      AsyncSupportConfigurer configurer = new AsyncSupportConfigurer();
      configureAsyncSupport(configurer);
      if (configurer.getTaskExecutor() != null) {
          adapter.setTaskExecutor(configurer.getTaskExecutor());
      }
      if (configurer.getTimeout() != null) {
          adapter.setAsyncRequestTimeout(configurer.getTimeout());
      }
      adapter.setCallableInterceptors(configurer.getCallableInterceptors());
      adapter.setDeferredResultInterceptors(configurer.getDeferredResultInterceptors());
  
      return adapter;
  }
  ```

  ​	

* getArgumentResolvers方法

    ```java
    protected final List<HandlerMethodArgumentResolver> getArgumentResolvers() {
        if (this.argumentResolvers == null) {
            this.argumentResolvers = new ArrayList<>();
            addArgumentResolvers(this.argumentResolvers);
        }
        return this.argumentResolvers;
    }
    ```

* addArgumentResolvers方法

    ```java
    // protected, 由子类去执行
    protected void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
    }
    ```

* 子类DelegatingWebMvcConfiguration的addArgumentResolvers方法

    ```java
    protected void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
       this.configurers.addArgumentResolvers(argumentResolvers);
    }
    ```

* 子类DelegatingWebMvcConfiguration中WebMvcConfigurerComposite类型属性的addArgumentResolvers方法

    ```java
    // 传入的argumentResolvers是一个空list，实际上就是存储HandlerMethodArgumentResolver的集合
    // 最终就是定位到this.delegates什么时候被初始化的
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
       for (WebMvcConfigurer delegate : this.delegates) {
          delegate.addArgumentResolvers(argumentResolvers);
       }
    }
    ```

* 子类DelegatingWebMvcConfiguration的setConfigurers方法

    ```java
    @Autowired(required = false)
    public void setConfigurers(List<WebMvcConfigurer> configurers) {
       if (!CollectionUtils.isEmpty(configurers)) {
          this.configurers.addWebMvcConfigurers(configurers);
       }
    }
    ```

    可知，子类DelegatingWebMvcConfiguration中WebMvcConfigurerComposite类型的delegates属性(list)就是在spring创建DelegatingWebMvcConfiguration bean时被自动填充(依赖注入)进去的。其中集合里面的元素就是实现了**WebMvcConfigurer**接口的bean，在此时，里面的元素就是AppConfig这个bean。所以，最终就是将自定义参数解析器存入到了**WebMvcConfigurationSupport**类的argumentResolvers属性中和新增的requestMappingHandlerAdapter中customArgumentResolvers属性。那么使用@EnableWebMvc创建的RequestMappingHandlerAdapter的argumentResolvers属性什么时候被填充的呢？ 跟上面说的一样，在调用创建bean后回调InitializingBea接口中的方法被初始化的。

* 原理解释

  @EnableWebMvc注解就是添加许多的bean(至少20个)，其中包含一些spring默认和自定义的handlerMapping、handlerAdapter等等。最终在DispatcherServlet中的initStrategies方法进行初始化handlerMapping和handlerAdapter时，不是从配置文件中读取了，而是直接从spring bean工厂获取相关的bean(根据type获取bean)了。其中就包含RequestMappingHandlerAdapter(自定义的参数解析器就是存在这个handlerAdapter)。

  需要确认这个自定义参数解析器什么时候加到RequstMappingHandlerAdapter的HandlerMethodArgumentResolverComposite属性的argumentResolvers属性中去的  ==> 在spring 创建bean时调用initializeBean的afterPropertiesSet方法中实例化argumentResolvers属性的。
  
* 添加了@EnableWebMvc注解后，DispatcherServlet中的handlerMapping和handlerAdapter都是从bean工厂获取了，因为这个注解导入了一个类，导入类的父类内部维护了20个bean。