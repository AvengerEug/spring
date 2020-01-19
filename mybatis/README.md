## 一、一级缓存
  
  * 是针对于当前线程而言, 同一个线程而言, 可以共享一级缓存(但是集成spring后, 一级缓存失效了)
  
### 1.1 集成spring中一级缓存为什么会失效

### 1.2 一级缓存技术底层原理
  
## 二、二级缓存
  
  * 开启二级缓存: 在对应的mapper中添加 <cache /> 标签
  * 二级缓存是基于命名空间而言的, 若其他的命名空间对表中的数据进行了修改, 当前命名空间的缓存并不会更新, 
    它只会更新对应命名空间的缓存, 所以会出现数据不一致的问题。 原因就是: 多个命名空间操作更新同一个表,
    只有做更新操作的命名空间对应的缓存会及时更新。
    
    所以, 如果有多个命令空间会对同一张表做操作, 那么建议别选mybatis的二级缓存, 可以使用第三方的eg: redis
    
## 三、mybatis中添加mapper的几种方式
  1. class
     org.apache.ibatis.session.Configuration.addMapper(Class<T> type)
  2. package
     org.apache.ibatis.session.Configuration.addMappers(String packageName)
  3. url
  4. path
  
## 四、mybatis原理执行流程图

### 4.1 @MapperScan注解执行流程

![@MapperScan注解执行流程](https://github.com/AvengerEug/spring/blob/develop/mybatis/@MapperScan注解执行流程.png)

### 4.2 mybatis环境初始化流程

![@mybatis环境初始化流程](https://github.com/AvengerEug/spring/blob/develop/mybatis/mybatis环境初始化流程.png)

## 五、mybatis使用的是采用构造方法来实现对MapperFactoryBean的自动装配的
```java
    Constructor<?>[] ctors = determineConstructorsFromBeanPostProcessors(beanClass, beanName);
    if (ctors != null ||
            mbd.getResolvedAutowireMode() == RootBeanDefinition.AUTOWIRE_CONSTRUCTOR ||
            mbd.hasConstructorArgumentValues() || !ObjectUtils.isEmpty(args))  {
        return autowireConstructor(beanName, mbd, ctors, args);
    }
```

* 具体在于上述一段代码, 第一行是获取写到@Autowired注解的构造方法, 很显然, 
  MapperFactoryBean中无构造方法添加了@Autowired注解，
  主要是这个判断`mbd.hasConstructorArgumentValues()`, mybatis针对每个
  MapperFactoryBean都添加了一个构造方法会用上的值(dao层接口的class字符串)
  , 源码为如下
    ```java
      definition.getConstructorArgumentValues().addGenericArgumentValue(definition.getBeanClassName()); // issue #59
    ```

## 六、spring事务管理流程
  * @Transactional注解可以添加在类中也可以添加在方法中, 若添加在类中则对类中的所有方法
    都添加事务管理,
  * 要想添加事务需要做如下步骤:
     1. 开启事务管理, 在一个bean中添加@EnableTransactionManagement注解表示开启事务管理
     2. 添加一个事务管理器(也能自己自定义一个事务逻辑, 可以参考MyTransactionManager类)， 如下
        @Bean
        public PlatformTransactionManager platformTransactionManager() {
            DataSourceTransactionManager manager = new DataSourceTransactionManager();
            manager.setDataSource(dataSource());
            return manager;
        }
     3. 设置事务回滚的类名, 这里设置了是抛出了exception类型的异常就回滚

