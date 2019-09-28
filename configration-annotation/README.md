# @Configration注解作用及原理
```
  作用: 
    使用@Configration注解的类, spring在注册bean的时候会创建它的一个代理对象, 会对原来的对象中的属性、方法
    进行`增强`(所谓增强就是对原类进行了修改)。 最终目的就是让@Configuration注解修饰的类中的@Bean注解创建
    出来的对象都是单例的。
    如下代码所示, 有三种case:
    1. 假设AppConfig类是正常的new出来的(忽略所有的spring注解), 那么它在调用 实例对象.user(); 实例对象.order();
       两个方法时, 一定会调用两次user()方法, 进而产生两个user对象
    2. 假设不忽略spring的相关注解(@Configuration, @Bean)的话, 在spring初始化对象时会对AppConfig创建一个代理对象
       其中代理对象对代码进行了 '增强', 进而在调用user()和order()方法时, 也只会产生一个user对象
    3. 假设去掉@Configuration注解的话, 那么在spring初始化对象时不会对AppConfig对象创建代理对象, 而是普通对象,
       这样在调用user()和orders()方法时会创建两个User对象出来
```

```java
    @Configuration
    public class AppConfig {
    
        @Bean
        public User user() {
            return new User();
        }
    
        @Bean
        public Order order() {
            user();
            return new Order();
        }
    }
```

```txt
   探究原理:
     要想执行user()和order()方法, 那么一定会有AppConfig的对象
       => 何时产生了AppConfig对象?
     产生AppConfig对象后, 需要一个代理对象进行增强
       => 代理类何时/怎么产生的？
     
```