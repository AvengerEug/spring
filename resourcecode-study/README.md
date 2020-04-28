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

  #### 3.4 processConfigBeanDefinitions执行过程

   * 主要作用: 循环bean工厂所有的beanDefinition, 处理配置类(全配置类)和非配置类的beanDefinition

   ![执行顺序](https://github.com/AvengerEug/spring/blob/develop/resourcecode-study/processConfigBeanDefinitions执行过程.png)

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
    
  2. `BeanFactoryPostProcessor` 这个接口可以采用流的方式进行添加, 因为有这个注解`@FunctionalInterface`
  
      * spring内置类`ConfigurationClassPostProcessor`就是一个BeanFactoryPostProcessor, 在此后置处理器中对全配置类(在解析配置类时, 
        若类中存在@Configration注解, 则添加一个属性, key`org.springframework.context.annotation.ConfigurationClassPostProcessor
        .configurationClass" -> "metadata attribute 'org.springframework.context.annotation.ConfigurationClassPostProcessor.configurationClass'`为, 内容为`full`)进行了代理处理, 具体
        处理方式如下: 

            1. 给全配置类对应的BeanDefinition添加了一个属性, key为`org.springframework.aop.framework.autoproxy.AutoProxyUtils.preserveTargetClass`内容为`true`
            2. 生成cglib代理类class, 并覆盖原有BeanDefinition的beanClass属性
     
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
    
### 七. 第一次调用BeanFactoryPostProcessor后置处理器的注意点

  * 调用链如下：
  
    ```java
      org.springframework.context.support.AbstractApplicationContext.refresh
        >org.springframework.context.support.AbstractApplicationContext.invokeBeanFactoryPostProcessors
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

### 八. 使用Import注解和BeanPostProcessor后置处理器模拟aop
  * 使用该方式可以手动开关aop功能
  * 具体核心在spring扫描解析类的时候, 会处理Import注解, 在处理Import注解时会处理如下三种情况
    1. 普通类
    
    2. ImportSelector
       ```markdown
         处理ImportSelector类型情况是因为提供了AnnotationMetadata对象, 它可以获取到当前解析类的注解信息(它一定加了@Import注解)
         此时可以根据AnnotationMetadata对象来获取它拥有什么注解和什么方法。 重写的方法最后返回一个字符串数组， 这个字符串数组非常
         重要,内部元素是一个类的全类名, spring会根据这个全类名把这个类也加入到spring容器中去, 所以可以根据解析类是否添加@EnableProxy注
         解(这个注解是自定义的, 具体可以参考此类[MyImportSelector.java](https://github.com/AvengerEug/spring/blob/develop/resourcecode-study/src/main/java/com/eugene/sumarry/resourcecodestudy/annocontext/registersimplebean/MyImportSelector.java))来返回一个数组(数组中包含一个BeanPostProcessor后置处理器, 此时这个后置处理器也会被加到bean工厂中去
         , 在创建bean的时候会被调用), 在后置处理器中再针对具体的类来创建代理对象, 至此, 完成了自定义的aop. 
       ```
       
    3. ImportBeanDefinitionRegistrar
    
       ```txt
       此类比较牛逼, 因为spring提供了一个BeanDefinitionRegistry, 这个类可以动态的添加beanDefinition, 我们可以指定具体某个类的自动装配模式，结合spring只为某些类型的bean自动装配的特性，我们可以为某个类不添加任何注解就将它交给spring处理，并完成自动装配, eg: mybatis的mapperFactoryBean以及自己实现的jwt-util jar包(连接: https://github.com/AvengerEug/permission-backend/blob/develop/jwt-util/src/main/java/com/eugene/sumarry/jwtutil/processor/ImportJWTRegistrar.java)
       ```
    
       

### 九. spring在解析一个@Configuration标记的类时, 如何控制它不被解析
  * 手动将该类的beanDefinition标识为配置类(全配置类或者部分配置类都行)
    eg:
    ```java
     context.getBeanDefinition("指定类的名称").setAttribute("org.springframework.context.annotation.ConfigurationClassPostProcessor.configurationClass", "lite");
     // 或者
     context.getBeanDefinition("指定类的名称").setAttribute("org.springframework.context.annotation.ConfigurationClassPostProcessor.configurationClass", "full");
    ```
  * 原理: 
    ```java
        // 下面的configCandidates对象非常重要, 因为spring在解析配置类的时候会校验它是一个什么样(全配置类还是部分配置类)的配置类
        // 只有spring自己手动标识的配置类才会被解析, 也就是下面的这行代码
        // ConfigurationClassUtils.checkConfigurationClassCandidate(beanDef, this.metadataReaderFactory)
        // spring执行了这段代码, 并且该类符合规则, 则返回true, 最后添加到configCandidates数据结构中去
        // 后面是根据这个集合中的配置类进行解析的,
        // 所以要实现这样一个功能(spring在解析一个@Configuration标记的类时, 如何控制它不被解析), 只需要在类被解析之前手动
        // 将beanDefinition标志为配置类就ok了
        for (String beanName : candidateNames) {
            BeanDefinition beanDef = registry.getBeanDefinition(beanName);
            if (ConfigurationClassUtils.isFullConfigurationClass(beanDef) ||
                    ConfigurationClassUtils.isLiteConfigurationClass(beanDef)) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Bean definition has already been processed as a configuration class: " + beanDef);
                }
            }
            else if (ConfigurationClassUtils.checkConfigurationClassCandidate(beanDef, this.metadataReaderFactory)) {
                configCandidates.add(new BeanDefinitionHolder(beanDef, beanName));
            }
        }
    ```
    
### 九. 后置处理器类型及功能示例

  * 见下表

    | 类型 | 执行时间 | 作用 | 提供的api | 示例 |
    | :--: | :--: | :--: | :--: | :--: |
    | BeanPostProcessor | bean创建后, 在将bean放入</br>bean工厂单例容器的`前后执行` | 能干扰创建bean的结果 | 提供了bean的名字和bean的对象 | @PostConstruct注解、AOP |
    | BeanFactoryPostProcessor | 在调用后置处理器(spring内置的、手动添加的、加了注解的)处执行 | 扩展spring | 提供了bean工厂对象 | spring内置类`ConfigurationClassPostProcessor`</br>为`@Configuration`注解生成cglib代理对象|
    | BeanDefinitionRegistryPostProcessor | 在调用后置处理器(spring内置的、手动添加的、加了注解的)处执行 | 扩展spring | 提供了BeanDefinitionRegistry类型的对象, 其实也是bean工厂 | spring内置类`ConfigurationClassPostProcessor` 加载所有包含`@Component`注解的类, 以及加载配置类(全配置@Configuration和部分配置@Component, @ComponentScan, @Import, @ImportResource, @Bean) |
    | ImportSelector | 在解析@Import注解时, 会解析类型为ImportSelector类型导入类 | 解析重写接口selectImports方法的返回值(是一个的全类名组成的字符串数组), 并将它们当做@Import注解导入的类再走一遍@Import的流程, 最终普通类会被添加到配置类解析器的存放配置类的数据结构中 | 返回字符串数组(内部为类的类的全类名), 并将他们加到bean工厂中 | 能获取被@Import标注的类的`AnnotationMetadata`对象, 可以根据该对象的一些标志来导入一些后置处理器进而影响某个bean的创建 | 未想到 |
    | ImportBeanDefinitionRegistrar | 在解析@Import注解时, 会解析类型为ImportBeanDefinitionRegistrar类型导入类 | 能获取BeanDefinitionRegistry, 可以手动的添加和修改beanDefinition | 能获取导入类的`AnnotationMetadata`对象以及beanDefinition注册器`BeanDefinitionRegistry` | mybatis的@MapperScan注解功能 |
    
### 十. spring自动装配注意事项, 以及spring默认不装配的几种类型

  * spring自动装配原则(beanDefinition的`autowireMode`属性为`AbstractBeanDefinition接口中的AUTOWIRE_BY_NAME, AUTOWIRE_BY_TYPE和AUTOWIRE_CONSTRUCTOR`才会自动进行装配)
    
    * 获取当前类的beanDefinition并拿到MutablePropertyValues对象中所有手动添加的property,
      为了让spring不自动装配, eg: 我们自己要设置它

    * 获取当前类(包含父类以及超类Object)中所有的get、set方法. spring会
       将方法名前面的set、get去掉后再将第一个字母小写得到的值
       (eg: getTest(); 最终会得到test)作为自动装配的候选者).
    * 装配的条件:
      
        | 对应spring源码 | 含义 | 判断顺序 |
        | -- | -- | -- |
        | pd.getWriteMethod() | 验证属性是否有set方法, pd为属性的描述器 | 1 |
        | !isExcludedFromDependencyCheck(pd) | 不在忽略自动装配的条件内(就是在spring refresh</br>方法中的prepareBeanFactory方法中添加的忽略自动装配的几种类型) | 2 |
        | !pvs.contains(pd.getName()) | 不在手动添加到beanDefinition的</br>MutablePropertyValues对象中的属性.</br>pvs为获取当前要自动装配类的beanDefinition的</br>MutablePropertyValues对象中存</br>储手动添加property的属性 | 3 |
        | !BeanUtils.isSimpleProperty(pd.getPropertyType()) | 不是一个简单属性, 见下面的源码 | 4 |
    
    * isSimpleProperty, 简单属性的源码:

      ```java
        // 这里只是截图了这个方法的源码
        public static boolean isSimpleValueType(Class<?> clazz) {
          return (ClassUtils.isPrimitiveOrWrapper(clazz) ||
              Enum.class.isAssignableFrom(clazz) ||
              CharSequence.class.isAssignableFrom(clazz) ||
              Number.class.isAssignableFrom(clazz) ||
              Date.class.isAssignableFrom(clazz) ||
              URI.class == clazz || URL.class == clazz ||
              Locale.class == clazz || Class.class == clazz);
        }
      ```

### 十一. 获取FactoryBean实例与它维护的bean实例规则

* 说这个规则之前, 首先先了解下FactoryBean的创建规则

  * 因为FactoryBean也是一个bean, 所以肯定在`ConfigurationClassPostProcessor`后置处理器执行时就已经被
    扫描出来并注册到bean工厂了, 所以在`finishBeanFactoryInitialization`方法中会对它进行初始化, 不管是
    因为某个类依赖了它而被初始化还是循环创建bean时的初始化, 都要走createBean流程
  * 这里先说一下遍历所有beanDefinition, 无类依赖它的情况
    ```java
        if (isFactoryBean(beanName)) {
            Object bean = getBean(FACTORY_BEAN_PREFIX + beanName);
            //。。。。。。 省略后面的代码
        }
    ```
    由上可知, 创建FactoryBean的时候会给默认的beanName加上一个`&`符号, 假设这个FactoryBean的名字被spring
    默认扫描出来后变成了testFactoryBean, 那么此时它会变成`&testFactoryBean`,
    但是在**getBean(FACTORY_BEAN_PREFIX + beanName)**方法内部却又对他进行了拆解(将前面的&去掉)并赋值给
    新变量(beanName), 最终整个创建bean的流程就是以无`&`符号的名字取创建, 所以最终会在bean工厂中创建一个名
    为`testFactoryBean`的bean
    
    当我们调用spring获取bean的api时(eg: context.getBean("&testFactoryBean")或context.getBean("test
    FactoryBean"))时, 它最终都是根据`testFactoryBean`来获取bean, 但是每次都会携带原来传入的bean名称和处理
    过的bean名称，分别存在name和beanName两个变量, 所以它最终是根据beanName去获取bean的, 拿到的bean一定是一
    个FactoryBean。最终再调用getObjectForBeanInstance方法来获取真正需要的bean。spring会根据getBean("beanName")
    api中的beanName来决定return哪个bean。若api中的beanName包含&符号, 那么就返回FactoryBean即可，若不包含则返回
    FactoryBean中维护的bean, 即调用getObject方法。如果是第二次获取FactoryBean对象的话, 那么就会从一个名
    叫`factoryBeanObjectCache`的map中去取, 因为在第一次获取时, 会将这个对象放在这个map中, key为FactoryBean
    的名称, 与spring容器中存储FactoryBean的名称一致, 只不过他们是存在不同的数据结构中。`并且FactoryBean维护的bean不会走spring的生命周期，即不会回调类似InitializingBean接口的afterPropertiesSet()方法`
  

### 十二. spring事件驱动原理

* 首先先总结下如何使用spring的事件驱动
  1. 新建事件源类。继承**ApplicationEvent**类, 并重写带参构造方法
     ```java
        public class MyApplicationEvent extends ApplicationEvent {
            // 带参构造方法中的参数就是可以为事件添加一些参数, 即
            // 自定义一些事件
            public MyApplicationEvent(Object source) {
                super(source);
            }
        }
     ```
     
  2. 新建事件监听器。
     ```java
        // 1. 需要添加@Component注解, 让监听者作为spring的一个bean
        // 2. 其次, 要实现ApplicationListener接口, 泛型为上述定义的事件源(MyApplicationEvent)
        @Component
        public class MyApplicationListener implements ApplicationListener<MyApplicationEvent> {
        
            @Override
            public void onApplicationEvent(MyApplicationEvent event) {
                System.out.println("监听者, " + event);
            }
        }
     ```
  
  3. 获取spring ApplicationEventPublisher对象
     ```java
        // 新建了一个bean来维护上下文对象
        @Component
        public class ApplicationContextHolder implements ApplicationContextAware {
        
            private static ApplicationContext applicationContext;
        
            @Override
            public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
                applicationContext = applicationContext;
            }
       
            public static ApplicationContext getApplicationContext() {
                return applicationContext;
            }
        }
     ```
  
  4. 触发事件
     ```java
        // 拿到spring上下文发布事件
        // spring上下文如何拿到？ spring上下文是指实现了ApplicationContext接口的类
        // 在以java config技术开启的spring应用程序中, 可以用AnnotationConfigApplicationContext的实例, 第三步可以忽略
        // 若是spring web应用程序, eg: spring mvc, 则可以新增一个bean并实现ApplicationContextAware接口, eg: 第三步
        ApplicationContextHolder.getApplicationContext().publishEvent(new MyApplicationEvent(new String[] {
           "1", "2"
        }))
     ```
  
* 原理
  1. spring在执行refresh方法中的prepareBeanFactory方法时, 会新增spring内置的ApplicationContextAwareProcessor后置处理器
     => 此后置处理器会将ApplicationContextHolder中的ApplicationContext属性注入进去
  2. spring在执行refresh方法中的registerBeanPostProcessors方法时, 会新增ApplicationListener bean的后置处理器
     `ApplicationListenerDetector`, 主要作用就是将他添加到上下文对象的存放ApplicationListener的Set集合中
  3. spring在执行refresh方法中的initApplicationEventMulticaster方法时, 会初始化spring的事件驱动对象
     `ApplicationEventMulticaster`, 它是通过beanFactory创建出来的, 所以走了spring的bean生命周期
  4. spring在执行refresh方法中的registerListeners方法时, 主要是将扫描出来的listener的beanName添加到第三步
     产生的`ApplicationEventMulticaster`中
  5. spring在执行refresh方法中的finishBeanFactoryInitialization, 开始初始化所有的bean, 在开始创建MyApplicationListener
     bean时, 创建完后会调用第2步注册的后置处理器.
  6. 发布事件, 根据事件本身对象拿到监听者, 然后调用监听者的方法
  
* 小结
  * 这里用到了很多集合来存储监听者对象, 用了广播机制(监听者模式), 当一个事件发布的时候, 要找到所有这个事件的监听者, 然后再统一调用
  * spring内置有两个Event(ApplicationContextEvent和RequestHandlerEvent), 具体信息如下:
    1. ApplicationContextEvent
      * ContextRefreshEvent
      * ContextStopEvent
      * ContextStartEvent
      * ContextCloseEvent
    
    2. RequestHandlerEvent
      * 待总结`d'd
  

