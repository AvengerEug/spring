# 基于java config方式模拟spring ioc, aop

### 一、基础注解
  * `@ComponentScan`, `@ImportResource`

### 二、IOC, 支持注解和xml混合使用
  * 模拟spring 注解版本的IOC, 包括`@Autowired`, `@Qualifier`, `@Repository`, `@Service`
  
### 三、AOP
  * 模拟spring 注解版本的aop, 包括`@Aspect`, `@Before`, `@Pointcut`
  
### 四、目标

#### 4.1. 模拟spring执行主要顺序

#### 4.1.1 无参构造方法

  | 实现功能 | 状态 |
  | --- | --- |
  | 初始化bean工厂 | 已实现 |
  | 实例化`AnnotatedBeanDefinitionReader` | 已实现 |
  | 添加内置BeanDefinition(`RootBeanDefinition`)至bean工厂, 在Reader中完成 | 已实现|

#### 4.1.2 register方法
  | 实现功能 | 状态 |
  | --- | --- |
  | 将配置类(`AnnotatedGenericBeanDefinition`)添加至bean工厂 | 已实现 |
  
#### 4.1.2 refresh方法
  | 实现功能 | 状态 |
  | --- | --- |
  | 模拟`invokeBeanFactoryPostProcessors()`方法调用后置处理器流程 | 已实现ConfigClassPostProcessor类的调用 |
  
    