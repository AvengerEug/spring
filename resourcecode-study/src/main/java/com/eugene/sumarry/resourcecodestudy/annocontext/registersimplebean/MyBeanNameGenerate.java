package com.eugene.sumarry.resourcecodestudy.annocontext.registersimplebean;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.stereotype.Component;

/**
 * 自定义name生成器生效流程
 *
 * 1. ClassPathBeanDefinitionScanner扫描器对ComponentScan注解进行解析并对信息存储
 * 2. 在ComponentScanAnnotationParser类中的
 *    parse(AnnotationAttributes componentScan, final String declaringClass)方法中
 *    的这段代码进行重新setBeanNameGenerator
 *    boolean useInheritedGenerator = (BeanNameGenerator.class == generatorClass);
 *    scanner.setBeanNameGenerator(useInheritedGenerator ? this.beanNameGenerator :
 *    BeanUtils.instantiateClass(generatorClass));
 *
 *    若在@ComponentScan注解中传入的nameGenerator属性的值不是BeanNameGenerator.class对象的话,
 *    则使用系统传入的值
 * 3. 注意, 此时因为是用到了扫描中的包, 所以是修改ClassPathBeanDefinitionScanner扫描器的BeanNameGenerator
 *    但是呢, 这个扫描器是在方法中new出来的, 并没有修改上下文中的扫描器的BeanNameGenerator
 * 4. 同时在这个类的doScan(String... basePackages)中, 对非配置类进行了封装(变成DefinitionHolder)并添加至工厂的
 *    beanDefinitionMap和beanDefinitionNames中去
 */
@Component
public class MyBeanNameGenerate implements BeanNameGenerator {

    @Override
    public String generateBeanName(BeanDefinition beanDefinition, BeanDefinitionRegistry beanDefinitionRegistry) {
        // spring的bean name默认是全路径, 添加自己的规则, 获取类名, 第一个字母变小写, 并在最后面拼接Eugene,
        // 修改了之后, 需要修改配置自动装配模式为byType的地方
        String beanName = beanDefinition.getBeanClassName();

        String mysqlBeanName = beanName.substring(beanName.lastIndexOf(".") + 1);

        String result = mysqlBeanName.substring(0, 1).toLowerCase() + mysqlBeanName.substring(1);

        return result + "Eugene";
    }
}
