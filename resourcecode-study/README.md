## Spring源码学习

### 一. BeanFactory和FactoryBean的区别
  * [Track to this repository](https://github.com/AvengerEug/spring/blob/develop/resourcecode-study/src/main/java/com/eugene/sumarry/resourcecodestudy/fbandbf/TestBeanFactory.java)

### 二. new AnnotationConfigApplicationContext(xxx.class)执行顺序
 ![执行顺序](https://github.com/AvengerEug/spring/blob/develop/resourcecode-study/spring%E6%9E%84%E9%80%A0%E6%96%B9%E6%B3%95-%E5%86%85%E9%83%A8%E6%89%A7%E8%A1%8C%E8%BF%87%E7%A8%8B.png)

### 三. Spring上下文结构
 ![执行顺序](https://github.com/AvengerEug/spring/blob/develop/resourcecode-study/spring上下文环境.png)

### 四. BeanDefinition添加至工厂过程
 ![执行顺序](https://github.com/AvengerEug/spring/blob/develop/resourcecode-study/beanDefinition注册过程.png)

### 五. AnnotationConfigApplicationContext
  * 注册单个bean(非java config类)
    1. 此时只是将该bean加入到spring容器中去, 走一遍bean的生命周期, 并且这个bean可以不用加任何注解
  
  * 注册配置类(java config类)
    1. java配置类可能会包含`@ComponentScan, @ImportResource`等注解, 所以要去解析并完成这些注解的功能,
       扫描所有注解是`ClassPathBeanDefinitionScanner`这个扫描器完成的
    
### 六. spring bean扩展点
  1. `BeanPostProcessor`后置处理器, spring内置了太多这样的实现(目测是使用订阅者模式实现, 将所有后置处理器存入列表中, 并依次执行)，aop切面就是这么实现的
    * 实现`PriorityOrdered`接口可以控制自定义后置处理器的执行顺序, 目测是根据返回的数字来设置谁先添加到列表中去, `@Order注解不行, 已经测试过了`
    * 疑问: spring自己写的后置处理器执行顺序会被打破么？ spring自己的后置处理器是没有添加跟注册bean有关的注解, 所以它是手动添加的
    * 在此处被触发PostProcessorRegistrationDelegate.invokeBeanFactoryPostProcessors的invokeBeanFactoryPostProcessors被触发
      内部有多个地方调用invokeBeanFactoryPostProcessors方法, 要注意传入的参数来确定调用的是哪一种BeanPostProcessor 
    
  2. `BeanFactoryPostProcessor` 使用jdk1.8的方式来扩展, 表示这个接口可以采用流的方式进行添加, 因为有这个注解`@FunctionalInterface`
  3. `BeanDefinitionRegistryPostProcessor` 也是在`PostProcessorRegistrationDelegate.invokeBeanDefinitionRegistryPostProcessors()`中的
     `invokeBeanDefinitionRegistryPostProcessors`被方法
     => 这种方式的扩展点有两种方式
        1. 手动添加到AbstractApplicationContext的beanFactoryPostProcessors属性中, 这种方式跟spring内置beanFactoryPostProcessors处理方式一样
        2. 直接添加@Component注解, 让spring扫描后去处理
        
        其实它是继承了BeanFactoryPostProcessor类, 所以BeanFactoryPostProcessor类的扩展也有这两种方式
    
  
### 七. ApplicationContextAwareProcessor 后置处理器作用
  * 首先它实现了`BeanPostProcessor`接口, 在bean初始化的时候被调用, 但因为它是spring内置的bean, 所以是采用手动添加列表的方式, 没有添加注册bean有关的注解
  * 代码如下: 它会给bean设置一些属性, 比如我们开发spring项目时经常会添加叫做`ApplicationContextHolder`的bean, 并让他实现`ApplicationContextAware`接口,
    以及实现它的set方法,目的是获取到spring上下文来扩展项目(比如获取scope为`prototype`的bean). 可以手动使用spring上下文来获取bean. 
    `ApplicationContextHolder`类中的applicationContext就是这样注入进去的。
    
    ```java
        // 
        @Component
        public class ApplicationContextHolder implements ApplicationContextAware {
        
            private static ApplicationContext applicationContext;
            private static final Logger logger = LoggerFactory.getLogger(ApplicationContextHolder.class);
        
            @Override
            public void setApplicationContext(ApplicationContext ctx) throws BeansException {
                applicationContext = ctx;
            }
        
            public static <T> T getBean(Class<T> clazz) {
                return applicationContext.getBean(clazz);
            }
        }
    
        // ApplicationContextAwareProcessor类中的部分源码
        private void invokeAwareInterfaces(Object bean) {
            if (bean instanceof Aware) {
                if (bean instanceof EnvironmentAware) {
                    ((EnvironmentAware)bean).setEnvironment(this.applicationContext.getEnvironment());
                }
    
                if (bean instanceof EmbeddedValueResolverAware) {
                    ((EmbeddedValueResolverAware)bean).setEmbeddedValueResolver(this.embeddedValueResolver);
                }
    
                if (bean instanceof ResourceLoaderAware) {
                    ((ResourceLoaderAware)bean).setResourceLoader(this.applicationContext);
                }
    
                // Spring事件模型注入spring事件发布者的代码处理位置
                if (bean instanceof ApplicationEventPublisherAware) {
                    ((ApplicationEventPublisherAware)bean).setApplicationEventPublisher(this.applicationContext);
                }
    
                if (bean instanceof MessageSourceAware) {
                    ((MessageSourceAware)bean).setMessageSource(this.applicationContext);
                }
    
                // 注入spring上下文属性到实现ApplicationContextAware接口的bean中去
                if (bean instanceof ApplicationContextAware) {
                    ((ApplicationContextAware)bean).setApplicationContext(this.applicationContext);
                }
            }
    
        }
    ```
    
### 八. BeanFactoryPostProcessor后置处理器的注意点
  ```text
    org.springframework.context.support.AbstractApplicationContext.refresh
      >org.springframework.context.support.AbstractApplicationContext.postProcessBeanFactory
        >PostProcessorRegistrationDelegate.invokeBeanFactoryPostProcessors(beanFactory, this.getBeanFactoryPostProcessors());
  ```
  * 在如上的代码调用栈下, 会去调用BeanFactoryPostProcessor的后置处理器, 
    但是这里只能获取到`程序员扩展spring的后置处理器`(其实就是把它跟spring内置后置处理器一样手动添加到bean工厂去)
    ```text
      这里的程序员扩展spring的后置处理器是什么含义呢？
        首先要实现一个`BeanFactoryPostProcessor`后置处理器, 
        要去实现BeanFactoryPostProcessor接口, 其次还要将当前
        类加到spring容器中去(一般添加@Component注解)。但是要明
        白, 虽然它实现了这个功能, 但是它并不在
        this.getBeanFactoryPostProcessors()方法返回的列表中
        (在AbstractApplicationContext类中), 所以这样的方式并
        不是在这里被触发的。那要如何在这里触发呢？
        步骤如下:
          1. 创建一个类并实现BeanFactoryPostProcessor接口
          2. 手动将后置处理器添加至工厂中, 
             AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
             context.addBeanFactoryPostProcessor(new MyBeanDefinitionRegistryPostProcessor());
             context.register(SimpleBean.class);
             context.refresh();
             // context.refresh()方法, 一定要最后去执行, 
             // 因为是在refresh方法中对BeanFactoryPostProcessor处理的
    ```
  * 此时只会触发BeanDefinitionRegistryPostProcessor接口的postProcessBeanDefinitionRegistry方法, 不会触发父类的BeanPostProcessor的方法
        目测是遍历的时候只调用了postProcessBeanDefinitionRegistry方法
      * 部分代码如下:
        
        ```java
            // beanFactoryPostProcessors就是手动调用AbstractApplicationContext类中的beanFactoryPostProcessors
            for (BeanFactoryPostProcessor postProcessor : beanFactoryPostProcessors) {
                if (postProcessor instanceof BeanDefinitionRegistryPostProcessor) {
                    BeanDefinitionRegistryPostProcessor registryProcessor =
                            (BeanDefinitionRegistryPostProcessor) postProcessor;
                    // 这里只触发了postProcessBeanDefinitionRegistry方法
                    registryProcessor.postProcessBeanDefinitionRegistry(registry);
                    registryProcessors.add(registryProcessor);
                }
                else {
                    regularPostProcessors.add(postProcessor);
                }
            }
        ```
  