### 十三. @Bean与@Configuration注解

  * 因为`ConfigurationClassPostProcessor`类的特殊身份(是`BeanDefinitionRegistryPostProcessor`和`BeanFactoryPostProcessor`),
    它处于`BeanFactoryPostProcessor`身份时会对@Configuration注解标识的类进行cglib代理, 为了防止如下情况下:testBeanAnnotation1
    方法调用两次时, TestBeanAnnotation1类会被创建出两个对象。
    
    ```java
        @Configuration
        @ComponentScan("com.eugene.sumarry.resourcecodestudy.annocontext.registersimplebean")
        public class AppConfig {

            @Bean
            public TestBeanAnnotation1 testBeanAnnotation1() {
                return new TestBeanAnnotation1();
            }
        
            @Bean
            public TestBeanAnnotation2 testBeanAnnotation2() {
                testBeanAnnotation();
                testBeanAnnotation();
                return new TestBeanAnnotation2();
            }
        
        }
    ```
  
  * 当代码改动成这样, testBeanAnnotation1方法变成静态的, 那么就会创建成两个对象, 原因是在处理@Bean的方法时,
    会对要返回出来的bean添加FactoryMethod方法, 所以在创建这个bean的时候发现它有FactoryMethod方法, 最终会
    将FactoryMethod的返回值作为这个bean的创建结果。
    
    ```java
        @Configuration
        @ComponentScan("com.eugene.sumarry.resourcecodestudy.annocontext.registersimplebean")
        public class AppConfig {

            @Bean
            public static TestBeanAnnotation1 testBeanAnnotation1() {
                return new TestBeanAnnotation1();
            }
        
            @Bean
            public TestBeanAnnotation2 testBeanAnnotation2() {
                testBeanAnnotation();
                testBeanAnnotation();
                return new TestBeanAnnotation2();
            }
        
        }
    ```
    
