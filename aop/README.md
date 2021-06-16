## AOP知识点(基于java config的方式构建)

---
### 概念(concepts)
  1. 切面(Aspect): 包含切点、连接点、通知的一个集合
  2. 切点(Pointcut): 切入点, 要对代码增强的地方, 可以是整个类、整个类的整个方法、带某些特性的方法等等
  3. 连接点(Join point): 将切点和通知关联起来. 一个切点可以有多个连接点
  4. 通知(Advice): 需要增强的地方
  5. 目标对象(Target): 需要被代理的对象
  6. 代理对象(Proxy): 代理目标对象的对象

---
### 如何定义一个切面(spring 5.x)
  1. 启动AspectJ语法。添加`@EnableAspectJAutoProxy`注解(一般在项目入口添加)
     ```java
         @EnableAspectJAutoProxy(proxyTargetClass = true)
         @Component
         @ComponentScan(byAspectj)
         public class AppConfig {
         }
     ```
  2. 创建切面织入对象。添加一个类作为切面织入对象. 并将该`类添加至spring bean容器`中, 同时添加`@Aspect`注解
     ```java
        @Component
        @Aspect
        public class MyAspectj {
        }
     ```
    
  3. 创建切点。添加一个方法, 使用`@Pointcut`注解定义切点, 并使用对应的表达式添加织入点。包括: 
     `executation`, `within`, `args`, `this`, `target`, `@target`, `@args`, `@within`, `@annotation `
     具体可参考[官网](https://docs.spring.io/spring/docs/5.0.9.RELEASE/spring-framework-reference/core.html#aop-pointcuts-designators)
     ```java
        @Pointcut("execution(* com.eugene.sumarry.aop.byAnnotation.dao..*.*(..))")
        public void pointcutExecution() {
        }
     ```
  4. 创建连接点和通知。添加一个方法, 使用`@After`注解表示通知的类型(后置通知), 内部使用`pointcutExecution()`作为字符串表示该连接点与该方法对应的
     切点相关联
     ```java
        @After("pointcutExecution()")
        public void afterPointcutExecution() {
            System.out.println("afterPointcutExecution after advice");
        }
     ```
  5. 至此, 一个切面就完成了, 这个切面切了`com.eugene.sumarry.aop.byAnnotation.dao`包及子包下的所有方法。

---
### 切点的表达式
  1. execution: 官网推荐的最常用的方式, 因为其粒度最低, 可以精确到具体的某一个方法
     ```java
        /**
        * 将com.eugene.sumarry.aop.byAnnotation.dao包或子包下的任意参数、任意返回值的所有方法作为一个切点
        */
        @Pointcut("execution(* com.eugene.sumarry.aop.byAnnotation.dao..*.*(..))")
        public void pointcutExecution() {
        }
     ```
  2. within: 只能切到类级别
     ```java
        /**
         * Within粒度比较大, 只能精确包下面的类
         */
        @Pointcut("within(com.eugene.sumarry.aop.byAnnotation.dao.*)")
        public void pointcutWithin() {
        }
     ```
  3. args: 针对方法级别, 所有方法参数特性匹配规则都会被增强
     ```java
        /**
         * 精确到可扫描包的所有第一个参数为Integer的方法
         */
        @Pointcut("args(java.lang.Integer, ..)")
        public void pointcutArgs() {
        }
     ```
  4. @annotation: 针对注解, 带了指定注解类型的方法或者类都会被增强
     ```java
        /**
         * 定义了一个切点, 表示带了@AspectAnnotation注解的才会被增强
         */
        @Pointcut(studyannotation)
        public void pointcutAnnotation() {
        }
     ```
  5. @args: 针对方法中参数携带指定类型的注解时, 才会被增强
     ```java
        /**
         * 定义参数加了@args的方法才会增强的切点
         */
        @Pointcut("@args(com.eugene.sumarry.aop.byAnnotation.annotation.AspectArgs)")
        public void pointcutArgsAnnotation() {
        }
     ```

  6. @within: 某个类中添加了指定注解时, 该类中的所有方法会被增强
     ```java
        /**
         * 定义在aop环境下添加了注解的类中的所有方法会被增强
         */
        @Pointcut(AspectWithin)
        public void pointcutWithinAnnotation() {
        }
     ```
     
  7. this: 代理对象的类型与指定类型匹配后才被增强
     ```java
        /**
         * 表示当生成的代理对象的类型是com.eugene.sumarry.aop.byAnnotation.daoproxy.UserDaoImpl时,
         * 改切点才会生效,
         *
         * 所以采用cglib代理生成的对象才会满足该切点的条件
         */
        @Pointcut(impl)
        public void thisPointcut() {
        }
     ```
  
  8. target: 代理的目标对象类型与指定类型匹配后才被增强
     ```java
       /**
         * 表示当生成的代理对象的目标对象类型是com.eugene.sumarry.aop.byAnnotation.daoproxy.UserDaoImpl时,
         * 该切点才会生效
         */
        @Pointcut("target(com.eugene.sumarry.aop.byAnnotation.daoproxy.UserDaoImpl)")
        public void targetPointcut() {
        }
     ```

  9. 混合(条件)使用: 
     ```java
        /**
         * 对dao包及子包下的所有方法做增强, 除了第一个参数为string第二个参数为integer的方法
         *
         * 通知中里面执行的切点可以是对应的函数 也可以是表达式
         */
        @After("pointcutExecution() && !execution(* com.eugene.sumarry.aop.byAnnotation.dao..*.*(java.lang.String, java.lang.Integer)))")
        public void conditionAfter() {
            System.out.println("condition after");
        }
     ```

---
### AOP注意点
  1. `@EnableAspectJAutoProxy`注解中的`proxyTargetClass`属性
     * 默认为false, 使用的是jdk动态代理, `代理对象与目标对象的关系是`: **实现了同一个接口**
     * 设置为true, 则使用cglib生成代理对象, `代理对象与目标对象的关系是`: **继承**
  2. jdk 动态代理基于接口产生的原因:
     * jdk动态代理生成的代理类中已经`默认继承Proxy类`了, `由于java单继承的特性, 所有只能基于接口生成代理类了`

---
### 创建原型代理对象
  ```java
    /**
     * 1. @Scope("prototype")表示每次获取的代理对象都是原型的
     * 2. ("perthis(this(com.eugene.sumarry.aop.byAnnotation.daoproxy.PrototypeDao))")
     *    表示当代理对象的类型是com.eugene.sumarry.aop.byAnnotation.daoproxy.PrototypeDao
     *    时, 代理对象是原型的, 其它的为单例的
     * 3. 每一次获取PrototypeDao类型的bean的时候, 代理对象都是最新的.
     * 4. 当目标对象是单例的, 但是代理对象是原型的, 在ProceedingJoinPoint类中获取代理对象和目标对象都是最新的。
     */
    @Component
    @Aspect("perthis(this(com.eugene.sumarry.aop.byAnnotation.daoproxy.PrototypeDao))")
    @Scope("prototype")
    public class PrototypeAspect {
    
        /**
         * 对com.eugene.sumarry.aop.byAnnotation.daoproxy.PrototypeDao类下的所有方法进行增强,
         * 并且当代理对象的类型是com.eugene.sumarry.aop.byAnnotation.daoproxy.PrototypeDao时,
         * 代理对象的scope为prototype
         */
        @Pointcut("execution(* com.eugene.sumarry.aop.byAnnotation.daoproxy.PrototypeDao.*(..))")
        public void prototypePointcut() {
        }
    
        @Around("prototypePointcut()")
        public void testPrototypeAspect(ProceedingJoinPoint proceedingJoinPoint) {
            try {
                // this等同于proceedingJoinPoint.getThis()
                System.out.println("环绕前");
                System.out.println("代理对象hashcode: " + this.hashCode());
                System.out.println("目标对象hashcode: " + proceedingJoinPoint.getTarget().hashCode());
                proceedingJoinPoint.proceed();
                System.out.println("环绕后");
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
    }
  ```

---
### 强制将一个类转成毫无关系的类型
  ```java
    @Component
    @Aspect
    public class ProxyTypeAspect {
    
        /**
         * 会将StudentDaoImpl这个类 创建bean的时候变成userDao类型, 并将UserDaoImpl中的方法copy到StudentDaoImpl bean中去
         */
        @DeclareParents(value = "com.eugene.sumarry.aop.byAnnotation.daoproxy.StudentDaoImpl", defaultImpl = UserDaoImpl.class)
        private UserDao userDao;
    }
  ```
  * 在使用spring上下文获取`StudentDaoImpl`类型的bean时, 它会变成UserDao类型, 并将`UserDaoImpl`类的所有方法copy进去
  * 只能在切面中对类进行强制转类型, 具体可以参考下面xml配置版和上述注解版
   ```xml
    <?xml version="1.0" encoding="UTF-8"?>
    <beans xmlns="http://www.springframework.org/schema/beans"
           xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
           xmlns:aop="http://www.springframework.org/schema/aop"
           xsi:schemaLocation="
           http://www.springframework.org/schema/beans
           http://www.springframework.org/schema/beans/spring-beans.xsd
           http://www.springframework.org/schema/aop
           http://www.springframework.org/schema/aop/spring-aop.xsd" >
    
        <!-- 开启CGLIB代理 -->
        <aop:aspectj-autoproxy proxy-target-class="true"/>
    
        <aop:config>
            <aop:pointcut id="myPointcut" expression="execution(* com.eugene.sumarry.aop.byXML.*.*(..))"/>
    
            <!-- 切面绑定bean的原因是想用bean中的某个方法作为增强的逻辑 -->
            <aop:aspect id="myAspect" ref="xmlAdviceBean">
    
                <!-- 连接点 将切点和通知绑定, 前置通知 -->
                <aop:before method="before" pointcut-ref="myPointcut"/>
    
                <!-- 连接点 将切点和通知绑定, 后置通知 -->
                <aop:after method="after" pointcut-ref="myPointcut"/>
    
                <!-- xml版对某个类进行增强, 让它强制实现某个接口, 并将配置的默认实现类的方法copy到指定类中 -->
                <aop:declare-parents
                        types-matching="com.eugene.sumarry.aop.byXML.StudentDao"
                        implement-interface="com.eugene.sumarry.aop.byXML.Dao"
                        default-impl="com.eugene.sumarry.aop.byXML.UserDao"
                />
    
            </aop:aspect>
        </aop:config>
    
        <!-- 使用property属性注入 一定要有set方法, 当添加自动装配配置后, 可以省略property注入属性 -->
        <bean id="xmlAdviceBean" class="com.eugene.sumarry.aop.byXML.XMLAspectBean" autowire="byName"/>
        <bean id="userDao" class="com.eugene.sumarry.aop.byXML.UserDao"/>
        <bean id="studentDao" class="com.eugene.sumarry.aop.byXML.StudentDao"/>
    </beans>
   ```
  * 注意: 虽然类被强制实现了接口, 但是`copy的原生的逻辑而不是代理后增强的逻辑`, 同时要开启`cglib`代理才能实现这一功能

---
### Spring AOP和AspectJ的关系
  1. Spring AOP可以支持AspectJ的语法, 使用`@EnableAspectJAutoProxy`注解声明
  2. AspectJ是静态织入, 在编译成字节码文件时就被增强了。
     Spring AOP是运行时织入, 在运行时才对类进行增强

### Spring在代理对象在执行目标对象方法时，如何才能执行到代理对象的逻辑呢？

* 举个例子：我对目标对象ObjectA的methodA和methodB方法做了代理，然后我现在要在当前方法内部调用methodA方法，要求能执行到代理对象的methodB方法的逻辑。

* 通常的两个解决方案：

  ```txt
  1、在ObjectA中，依赖注入自己（注入的自己是一个代理对象）
  2、使用spring上下文中，获取ObjectA bean（因为这个bean是一个代理对象），
  ```

* 现在有一个比较nice的解决方案，其步骤如下：

  ```txt
  1、在@EnableAspectJAutoProxy注解中指定exposeProxy属性为true ---> 这代表着当代理对象要执行对应的方法时，若这个属性为true，则会把这个代理对象放到一个threadLocal中，这样我们就可以使用AopContext.currentProxy();方法获取到代理对象了。
  2、在methodA方法中调用((ObjectA)AopContext.currentProxy()).methodB(); 方法即可。但前提是：调用methodA和methodB方法时，得位于同一个线程才行，因为代理对象是 暴露在 线程级别的。跨线程后，无法访问了。
  ```

  这个比较nice的解决方案，还有一个

### spring 内部aop的实现采用的是动态代理 + 责任链设计模式实现的

* spring aop的返回通知是什么？ 根据源码中的设计来讲，如果目标方法抛了异常，那么是不会执行返回通知的逻辑的（详细看AfterRunningAdviceInterceptor.java类）。原因是在责任链的顶端有一个检验抛异常的链A，因为返回通知是在执行外目标方法后执行完的，如果在执行目标方法的过程中抛异常了，那么会被链A给捕捉到，进而无法执行到**返回通知**
* 整个责任链的链路过程是这样的：ExposeInvocationInterceptor -> AspectJAfterThrowingAdvice（异常通知） -> AfterRunningAdviceInterceptor（内部对目标方法的逻辑做了try catch的逻辑） -> AfterReturnAdvice（在执行目标方法后，同步执行**返回通知**逻辑，但由于是责任链的设计模式，如果此链路执行目标方法时发生了异常，那么就会向上抛，最终会被AfterRunningAdviceInterceptor捕捉到）-> AspectJAfterAdvice（在此链路上执行了try finally的逻辑，而finally中执行的就是后置通知，因此我们的后置通知一定会被执行。） -> MethodBeforeAdvice（执行before的逻辑） 
* 异常通知和返回通知是互斥的，有异常通知就不会有返回通知，有返回通知就不会有异常通知

### spring aop代理方式推断

* 默认使用的是jdk动态代理的方式。若目标类没有实现接口，或者在@EnableAspectJAutoProxy注解中有指定使用的是cglib类，则会使用cglib代理
* spring的aop后置处理器(InstantiationAwareBeanPostProcessors)做了哪些事情：
  * before：找到增强器（eg：@Before、@After、返回通知、异常通知等等）
  * after：创建代理对象，把我们的增强器放到代理对象中去

### spring 事务机制

* @Transaction注解不管是添加到方法上、类上、接口上、接口定义的方法上，事务都能生效。其主要原因是spring在扫描@Transaction注解时有这么一个寻找顺序：**实现类的方法 -> 实现类 -> 接口的方法 -> 接口**。即先从实现类的方法上面找，然后再到实现类上找，然后再到接口的方法上找，最后到接口上找@Transaction注解

* spring的事务中，有一个叫**TransactionSynchronizationManager**的事务同步管理器，其内部的核心就是存在非常多的ThreadLocal变量，比如：当前线程绑定的事务名称、事务隔离级别、事务同步器（可以添加事务各个生命周期添加对应的钩子函数）等数据。但需要注意的是，它是线程级别的，如果跨线程了，这些东西就都获取不到了
* spring的事务执行过程：@EnableTransactionManagement注解中导入了ProxyTransactionManagementConfiguration和AutoProxyRegistrar（假设为jdk动态代理的方式），而ProxyTransactionManagementConfiguration内部导入了BeanFactoryTransactionAttributeSourceAdvisor、TransactionAttributeSource、TransactionInterceptor。而AutoProxyRegistrar导入了apo的创建器：InfrastructureAdvisorAutoProxyCreator。其中，BeanFactoryTransactionAttributeSourceAdvisor是事务的增强器（这里比aop要好，aop需要循环所有的类找到对应的切面，并把切面对应的增强行为的函数都封装成增强器，而这里是直接定义了），TransactionAttributeSource是事务属性资源，保存的是事务相关的属性，最后的TransactionInterceptor就是事务的拦截器了，最终会执行到这个拦截器，在这个拦截器内部再使用BeanFactoryTransactionAttributeSourceAdvisor来执行对应的事务逻辑以及TransactionAttributeSource来保存事务相关的属性。

### 目标：写一个spring aop系列的博客，内容包含：aop的使用方式（before、after、返回通知、异常通知的执行结果和顺序）、spring是如何找到切面以及对应的切面增强方法并把它们转化成一系列的拦截器、spring aop的执行顺序、spring aop事务的使用案例（以传播机制为REQUIRED_NEW为例子）来讲解spring 事务的执行顺序。一共：4篇文章。