## Spring源码学习

### 一. BeanFactory和FactoryBean的区别
  * [Track to this repository](https://github.com/AvengerEug/spring/blob/develop/resourcecode-study/src/main/java/com/eugene/sumarry/resourcecodestudy/fbandbf/TestBeanFactory.java)

### 二. new AnnotationConfigApplicationContext(xxx.class)执行顺序
 ![执行顺序](https://github.com/AvengerEug/spring/blob/develop/resourcecode-study/spring%E6%9E%84%E9%80%A0%E6%96%B9%E6%B3%95-%E5%86%85%E9%83%A8%E6%89%A7%E8%A1%8C%E8%BF%87%E7%A8%8B.png)

### 三. Spring 学习总结图
  #### 3.1 spring上下文环境
   ![执行顺序](https://github.com/AvengerEug/spring/blob/develop/resourcecode-study/spring上下文环境.png)

  #### 3.2. BeanDefinition添加至工厂过程
   ![执行顺序](https://github.com/AvengerEug/spring/blob/develop/resourcecode-study/beanDefinition注册过程.png)
   
  #### 3.3 invokeBeanFactoryPostProcessors执行过程
   ![执行顺序](https://github.com/AvengerEug/spring/blob/develop/resourcecode-study/invokeBeanFactoryPostProcessors执行过程.png)
   
   * 源码注释
     ```java
        public static void invokeBeanFactoryPostProcessors(
                    ConfigurableListableBeanFactory beanFactory, List<BeanFactoryPostProcessor> beanFactoryPostProcessors) {
        
                // Invoke BeanDefinitionRegistryPostProcessors first, if any.
                Set<String> processedBeans = new HashSet<>();
        
                // 传入的bean工厂DefaultListableBeanFactory也是一个BeanDefinitionRegistry, 它实现了这个接口
                if (beanFactory instanceof BeanDefinitionRegistry) {
                    BeanDefinitionRegistry registry = (BeanDefinitionRegistry) beanFactory;
        
                    // 用来存储手动添加BeanFactoryPostProcessor的处理器,
                    // eg: context.addBeanFactoryPostProcessor(new MyBeanDefinitionRegistryPostProcessor());
                    // 其中context是AnnotationConfigApplicationContext对象, 但是它只是执行到了父类AbstractApplicationContext的方法
                    List<BeanFactoryPostProcessor> regularPostProcessors = new ArrayList<>();
        
                    // 用来存储手动添加BeanDefinitionRegistryPostProcessor的处理器, 也是执行上述注释中说的方法
                    // 因为BeanFactoryPostProcessor有一个子类叫BeanDefinitionRegistryPostProcessor
                    // regularPostProcessors和registryProcessors这两个list只是为了存储手动添加的BeanFactoryPostProcessor
                    List<BeanDefinitionRegistryPostProcessor> registryProcessors = new ArrayList<>();
        
                    for (BeanFactoryPostProcessor postProcessor : beanFactoryPostProcessors) {
                        if (postProcessor instanceof BeanDefinitionRegistryPostProcessor) {
                            BeanDefinitionRegistryPostProcessor registryProcessor =
                                    (BeanDefinitionRegistryPostProcessor) postProcessor;
                            // 对于手动添加的BeanDefinitionRegistryPostProcessor会在这里第一次被调用, 所以这里是后置处理器第一次被调用的地方
                            registryProcessor.postProcessBeanDefinitionRegistry(registry);
                            // 存储手动添加的BeanDefinitionRegistryPostProcessor, 后续会用到
                            registryProcessors.add(registryProcessor);
                        }
                        else {
                            // 存储手动添加的BeanFactoryPostProcessor, 后续会用到
                            regularPostProcessors.add(postProcessor);
                        }
                    }
        
                    // 这个list是用来存储spring内置的BeanFactoryPostProcessor
                    List<BeanDefinitionRegistryPostProcessor> currentRegistryProcessors = new ArrayList<>();
        
                    // 这里是调用实现了PriorityOrdered接口的BeanDefinitionRegistryPostProcessor后置处理器
                    // 这里只是获取, 调用是在下面的invokeBeanDefinitionRegistryPostProcessors(currentRegistryProcessors, registry);完成的
                    String[] postProcessorNames =
                            beanFactory.getBeanNamesForType(BeanDefinitionRegistryPostProcessor.class, true, false);
                    for (String ppName : postProcessorNames) {
                        if (beanFactory.isTypeMatch(ppName, PriorityOrdered.class)) {
                            currentRegistryProcessors.add(beanFactory.getBean(ppName, BeanDefinitionRegistryPostProcessor.class));
                            processedBeans.add(ppName);
                        }
                    }
                    sortPostProcessors(currentRegistryProcessors, beanFactory);
                    registryProcessors.addAll(currentRegistryProcessors);
                    // 这里首先调用的类是spring内置beanName叫org.springframework.context.annotation.internalConfigurationAnnotationProcessor,
                    // 类型叫ConfigurationAnnotationProcessor的bean
                    // 因为在spring内置的6个bean中只有它是实现了BeanDefinitionRegistryPostProcessor接口
                    // 所以ConfigurationAnnotationProcessor类这一次被调用的主要目的是:
                    // 1. 为bean工厂生成factoryId并记录起来
                    // 2. 循环解析传入的配置类(即传入register方法中的几个Class类对应的类)
                    //  2.1 根据类获取他们的BeanDefinition, 来判断BeanDefinition是否为AnnotatedBeanDefinition类型(因为目前是考虑java config模式, 所以只考虑这种类型)
                    //  2.2 判断传入类是否加了@Configuration注解或者(@Component和@ComponentScan和@Import和ImportResource注解)或者内部是否有方法添加了@Bean注解并解析他们
                    invokeBeanDefinitionRegistryPostProcessors(currentRegistryProcessors, registry);
                    currentRegistryProcessors.clear();
        
                    // Next, invoke the BeanDefinitionRegistryPostProcessors that implement Ordered.
                    postProcessorNames = beanFactory.getBeanNamesForType(BeanDefinitionRegistryPostProcessor.class, true, false);
                    for (String ppName : postProcessorNames) {
                        if (!processedBeans.contains(ppName) && beanFactory.isTypeMatch(ppName, Ordered.class)) {
                            currentRegistryProcessors.add(beanFactory.getBean(ppName, BeanDefinitionRegistryPostProcessor.class));
                            processedBeans.add(ppName);
                        }
                    }
                    sortPostProcessors(currentRegistryProcessors, beanFactory);
                    registryProcessors.addAll(currentRegistryProcessors);
                    invokeBeanDefinitionRegistryPostProcessors(currentRegistryProcessors, registry);
                    currentRegistryProcessors.clear();
        
                    // Finally, invoke all other BeanDefinitionRegistryPostProcessors until no further ones appear.
                    boolean reiterate = true;
                    while (reiterate) {
                        reiterate = false;
                        postProcessorNames = beanFactory.getBeanNamesForType(BeanDefinitionRegistryPostProcessor.class, true, false);
                        for (String ppName : postProcessorNames) {
                            if (!processedBeans.contains(ppName)) {
                                currentRegistryProcessors.add(beanFactory.getBean(ppName, BeanDefinitionRegistryPostProcessor.class));
                                processedBeans.add(ppName);
                                reiterate = true;
                            }
                        }
                        sortPostProcessors(currentRegistryProcessors, beanFactory);
                        registryProcessors.addAll(currentRegistryProcessors);
                        invokeBeanDefinitionRegistryPostProcessors(currentRegistryProcessors, registry);
                        currentRegistryProcessors.clear();
                    }
        
                    // Now, invoke the postProcessBeanFactory callback of all processors handled so far.
                    // 这里是第一次调用手动添加到spring的BeanDefinitionRegistryPostProcessor的重写BeanFactoryPostProcessors接口的(postProcessBeanFactory)方法
                    // 因为BeanDefinitionRegistryPostProcessor是继承BeanFactoryPostProcessor类。所以也重写了BeanFactoryPostProcessor的方法
                    // 在第一次调用时只调用了BeanDefinitionRegisterPostProcessor中的方法
                    invokeBeanFactoryPostProcessors(registryProcessors, beanFactory);
                    // 这里是第一次调用手动添加到spring的BeanFactoryPostProcessor
                    invokeBeanFactoryPostProcessors(regularPostProcessors, beanFactory);
                }
        
                else {
                    // Invoke factory processors registered with the context instance.
                    invokeBeanFactoryPostProcessors(beanFactoryPostProcessors, beanFactory);
                }
        
                // Do not initialize FactoryBeans here: We need to leave all regular beans
                // uninitialized to let the bean factory post-processors apply to them!
                // 这里是调用非手动添加的BeanFactoryPostProcessor后置处理器, 即使用了@Component注解
                // 因为在上一步调用ConfigurationClassPostProcessor这种类型(BeanDefinitionRegistryBeanFactory)的后置处理器时, 对包已经扫描成功，
                // 并将扫描出来的类信息封装成ScannedGenericBeanDefinition的BeanDefinition了, 所以根据类型找出的来的bean包括以注解的方式注册的
                // BeanFactoryPostProcessor，但也包括ConfigurationClassPostProcessor, 因为它实现的BeanDefinitionRegistryBeanFactory接口也
                // 继承了BeanFactoryPostProcessor接口
                String[] postProcessorNames =
                        beanFactory.getBeanNamesForType(BeanFactoryPostProcessor.class, true, false);
        
                // Separate between BeanFactoryPostProcessors that implement PriorityOrdered,
                // Ordered, and the rest.
                List<BeanFactoryPostProcessor> priorityOrderedPostProcessors = new ArrayList<>();
                List<String> orderedPostProcessorNames = new ArrayList<>();
                List<String> nonOrderedPostProcessorNames = new ArrayList<>();
                for (String ppName : postProcessorNames) {
                    if (processedBeans.contains(ppName)) {
                        // skip - already processed in first phase above
                    }
                    else if (beanFactory.isTypeMatch(ppName, PriorityOrdered.class)) {
                        priorityOrderedPostProcessors.add(beanFactory.getBean(ppName, BeanFactoryPostProcessor.class));
                    }
                    else if (beanFactory.isTypeMatch(ppName, Ordered.class)) {
                        orderedPostProcessorNames.add(ppName);
                    }
                    else {
                        nonOrderedPostProcessorNames.add(ppName);
                    }
                }
        
                // First, invoke the BeanFactoryPostProcessors that implement PriorityOrdered.
                // 解析实现了PriorityOrdered接口的BeanFactoryPostProcessor并按顺序执行
                sortPostProcessors(priorityOrderedPostProcessors, beanFactory);
                invokeBeanFactoryPostProcessors(priorityOrderedPostProcessors, beanFactory);
        
                // 解析实现了Ordered接口的BeanFactoryPostProcessor并按顺序执行
                List<BeanFactoryPostProcessor> orderedPostProcessors = new ArrayList<>();
                for (String postProcessorName : orderedPostProcessorNames) {
                    orderedPostProcessors.add(beanFactory.getBean(postProcessorName, BeanFactoryPostProcessor.class));
                }
                sortPostProcessors(orderedPostProcessors, beanFactory);
                invokeBeanFactoryPostProcessors(orderedPostProcessors, beanFactory);
        
                // 调用实现了没有实现PriorityOrdered和Ordered接口的BeanFactoryPostProcessor的后置处理器
                List<BeanFactoryPostProcessor> nonOrderedPostProcessors = new ArrayList<>();
                for (String postProcessorName : nonOrderedPostProcessorNames) {
                    nonOrderedPostProcessors.add(beanFactory.getBean(postProcessorName, BeanFactoryPostProcessor.class));
                }
                invokeBeanFactoryPostProcessors(nonOrderedPostProcessors, beanFactory);
        
                // Clear cached merged bean definitions since the post-processors might have
                // modified the original metadata, e.g. replacing placeholders in values...
                beanFactory.clearMetadataCache();
            }
     ```

### 四. AnnotationConfigApplicationContext
  * 注册单个bean(非java config类)
    1. 此时只是将该bean加入到spring容器中去, 走一遍bean的生命周期, 并且这个bean可以不用加任何注解
  
  * 注册配置类(java config类)
    1. java配置类可能会包含`@ComponentScan, @ImportResource`等注解, 所以要去解析并完成这些注解的功能,
       扫描所有注解是`ClassPathBeanDefinitionScanner`这个扫描器完成的
    
### 五. spring bean扩展点
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
    
  
### 六. ApplicationContextAwareProcessor 后置处理器作用
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
    
### 七. BeanFactoryPostProcessor后置处理器的注意点
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
  
### 八. 使用ImportSelector注解和BeanPostProcessor后置处理器模拟aop
  * 使用该方式可以手动开关aop功能
  * 具体核心在spring扫描解析类的时候, 会处理Import注解, 在处理Import注解时会处理如下三种情况
    1. 普通类
    2. ImportSelector
       ```text
         处理ImportSelector类型情况是因为提供了AnnotationMetadata对象, 它可以获取到当前解析类的注解信息(它一定加了@Import注解)
         此时可以根据AnnotationMetadata对象来获取它拥有什么注解和什么方法, 最后返回一个字符串数组， 这个字符串数组非常重要,
         内部元素是一个类的全类名, spring会根据这个全类名把这个类也加入到spring容器中去, 所以可以根据解析类是否添加@Proxy注解(这
         个注解是自定义的)来返回一个数组(数组中包含一个BeanPostProcessor后置处理器, 此时这个后置处理器也会被加到bean工厂中去, 在
         创建bean的时候会被调用), 在后置处理器中再针对具体的类来创建代理对象, 至此, 完成了自定义的aop. 
       ```
    3. ImportBeanDefinitionRegistrar
    
    
    