### 十四. 使用构造方法创建普通bean(非FactoryBean类型)步骤
  * 1. 遍历bean工厂存放beanNames集合，开始创建非抽象、单例、非懒加载的bean
  * 2. getBean(beanName)
    ```text
        getBean是一个common的方法, 创建bean和之后使用上下文根据beanName获取bean
        以及循环依赖使用的getBean都会进入此方法. 
    ```
  * 3. doGetBean
  * 4. getSingleton(beanName)
    ```text
        doGetBean针对beanName做了处理, 主要是考虑到了FactoryBean的情况:
        1. 内部维护了两个bean的名称, 一个叫beanName另一个叫name, 
           beanName存放是处理过的名称(将beanName前面的'&'符号去掉), name是存放原始的bean名称
        2. 首先从bean工厂的单例池中去获取, 刚刚也说了这个方法在获取bean的时候也会被调用, 这个操
           作对应的就是这种情形, 但拿到bean后不会立刻返回这个bean, 因为我们要确定用户要的是Fact
           oryBean还是FactoryBean维护的bean, 这个判断依据就是根据上述提供的beanName和name的值
           以及当前从单例池获取到的bean的类型综合判断
    ```
  * 5. getSingleton(String beanName, ObjectFactory<?> singletonFactory)
    ```text
        此方法要创建新的bean时才会用到。
        大致步骤为:
        1. beforeSingletonCreation: 标识这个bean正在被创建
        2. singletonFactory.getObject(): 实际上是执行createBean方法
        3. doCreateBean
        4. 判断使用哪一个构造方法来实例化对象
        5. 判断是否使用构造方法自动装配
           条件如下:
           1. ctors != null 
                => 首先获取到所有@Autowired注解的构造方法，并根据一些条件来判断使用具体的哪一个
                   其次再看是否只有一个带参数的构造方法，如果有则使用它并自动装配
                   再然后获取到所有非@Autowired注解的构造方法，并根据是否有@Primary注解和默认构造方法来决定使用具体的哪一个
           2. mbd.getResolvedAutowireMode() == AUTOWIRE_CONSTRUCTOR  => 此条件为使用后置处理器手动修改beanDefinition的自动装配的值, 默认为no
           3. mbd.hasConstructorArgumentValues()
               => 使用后置处理器手动添加beanDefinition中存放构造方法值的情形
           
           4. !ObjectUtils.isEmpty(args)
           上述条件只要满足一个就走构造方法自动装配的逻辑
        6. 使用CglibSubclassingInstantiationStrategy策略创建bean 
        7. 填充bean属性, 执行populateBean方法, 完成自动装配和循环依赖
        8. 若bean实现了InitialzingBean接口, 则调用对应的接口
        9. 回调beanPostProcessor后置处理器  
        10. afterSingletonCreation, bean创建结束, 将bean的正在创建状态去除
        11. addSingleton, 将bean添加到spring单例池中
    
    ```

