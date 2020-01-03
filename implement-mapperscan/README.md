# 使用spring ImportBeanDefinitionRegistrar和FactoryBean知识模拟mybatis @MapperScan注解

## 一、背景

### 1.1 背景

  * @MapperScan功能: 指定添加了`@Mapper`注解的类的路径, 能够不写实现类就调用接口的方法
  
### 1.2 执行原理

[@MapperScan注解执行流程.png](https://github.com/AvengerEug/spring/blob/develop/implement-mapperscan/@MapperScan注解执行流程.png)

## 二、分析与解决方案

### 2.1 分析
  1. java 中接口是不能直接调用方法的, 得有实现类才行, 但是Mybatis可以不提供实现类也能调用接口中的方法,
     那要如何直接调用接口中的方法？ 代理
  2. 因为service层中会依赖与dao层, 所以我们需要将这个代理对象添加到spring容器中去, 让spring完成自动装配
     如何将创建出来的代理对象添加到spring容器中去？ => 如何手动的将一个类添加到spring容器中去？
     
### 2.2 解决方案
  * 针对第一个问题: 很明显, 可以使用动态代理模式动态产生一个对象
  * 针对第二个问题: 
    1. **ImportSelector**
       * => `此方案不可行`, 因为该接口是要根据类的全类型作为bean的name来创建bean的, 因为此时是接口, 所以不行
    2. **ImportBeanDefinitionRegistrar**接口
       * => `此方案可行`, 原理与下面的第五个方案类似, 因为此接口也提供了`BeanFactoryRegistry`, 可以手动添加BeanDefinition至工厂中
       * => [对应类https://github.com/AvengerEug/spring/tree/develop/implement-mapperscan/src/main/java/com/eugene/sumarry/implementmapperscan/beans/MyImportBeanDefinitionRegistrar](https://github.com/AvengerEug/spring/tree/develop/implement-mapperscan/src/main/java/com/eugene/sumarry/implementmapperscan/beans/MyImportBeanDefinitionRegistrar)
    3. 查看**DefaultListableBeanFactory类的api**
       * => `此方案不可行`, 无api直接添加BeanDefinition至bean工厂
    4. 使用**register**方法  
       * => `此方案不可行`, 因为register方法传入的是一个class, spring会根据class来new对象。因为MapperScan功能
         是扫描接口并动态生成实现类。 此时传入的是接口, spring无法帮忙new对象。
    5. 使用后置处理器的方式
       * => `方案可行`, 但这里只能通过**BeanDefinitionRegistryPostProcessor**类型的后置处理器来完成, 因为只有它  
            提供了`BeanFactoryRegistry`, 能手动添加BeanDefinition至bean工厂, **BeanFactoryPostProcessor**类型  
            的后置处理器没有提供这样的api
       * => [对应类https://github.com/AvengerEug/spring/tree/develop/implement-mapperscan/src/main/java/com/eugene/sumarry/implementmapperscan/beans/MapperScanBeanDefinitionRegistryPostProcessor](https://github.com/AvengerEug/spring/tree/develop/implement-mapperscan/src/main/java/com/eugene/sumarry/implementmapperscan/beans/MapperScanBeanDefinitionRegistryPostProcessor)
    
## 三、总结
  * 利用BeanDefinitionRegistryPostProcessor或ImportBeanDefinitionRegistrar接口来实现手动添加BeanDefinition至bean工厂
  * 利用spring的`FactoryBean`的`getObject`方法来实现将接口类型(无实现类)的代理对象添加到spring中去的, 因为FactoryBean的  
    getObject方法返回的是自己想要的对象
  * 利用spring的BeanDefinitionBuild类的api来创建一个基本的BeanDefinition, 并使用内置的ConstructorArgumentValues对象来  
    为带参构造器参数的提供
