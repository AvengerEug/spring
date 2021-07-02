# Spring事务使用篇：学习spring事务传播机制的7种姿势

## 前言

* 在上篇[spring aop原理篇：我用上我的洪荒之力来帮你彻底了解aop注解@EnableAspectJAutoProxy的原理](1)的文章中，我们熟悉了Spring如何找到切面、如何找到通知、如何生成代理对象以及代理对象的执行顺序。现在，我们再来学习下Spring事务相关的知识点，这篇文章比较简单，适合入门，主要来学习下如何使用Spring的事务以及相关传播机制的特性。

### 一、以测试用例的方式认识Spring的事务机制

* 案例背景：以支付系统的转账业务为例，我们的转账业务一定是一个原子性的操作。A向B转账，A的账户扣钱、B的账户加钱，两者要么一起成功要么一起失败。

#### 1.1 预览测试案例项目结构

* 项目入口Entry.java

  ```java
  public class Entry {
  
      public static void main(String[] args) {
          AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
          TransferService transferService = context.getBean(TransferService.class);
          // 调用转账接口，avengerEug向zhangsan转账1元
          transferService.transfer("avengerEug", "zhangsan", new BigDecimal("1"));
      }
  
      @ComponentScan("com.eugene.sumarry.transaction")
      @Configuration
      @EnableTransactionManagement
      @EnableAspectJAutoProxy(exposeProxy = true)
      public static class AppConfig {
  
          @Bean
          public DataSource dataSource() {
              DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
              driverManagerDataSource.setUrl("jdbc:mysql://127.0.0.1:3306/transaction_test?useUnicode=true&characterEncoding=utf-8&useSSL=false&allowMultiQueries=true");
              driverManagerDataSource.setUsername("root");
              driverManagerDataSource.setDriverClassName("com.mysql.jdbc.Driver");
              driverManagerDataSource.setPassword("");
              return driverManagerDataSource;
          }
  
          @Bean
          public PlatformTransactionManager platformTransactionManager(DataSource dataSource) {
              DataSourceTransactionManager manager = new DataSourceTransactionManager();
              manager.setDataSource(dataSource);
              return manager;
          }
  
          @Bean
          public JdbcTemplate jdbcTemplate(DataSource dataSource) {
              JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
              return jdbcTemplate;
          }
  
      }
  }
  ```

* 转账接口Transfer.java

  ```java
  public interface TransferService {
  
      // 转账操作
      void transfer(String outAccountId, String inAccountId, BigDecimal amount);
  
      // 加钱操作
      void incrementAmount(String accountId, BigDecimal amount);
  
  }
  ```

* 转账接口实现类TransferImpl.java

  ```java
  @Component
  public class TransferServiceImpl implements TransferService {
  
      @Autowired
      private JdbcTemplate jdbcTemplate;
  
      @Override
      public void transfer(String outAccountId, String inAccountId, BigDecimal amount) {
          // 进钱
          this.incrementAmount(inAccountId, amount);
  
          // ...... 可以增加各种其他业务逻辑  @extension1
          
          // 出钱
          jdbcTemplate.update("UPDATE account SET amount = amount - ? WHERE id = ?", amount, outAccountId);
          
          // ...... 可以增加各种其他业务逻辑  @extension2
      }
  
      @Override
      public void incrementAmount(String accountId, BigDecimal amount) {
          // 不考虑任何并发情况，直接新增金额
          jdbcTemplate.update("UPDATE account SET amount = amount + ? WHERE id = ?", amount, accountId);
      }
  }
  ```

* 数据库 & 数据库表数据初始化脚本

  ```mysql
  # 创建数据库
  CREATE DATABASE transaction_test;
  
  # 使用数据库
  USE transaction_test;
  
  # 创建表
  CREATE TABLE account(
    id VARCHAR(255) PRIMARY KEY,
    amount Decimal NULL
  );
  
  # 初始化数据
  INSERT INTO account(id, amount) VALUES
  ('avengerEug', 100),
  ('zhangsan', 20);
  ```

  

* 项目结构分析：

  > 首先，在Entry类中定义了一个Spring的配置类**AppConfig**。在这个配置类中，我们指定了spring应用的扫描路径、开启了事务、开启了AOP功能并暴露了代理对象、指定了DataSource、添加了事务管理器以及初始化了JdbcTemplate。
  >
  > 其次，定义了转账接口Transfer.java，以此接口来模拟转账业务。
  >
  > 最后，提供了数据库 & 数据库表数据初始化脚本，方便快速搭建数据库环境。
  >
  > 
  >
  > 在**项目入口Entry.java**类中调用了transfer转账接口方法，主要是模拟avengerEug账户向zhangsan账户转账的这么一个操作。
  