### 十五. 循环依赖步骤
```text
    一个案例了解循环依赖:

    1. 创建Bean a
        第一次调用getSingleton方法, 单例池中无bean a 也没标识正在被创建, 所以走下面第二个getSingleton方法。此时a被标注正在被创建走使用构造方法创建bean的逻辑
    2. 在创建完a后开始记录bean a的一些状态(将bean name添加至registeredSingletons以及将bean对应的ObjectFactory添加至singletonFactories)和填充属性,  发现它依赖于B, 此时要去getBean(b)
    3. 然后进入创建bean b流程
    4. 此时会进入第一个getSingleton方法, 此时spring单例池中无b并且b也还未被标注为被创建(因为它是在getSingleton方法后面标注的)状态所以进入创建createBean方法, 
    5. 创建完b后开始填充属性, 发现它又依赖于A, 此时要去getBean(a)
    6. 此时进入第一个getSingleton方法, 发现单例池中无a(虽然a在上述过程中被创建了, 但是它还未放入单例池中)但是它是处于被创建的状态, 所以从singletonFactories中根据beanName拿ObjectFactory, 最终从ObjectFactory拿到Bean a, 并将singletonFactories中beanName对应的objectFactory remove掉,以及将拿到的bean a放入到earlySingletonObjects中
    7. 最终拿到了bean a, 此时将bean a注入到bean b中的a属性中, 完成bean的创建(此时会将它正在创建的标识移除并它放入单例池中去),  并调用BeanPostProcessor后置处理器以及InitializingBean接口的afterPropertiesSet方法
    8. 此时又回到了上述的第二步, 现在第二步已经拿到bean b了, 所以将bean a中的b属性给注入进去, 最终完成bean a的创建, 将a也添加到spring单例池中并调用BeanPostProcessor后置处理器以及InitializingBean接口的afterPropertiesSet方法
    9. 循环依赖步骤完成
    
    涉及到的几个数据结构:
    1. registeredSingletons: 表示该bean已经通过构造方法创建出来了, 
    2. singletonFactories: 存放bean对应的objectFactory方法, 循环依赖时需要根据它来拿bean
    3. earlySingletonObjects: 与singletonFactories的操作是互斥的, 里面存放的是singletonFactories中beanName对应的objectFactory创建出来的bean,若一个beanName在该集合中存在, 那么该bean对应的ObjectFactory就会在singletonFactories中被remove掉
    4. singletonsCurrentlyInCreation: 表示当前bean正在被创建, 在getSingleton(String beanName, ObjectFactory<?> singletonFactory)方法中进行操作, 添加、移除正在创建的标识都是在此方法中完成
```

