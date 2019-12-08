# 使用spring ImportBeanDefinitionRegistrar和FactoryBean知识模拟mybatis @MapperScan注解

## 背景
  * @MapperScan功能: 指定添加了`@Mapper`注解的类的路径, 能够不写实现类就调用接口的方法

## 分析与解决思路

### 分析
  1. java 中接口是不能直接调用方法的, 得有实现类才行, 但是Mybatis可以不提供实现类也能调用接口中的方法,
     那要如何直接调用接口中的方法？ 代理
  2. 因为service层中会依赖与dao层, 所以我们需要将这个代理对象添加到spring容器中去, 让spring完成自动装配
     如何将创建出来的代理对象添加到spring容器中去？ => 如何手动的将一个类添加到spring容器中去？
     
     
### 解决思路
  1. 针对第一个问题: 很明显, 可以使用动态代理模式动态产生一个对象
  2. 针对第二个问题: **ImportSelector**接口或**ImportBeanDefinitionRegistrar**接口或查看**DefaultListableBeanFactory类的api**