#### 1.2 使用@Transactional注解，为转账接口开启事务功能

* 在上述的案例中，我们的转账接口是不具备事务功能的，如果我在`@extension1`处添加了其他业务逻辑的处理，并且在处理这些业务逻辑的过程中抛了异常。那么会出现数据库**inAccountId的钱增加了，但是outAccountId的钱没有减少的情况**。上面有分析到，转账操作是一个原子性操作。为了达到这个目的，我们可以在转账接口添加@Transactional注解来告诉Spring：我这个转账接口需要开启事务功能，后续的事务开启、事务提交、事务回滚将由spring来完成。

* 改造**TransferServiceImpl.java**类，其代码如下所示：

  ```java
  @Component
  public class TransferServiceImpl implements TransferService {
  
      @Autowired
      private JdbcTemplate jdbcTemplate;
  
      @Transactional(rollbackFor = Exception.class)  // @1
      @Override
      public void transfer(String outAccountId, String inAccountId, BigDecimal amount) {
          // 进钱
          this.incrementAmount(inAccountId, amount);
  
          // ...... 可以增加各种其他业务逻辑  @extension1
          
          // 出钱
          jdbcTemplate.update("UPDATE account SET amount = amount - ? WHERE id = ?", amount, outAccountId);
          
          // ...... 可以增加各种其他业务逻辑  @extension2
      }
  
      @Override
      public void incrementAmount(String accountId, BigDecimal amount) {
          // 不考虑任何并发情况，直接新增金额
          jdbcTemplate.update("UPDATE account SET amount = amount + ? WHERE id = ?", amount, accountId);
      }
  }
  ```

  `@1`处的代码表示：transfer接口是一个原子性接口，Spring在执行transfer接口之前会向MySQL提交一个开启事务的指令。若transfer接口中抛出了Exception类型的异常，Spring捕捉异常后会向MySQL发送一个回滚操作的指令，否则发送提交事务的指令。我们可以把执行过程简单的理解成下列所示的伪代码：

  ```java
  try {
      // 开启事务
      begin transaction;
      
      // 执行转账接口
      transfer();
      
      // 提交事务
      commit transaction;
  } catch(Exception ex) {
      // 捕获transfer逻辑执行的异常，执行回滚操作
      rollback transaction;
  }
  ```

* 接下来我们以上述**TransferServiceImpl.java**类的代码为标准，新增几种测试用例，来认识spring的事务功能。

##### 1.2.1 测试用例1：直接执行Entry.java的main方法

* 在正常情况下，transfer接口内部不会抛出异常，因此转账业务是能执行成功的。测试用例1的执行结果为：**avengerEug的账户往zhangsan的账户转账1块钱  ==> 执行成功，avengerEug账户增加了一块钱，zhangsan账户减少了一块钱**

##### 1.2.1 测试用例2：改造TransferServiceImpl，在@extension1处模拟抛出异常

* 其改动后的代码如下所示：

  ```java
  @Component
  public class TransferServiceImpl implements TransferService {
  
      @Autowired
      private JdbcTemplate jdbcTemplate;
  
      @Transactional(rollbackFor = Exception.class)  // @1
      @Override
      public void transfer(String outAccountId, String inAccountId, BigDecimal amount) {
          // 进钱
          this.incrementAmount(inAccountId, amount);
  
  	    int result = 1 / 0;   // @extension1
          
          // 出钱
          jdbcTemplate.update("UPDATE account SET amount = amount - ? WHERE id = ?", amount, outAccountId);
          
          // ...... 可以增加各种其他业务逻辑  @extension2
      }
  
      @Override
      public void incrementAmount(String accountId, BigDecimal amount) {
          // 不考虑任何并发情况，直接新增金额
          jdbcTemplate.update("UPDATE account SET amount = amount + ? WHERE id = ?", amount, accountId);
      }
  }
  ```

  很明显，在@extension1处会抛出一个**算术异常**，因此测试用例2的执行结果为：**avengerEug向zhangsan转账1元 --> 失败，avengerEug账户不会扣钱，zhangsan账户也不会加钱**。

