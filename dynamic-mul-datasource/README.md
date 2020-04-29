## 无中间件实现动态数据源集成mysql主从复制，实现读写分离

## 一、使用到的技术点

* jdk的ThreadLocal功能
* spring-jdbc的路由数据源功能
* spring-context组件的aop功能

## 二、spring事务传播机制下的读写分离事务问题

* spring的事务传播机制如下：

    |   事务类型    |                             特点                             |          备注          |
    | :-----------: | :----------------------------------------------------------: | :--------------------: |
    |   REQUIRED    |    两个带有事务注解的方法共有，若事务不存在则创建一个新的    |          默认          |
    | NOT_SUPPORTED |                          不支持事务                          |                        |
    | REQUIRES_NEW  | 不管之前有没有事务，都会为开启一个新的事务，待新的事务执行完毕后，再执行之前的事务 |                        |
    |   MANDATORY   |             必须在有事务的情况下执行，否则抛异常             | 此类型依赖于已有的事务 |
    |     NEVER     |   必须在一个没有的事务中执行,否则抛出异常(与MANDATORY相反)   |                        |
    |   SUPPORTS    |    依赖于调用方是否开启事务，若调用方开启了事务则使用事务    |                        |
    
    因为`MANDATORY, NEVER, SUPPORTS`的事务体现都依赖于调用者，所以只测试`REQUIRED, NOT_SUPPORTED, REQUIRES_NEW`	三种情况下的事务问题，功能已经测试完成(可查看Entry类)，先做出总结：
    
    测试代码：
    
    ```java
    System.out.println(userDao.list());
    userDao.update(user);
    userDao.insert(user);
    System.out.println(userDao.list());
    int x = 1 / 0;
    ```
    |   事务类型    |                    读写操作使用数据源情况                    |          备注          |
    | :-----------: | :----------------------------------------------------------: | :--------------------: |
    |    无事务     | 读和写每次都会调用AbstractRoutingDataSource的determineCurrentLookupKey方法来获取数据源。所以读和写使用的都是自己的数据源 |                        |
    |   REQUIRED    | 尽管都执行了dao层的切面方法来设置ThreadLocal当前要使用数据源的key，但是在这种事务级别下，数据源的获取比dao的切面的执行还要前，也就是说在执行dao层切面之前就已经执行了AbstractRoutingDataSource的determineCurrentLookupKey方法来获取数据源了，后续所有的操作都是基于这一个数据源来的。 | 共用第一次获取的数据源 |
    | NOT_SUPPORTED | spring也只会获取一次数据源，但是是在执行dao代码时才会去获取数据源。所以读和写操作使用的数据源取决于第一个dao的切面往ThreadLocal中put进去的 |           无           |
    | REQUIRES_NEW  | 同REQUIRES |           无           |
    
    