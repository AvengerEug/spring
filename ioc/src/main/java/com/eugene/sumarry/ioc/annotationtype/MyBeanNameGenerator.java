package com.eugene.sumarry.ioc.annotationtype;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;

/**
 * 自定义bean name 生成器
 *   1. 实现BeanNameGenerator接口
 *   2. 必须拥有默认构造器
 *   3. 在注解扫描注解中, 新增nameGenerator属性, 将自定义bean name生成器的全路径传入
 */
public class MyBeanNameGenerator implements BeanNameGenerator {

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