* 熟悉上述的两个测试用例后，相信你已经对spring的事务有所了解了，接下来我们来认识下Spring的事务传播机制。

### 二、认识Spring的事务传播机制

* 这里穿插一个知识点：**Spring的事务传播机制与MySQL的事务隔离机制是两个完全不一样的东西，两者没有直接关系。**简单理解的方式为：**Spring的事务传播机制是在方法调用栈中，如果有多个方法具有事务功能时，来确认它们是使用`同一个事务`还是使用`独立的事务`或者`其他策略`等等。** 如下图所示：

  ![Spring事务传播机制含义.png](Spring事务传播机制含义.png)
  
* 那Spring有哪些事务传播机制呢？我们查看下**org.springframework.transaction.annotation.Propagation**枚举，在源码中一共定义了7中事务传播机制，他们分别有什么样的特性呢？我将以**不同的姿势（测试案例）**来带大家认识他们。

* 继续改造**TransferServiceImpl.java**类，其模板代码如下所示：

  ```java
  @Component
  public class TransferServiceImpl implements TransferService {
  
      @Autowired
      private JdbcTemplate jdbcTemplate;
  
      @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
      @Override
      public void transfer(String outAccountId, String inAccountId, BigDecimal amount) {
          // 进钱
  	   ((TransferService) AopContext.currentProxy()).incrementAmount(inAccountId, amount);
  
          // ...... 可以增加扩展代码  @1
  
          // 出钱
          jdbcTemplate.update("UPDATE account SET amount = amount - ? WHERE id = ?", amount, outAccountId);
  
          // ...... 可以增加扩展代码  @2
      }
  
      // @3
      @Override
      public void incrementAmount(String accountId, BigDecimal amount) {
          // 不考虑任何并发情况，直接新增金额
          jdbcTemplate.update("UPDATE account SET amount = amount + ? WHERE id = ?", amount, accountId);
          
          // ...... 可以增加扩展代码  @4
      }
  }
  ```

  在上述模板中指定了transfer接口开启了事务功能，并且指定事务传播机制为**REQUIRED**。同时，还定义了4个扩展点，其中**@3**是定义方法的事务特性，**@1、@2、@4**是测试抛异常的位置。接下来将设置**incrementAmount**方法的传播机制为**REQUIRED_NEW**的情形进行测试（剩下的6个传播机制可以参考相应的测试用例）

#### 2.1 将incrementAmount方法设置REQUIRED_NEW的事务传播机制

* 改造**TransferServiceImpl.java**类，其代码如下所示：

  ```java
  @Component
  public class TransferServiceImpl implements TransferService {
  
      @Autowired
      private JdbcTemplate jdbcTemplate;
  
      @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
      @Override
      public void transfer(String outAccountId, String inAccountId, BigDecimal amount) {
          // 进钱
         ((TransferService) AopContext.currentProxy()).incrementAmount(inAccountId, amount);
  
          // ...... 可以增加扩展代码  @1
  
          // 出钱
          jdbcTemplate.update("UPDATE account SET amount = amount - ? WHERE id = ?", amount, outAccountId);
  
          // ...... 可以增加扩展代码  @2
      }
  
      @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW) // @3
      @Override
      public void incrementAmount(String accountId, BigDecimal amount) {
          // 不考虑任何并发情况，直接新增金额
          jdbcTemplate.update("UPDATE account SET amount = amount + ? WHERE id = ?", amount, accountId);
          
          // ...... 可以增加扩展代码  @4
      }
  }
  ```

  在`@3`处指定了事务的传播机制为**REQUIRES_NEW**

##### 2.1.1 测试用例1：在@1处模拟抛出异常

* 在`@1`添加如下代码：

  ```java
  int result = 1 / 0;
  ```

  并继续执行Entry.java的main方法。你会发现zhangsan账户的钱加了，**但是avengerEug账户的钱没扣**。

* 有人可能会问：这与我们**1.2.1 测试用例2**上说的不一样呀，内部抛出了异常，为什么不会同时回滚？这是因为在执行的过程中，我们虽然是从具有事务特性的transfer方法内部调用了具有事务特性的**incrementAmount**方法，但**incrementAmount**方法的事务传播机制是**REQUIRES_NEW**，Spring在执行的过程中会为**incrementAmount**方法`单独开启一个事务`。而当我们在`@1`处抛出异常时，**incrementAmount**方法的事务已经提交了。因此，在这种情况下是不会影响到**incrementAmount**方法的执行结果的。

