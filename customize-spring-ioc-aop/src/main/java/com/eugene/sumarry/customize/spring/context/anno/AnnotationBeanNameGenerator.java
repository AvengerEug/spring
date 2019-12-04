package com.eugene.sumarry.customize.spring.context.anno;

import com.eugene.sumarry.customize.spring.beans.BeanDefinition;
import com.eugene.sumarry.customize.spring.beans.CommonBeanDefinition;
import com.eugene.sumarry.customize.spring.beans.RootBeanDefinition;
import com.eugene.sumarry.customize.spring.context.BeanNameGenerator;
import com.eugene.sumarry.customize.spring.util.StringUtils;

public class AnnotationBeanNameGenerator implements BeanNameGenerator {

    @Override
    public String generateBeanName(BeanDefinition beanDefinition) {
        String beanName = beanDefinition.getBeanClassName();

        if (beanDefinition instanceof RootBeanDefinition) {
            // spring的bean name默认是全路径, 添加自己的规则, 获取类名, 第一个字母变小写, 并在最后面拼接Eugene,
            // 修改了之后, 需要修改配置自动装配模式为byType的地方
            beanName = StringUtils.defaultBeanNameCreator(beanName);
        }

        return beanName;
    }
}
