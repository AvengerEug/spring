package com.eugene.sumarry.resourcecodestudy.invokeBeanFactoryPostProcessor2.postprocessor;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.stereotype.Component;

@Component
public class UserDaoBeanFactoryPostProcessor implements BeanFactoryPostProcessor {

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		GenericBeanDefinition genericBeanDefinition = ((GenericBeanDefinition) beanFactory.getBeanDefinition("userDaoImpl"));
		genericBeanDefinition.getConstructorArgumentValues().addGenericArgumentValue("123");
	}
}