##### 2.1.2 测试用例2：在@2处模拟抛异常

* 在`@2`处添加如下代码：

  ```java
  int result = 1 / 0;
  ```

  其运行结果与**测试用例1**的结果一致。

##### 2.1.3 测试用例3：在@4处模拟抛异常

* 在`@4`处添加如下代码

  ```java
  int result = 1 / 0;
  ```

  并继续执行Entry.java的main方法。你会发现zhangsan账户的钱没有加，avengerEug账户的钱也没有减。原因就是：**incrementAmount**方法内部抛出的异常，在incrementAmount方法中开启的事务也回滚了。但由于没有对这个异常做捕获，因此这个异常会抛向transfer方法，而transfer接口也没有对异常做任何处理，最终异常会向上抛。这个时候spring就会感知到异常，进而会对transfer方法做回滚操作，所以会出现zhangsan账户的钱没有加，avengerEug账户的钱也没有减的情况。

#### 2.2 其他的6种事务传播机制特性

* 上面总结到**REQUIRES_NEW**传播机制的特性，并从三个测试用例的视角来了解其相关的特性。由于文章篇幅有限，这里已经总结好spring的7种事务传播机制的特性了，大家可以套用上述的测试案例的模板来验证剩下的6种传播机制。

  | 事务传播机制类型 |                             特点                             | 举例                                                         |
  | :--------------: | :----------------------------------------------------------: | :----------------------------------------------------------- |
  |     REQUIRED     |    两个带有事务注解的方法共用，若事务不存在则创建一个新的    | 条件：下游方法的事务传播机制为REQUIRED。<br/>案例：若上游方法有事务，则共用一个事务。否则自己开启一个事务 |
  |  NOT_SUPPORTED   |                          不支持事务                          | 条件：下游方法的事务传播机制为NOT_SUPPORTED。<br/>案例：若上游方法开启了事务，就算下游方法内部抛了异常，上游方法也不会回滚事务。 |
  |   REQUIRES_NEW   | 不管之前有没有事务，都会开启一个新的事务，待新的事务执行完毕后，再执行之前的事务 | 条件：下游方法的事务传播机制为REQUIRES_NEW时。<br/>案例：<br/>1、不管上游方法有没有事务，自己都会开启一个独立的事务。 |
  |    MANDATORY     |             必须在有事务的情况下执行，否则抛异常             | 条件：下游方法的事务传播机制为REQUIRES_NEW。<br>案例：<br/>1、若上游方法未开启事务，则抛异常<br/>2、若上游方法开启事务，则正常执行 |
  |      NEVER       |   必须在一个没有事务中执行，否则抛出异常(与MANDATORY相反)    | 条件：下游方法的事务传播机制为NEVER时。<br>案例：<br>1、若上游方法开启了事务，则抛异常<br>2、若上游方法没有开启事务，则正常执行 |
  |     SUPPORTS     |    依赖于调用方是否开启事务，若调用方开启了事务则使用事务    | 与NOT_SUPPORTED相反。<br>条件：下游方法的事务传播机制为SUPPORTED。<br>案例：<br>1、若上游方法无事务，则自己也无事务。<br>2、若上游方法有事务，则自己也有事务。 |
  |      NESTED      | 如果当前存在事务，则在嵌套事务内执行。如果当前没有事务，则会开启事务 | 条件：下游方法的事务传播机制为NESTED。<br>案例：<br>1、若上游方法开启了事务，在执行下游方法时，下游方法会开启一个子事务，当下游方法的事务是否提交依赖于上游方法的操作。如果上游方法执行了提交事务操作，此时会将子事务也提交，若上游方法执行了回滚操作，也类似。<br>2、若上游方法没有开启事务，在执行下游方法时，下游方法会自己开一个事务。此时类似于**REQUIRES_NEW** |
  
  表格中说的上游方法和下游方法的概念，拿上面说的转账例子来说：上游方法就是**transfer**，下游方法就是：**incrementAmount**。即下游方法是在上游方法内部被调用的。

### 三、总结

* **Spring的事务使用姿势以及传播机制特性，你学会了吗？**
* **如果你觉得我的文章有用的话，欢迎点赞、收藏和关注。:laughing:**
* **I'm a slow walker, but I never walk backwards**