### 十六. spring事务管理原理与自定义事务
  * spring开启事务步骤
    1. 添加`@EnableTransactionManagement`注解开启事务管理
    2. 添加事务管理器的bean, eg: `DataSourceTransactionManager`
    3. 在方法或类中添加`@Transactional`注解(可配置rollbackForClassName指定回滚的类型)
    
  * 原理:
    1. `@EnableTransactionManagement`注解利用了spring的@Import注解扩展点,
       import了类`TransactionManagementConfigurationSelector`
       (父类进行校验, 自己负责返回真正的bean)
       并根据通知模式(`adviceMode`)来动态的添加特定的bean
       若通知模式为PROXY, 
         则添加`AutoProxyRegistrar`和`ProxyTransactionManagementConfiguration`两个bean
         其中`AutoProxyRegistrar`还是个`ImportBeanDefinitionRegistrar`, 最终会注册一个
         cglib代理类
       若通知模式为ASPECTJ,
         则添加`org.springframework.transaction.aspectj.AspectJTransactionManagementConfiguration`
         bean
       使用哪种模式可以在`@EnableTransactionManagement`注解的mode属性指定, 默认为`PROXY`
    2. 需要添加事务管理器, 这里做的比较低耦合, 我们可以实现自定义的事务管理器, eg: jpa、jdbc等等
       但spring的事务模块中并没有手动添加默认的事务管理器, 这个需要我们自己手动添加
    3. 添加`@Transactional`注解, 可指定为抛出异常的指定类来执行回滚操作
    
