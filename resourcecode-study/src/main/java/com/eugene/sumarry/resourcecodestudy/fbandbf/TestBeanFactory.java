package com.eugene.sumarry.resourcecodestudy.fbandbf;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.stereotype.Component;

/**
 * 继承了FactoryBean的类, 它是一个bean的工厂
 * 可以动态的配置bean的一些属性,
 * 一般是第三方jar包基于spring开发用的比较多，
 * eg: mybatis的SqlSessionFactoryBean这个类,
 * 这个类会构建sqlSessionFactory, 然后sqlSessionFactory会依赖于很多属性
 *
 * 作为一个集成spring的第三方jar包, 我们需要将bean添加到spring容器中
 * 这里有两种方式:
 *   第一种: 用户自己去装配这个bean, eg: ssm中需要配置数据源,
 *          我们需要在xml中配置很多Configuration
 *   第二种: 框架自己配置默认的。 eg: springboot集成mybatis的自动配置sqlSessionFactory的
 *          属性, 但是这个一般还需要自己配置数据源
 * 除了将bean加入到spring容器外, 加入这个bean的初始化过程有很多判断条件, 这时, FactoryBean的
 * 作用也体现出来了, 此时我们可以在getObject这个方法中对想要初始化的bean做处理
 *
 *
 * 注意:
 * testBeanFactory 这个bean的类型是getObject方法中返回的对象类型
 * &testBeanFactory 这个bean是TestBeanFactory类型而不是FactoryBean类型
 *
 * 所以当前对象的beanName是加了&符号的
 * getObject()返回的bean的name是当前类的名字(首字母变小写)
 *
 * BeanFactory  它是一个工厂, 能生产bean, 但本身不是一个bean
 * FactoryBean  它是一个工厂Bean, 是一个bean也能生产bean
 *
 * 什么时候使用FactoryBean?
 *   当一个bean的实例依赖于很多对象时, 可以使用FactoryBean简化步骤.
 *   只需提供一个简单的api即可, eg: mybatis的SqlSessionFactoryBean
 *
 * org.springframework.beans.factory.support.DefaultListableBeanFactory#preInstantiateSingletons()
 * 在该方法中指定了FactoryBean的命令会添加一个&
 * if (isFactoryBean(beanName)) {
 *      // 此处的FACTORY_BEAN_PREFIX就是&符号
 * 		Object bean = getBean(FACTORY_BEAN_PREFIX + beanName);
 * 		if (bean instanceof FactoryBean) {
 * 		    final FactoryBean<?> factory = (FactoryBean<?>) bean;
 * 		    ..............
 * 		}
 * 	}
 * 	else {
 * 		getBean(beanName);
 * 	}
 */
@Component
public class TestBeanFactory implements FactoryBean {

    @Override
    public Object getObject() throws Exception {
        return new TestBean();
    }

    @Override
    public Class<?> getObjectType() {
        return TestBean.class;
    }
}
