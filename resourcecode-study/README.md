## Spring源码学习

### BeanFactory和FactoryBean的区别
  * [Track to this](https://github.com/AvengerEug/spring/blob/develop/resourcecode-study/src/main/java/com/eugene/sumarry/resourcecodestudy/fbandbf/TestBeanFactory.java)

### AnnotationConfigApplicationContext
  * 注册单个bean(非java config类)
    1. 此时只是将该bean加入到spring容器中去, 走一遍bean的生命周期, 并且这个bean可以不用加任何注解
  
  * 注册配置类(java config类)
    1. java配置类可能会包含`@ComponentScan, @ImportResource`等注解, 所以要去解析并完成这些注解的功能