* 相关总结：

  ```txt
  1. 事务生效的情况：
    不管是使用cglib的代理还是jdk的动态代理，最终只有public修饰的方法才能享用事务。
  2. 事务的功能是代理类来实现的，即代理类对方法进行了增强，所以如果一个不带事务注解的方法调用一个带事务注解的方法，这种情况下也是不生效的。因为调用不带事务注解的方法时，代理类中并没有增强方法(添加开启事务、提交事务、回滚事务的相关方法)，所以最终还是要用目标对象去调用自己的方法，所以此时就算调用了带了事务注解的方法，也不会有事务，因为当前的this为目标对象
  ```

* 事务传播机制

  |   事务类型    |                             特点                             |          备注          |
  | :-----------: | :----------------------------------------------------------: | :--------------------: |
  |   REQUIRED    |    两个带有事务注解的方法共有，若事务不存在则创建一个新的    |          默认          |
  | NOT_SUPPORTED |                          不支持事务                          |                        |
  | REQUIRES_NEW  | 不管之前有没有事务，都会为开启一个新的事务，待新的事务执行完毕后，再执行之前的事务 |                        |
  |   MANDATORY   |             必须在有事务的情况下执行，否则抛异常             | 此类型依赖于已有的事务 |
|     NEVER     |   必须在一个没有的事务中执行,否则抛出异常(与MANDATORY相反)   |                        |
  |   SUPPORTS    |    依赖于调用方是否开启事务，若调用方开启了事务则使用事务    |                        |
  
  

