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
        @Pointcut("execution(* com.eugene.sumarry.aop.main.dao..*.*(..))")
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
  5. 至此, 一个切面就完成了, 这个切面切了`com.eugene.sumarry.aop.main.dao`包及子包下的所有方法。

--- 
### 切点的表达式
  1. execution: 官网推荐的最常用的方式, 因为其粒度最低, 可以精确到具体的某一个方法
     ```java
        /**
        * 将com.eugene.sumarry.aop.main.dao包或子包下的任意参数、任意返回值的所有方法作为一个切点
        */
        @Pointcut("execution(* com.eugene.sumarry.aop.main.dao..*.*(..))")
        public void pointcutExecution() {
        }
     ```
  2. within: 只能切到类级别
     ```java
        /**
         * Within粒度比较大, 只能精确包下面的类
         */
        @Pointcut("within(com.eugene.sumarry.aop.main.dao.*)")
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
        @Pointcut("@args(com.eugene.sumarry.aop.main.annotation.AspectArgs)")
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
         * 表示当生成的代理对象的类型是com.eugene.sumarry.aop.main.daoproxy.UserDaoImpl时,
         * 改切点才会生效,
         *
         * 所以采用cglib代理生成的对象才会满足该切点的条件
         */
        @Pointcut("this(com.eugene.sumarry.aop.main.daoproxy.UserDaoImpl)")
        public void thisPointcut() {
        }
     ```
  
  8. target: 代理的目标对象类型与指定类型匹配后才被增强
     ```java
       /**
         * 表示当生成的代理对象的目标对象类型是com.eugene.sumarry.aop.main.daoproxy.UserDaoImpl时,
         * 该切点才会生效
         */
        @Pointcut("target(com.eugene.sumarry.aop.main.daoproxy.UserDaoImpl)")
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
        @After("pointcutExecution() && !execution(* com.eugene.sumarry.aop.main.dao..*.*(java.lang.String, java.lang.Integer)))")
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
     * 2. ("perthis(this(com.eugene.sumarry.aop.main.daoproxy.PrototypeDao))")
     *    表示当代理对象的类型是com.eugene.sumarry.aop.main.daoproxy.PrototypeDao
     *    时, 代理对象是原型的, 其它的为单例的
     * 3. 每一次获取PrototypeDao类型的bean的时候, 代理对象都是最新的.
     * 4. 当目标对象是单例的, 但是代理对象是原型的, 在ProceedingJoinPoint类中获取代理对象和目标对象都是最新的。
     */
    @Component
    @Aspect("perthis(this(com.eugene.sumarry.aop.main.daoproxy.PrototypeDao))")
    @Scope("prototype")
    public class PrototypeAspect {
    
        /**
         * 对com.eugene.sumarry.aop.main.daoproxy.PrototypeDao类下的所有方法进行增强,
         * 并且当代理对象的类型是com.eugene.sumarry.aop.main.daoproxy.PrototypeDao时,
         * 代理对象的scope为prototype
         */
        @Pointcut("execution(* com.eugene.sumarry.aop.main.daoproxy.PrototypeDao.*(..))")
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