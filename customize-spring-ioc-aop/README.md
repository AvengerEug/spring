# 基于java config方式模拟spring ioc, aop

### 基础注解
  * `@ComponentScan`, `@ImportResource`

### IOC, 支持注解和xml混合使用
  * 模拟spring 注解版本的IOC, 包括`@Autowired`, `@Qualifier`, `@Repository`, `@Service`
  
### AOP
  * 模拟spring 注解版本的aop, 包括`@Aspect`, `@Before`, `@Pointcut`
  
### 目标
  * 集成spring ioc注入流程。`全局byType`, `局部byName`等