### 十七. Spring bean初始化过程中涉及到的后置处理器、执行顺序及作用

| 执行顺序 | 执行位置 | 执行到的后置处理器 | 执行的方法 | 作用 |
| :-: | :-: | :-: | :-: | :-: |
| 1 | org.springframework.beans.factory.support.</br>AbstractAutowireCapableBeanFactory.doCreateBean => resolveBeforeInstantiation方法  | InstantiationAwareBeanPostProcessor | 1. postProcessBeforeInstantiation</br> 2.postProcessAfterInitialization | 1. postProcessBeforeInstantiation作用: 若后置处理器中返回了一个对象, 则不会走spring的创建bean的流程</br> 2.postProcessAfterInitialization方法执行了BeanPostProcessor后置处理器, 自动装配的后置处理器就间接实现了BeanPostProcessor, 若BeanPostProcessor不被执行, 那么自动装配的功能也将缺失  |
| 2 | org.springframework.beans.factory.support.</br>AbstractAutowireCapableBeanFactory.createBeanInstance => determineConstructorsFromBeanPostProcessors | SmartInstantiationAwareBeanPostProcessor | determineCandidateConstructors | 扫描当前bean携带@Autowired注解的构造方法 |
| 3 | org.springframework.beans.factory.support.</br>AbstractAutowireCapableBeanFactory.doCreateBean => applyMergedBeanDefinitionPostProcessors | MergedBeanDefinitionPostProcessor | postProcessMergedBeanDefinition | 待写 |
| 4 | org.springframework.beans.factory.support.</br>AbstractAutowireCapableBeanFactory.doCreateBean => getEarlyBeanReference |  SmartInstantiationAwareBeanPostProcessor | getEarlyBeanReference | 待写 |
| 5 | org.springframework.beans.factory.support.</br>AbstractAutowireCapableBeanFactory.populateBean => postProcessAfterInstantiation | InstantiationAwareBeanPostProcessor | postProcessAfterInstantiation | 待写 |
| 6 | org.springframework.beans.factory.support.</br>AbstractAutowireCapableBeanFactory.populateBean => postProcessPropertyValues | InstantiationAwareBeanPostProcessor | postProcessPropertyValues | 待写 |
| 7 | org.springframework.beans.factory.support.</br>AbstractAutowireCapableBeanFactory.initializeBean => applyBeanPostProcessorsBeforeInitialization | BeanPostProcessor | postProcessBeforeInitialization | 当bean被实例化并完成自动装配之后执行 |
| 8 | org.springframework.beans.factory.support.</br>AbstractAutowireCapableBeanFactory.initializeBean => applyBeanPostProcessorsAfterInitialization | BeanPostProcessor | postProcessAfterInitialization | 当bean被实例化并完成自动装配之后执行 |

* 如上, 一共会执行4个后置处理器**InstantiationAwareBeanPostProcessor**, **SmartInstantiationAwareBeanPostProcessor**, **MergedBeanDefinitionPostProcessor**, **BeanPostProcessor**, 共执行了8次

### 十八. Spring Mvc
* 启动流程
   1. DispatchServlet设置setLoadOnStartup(1) => 在tomcat启动时就会调用Servlet的init方法
   
   2. Spring Mvc的两种Controller的定义方式:
   
      * ```java
        @RestController
        @RequestMapping("/index")
        public class Index1Controller {
        
           @GetMapping
           @ResponseBody
           public String index() {
              return "indexssss";
           }
        }
        ```
   
      * ```java
        @Component("/index2")
        public class Index2Controller implements Controller {
        
           @Override
           public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
              return new ModelAndView("index");
           }
        }
        ```
   
      * 解析:
   
        1. 上述第一种在springmvc底层中会使用`org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping`
   
           来处理, 主要是处理Request uri和method的映射关系
   
        2. 上述的第二种在springmvc底层中会使用
   
           `org.springframework.web.servlet.handler.BeanNameUrlHandlerMapping`来处理, 主要是处理request uri和bean name的映射关系。 此种方式跟原生的`servlet`功能一样, 只能处理一个请求,即一个bean处理一个请求, 且返回值只能是`ModelAndView`. 并且bean name一定要为request的uri。到最后调用时，此种方式最简单, 直接获取到bean并调用接口中的**handleRequest**方法即可。
   
   3. 一个url从浏览器输入到最后获取到对应mapping handler的过程
   
      	1. 浏览器输入**localhost:8080/index**
       	2. 进入dispatchServlet的**doService**方法， 并调用分发**doDispatch**方法
       	3. 调用**getHandler**方法, 根据request 的uri获取对应的mapping handler处理器
       	4. 根据mapping uri从上述存储url和handler映射关系的两个map(`org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping、org.springframework.web.servlet.handler.BeanNameUrlHandlerMapping`)中获取handler. 先从**BeanNameUrlHandlerMapping**获取后再从**RequestMappingHandlerMapping**获取。
       	5. 根据mapping handler获取handlerAdapter, 即根据不同的Controller类型(上述的两种实现controller的方式)获取到不同的适配器，最终根据适配器来执行请求
   
   4. 两个存储request mapping的类何时被初始化的?
   
      1. DispatchServlet类的静态代码块中加载了一个配置文件**DispatcherServlet.properties**
   
      2. spring发布refresh事件时，**FrameworkServlet**类中维护了一个内部类**ContextRefreshListener**。此类是spring事件模型中继承了ApplicationContextEvent类的ContextRefreshEvent事件的监听器。在此处**initStrategies**方法, 并在此方法中做了spring mvc一系列的事情，具体如下
   
         ```java
         protected void initStrategies(ApplicationContext context) {
            // 初始化spring文件上传的bean, 这段代码可以解释为什么文件上传的bean的name一定要写死成multipartResolver
            initMultipartResolver(context);
            initLocaleResolver(context);
            initThemeResolver(context);
             
            // 初始化springmvc url的映射关系, 此处会初始化那两个map BeanNameUrlHandlerMapping, RequestMappingHandlerMapping
            initHandlerMappings(context);
             
            // 同理, 初始化springmvc的handlerAdapter, 与handlerMapping的初始化流程类似，都是从配置文件中读取对应类的class全路径名，并使用spring的bean工厂完成bean的创建
            // 分别是这三个:
            // org.springframework.web.servlet.mvc.HttpRequestHandlerAdapter 
            // org.springframework.web.servlet.mvc.SimpleControllerHandlerAdapter => 处理BeanNameUrlHandlerMapping类型的handlerMapping
            // org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter => 处理RequestMappingHandlerMapping类型的handlerMapping
            initHandlerAdapters(context);
            initHandlerExceptionResolvers(context);
            initRequestToViewNameTranslator(context);
            initViewResolvers(context);
            initFlashMapManager(context);
         }
         ```

### 十九、Spring Mvc处理请求流程

1. 浏览器输入url: **localhost:8080/index?userName=eugene**
2. 进入DispacherServlet的doService方法
3. 处理分发调用**doDispatch**方法
4. 根据request获取mapping handler
5. 根据handler获取adapter
6. 处理interceptor
7. 处理adapter
8. 返回ModelAndView



