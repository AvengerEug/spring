package com.eugene.sumarry.aop.byAnnotation;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

@Configuration
/**
 * proxyTargetClass: true if the target class is to be proxied,
 * rather than the target class' interfaces. If this property value is set to true,
 * then CGLIB proxies will be created (but see also JDK- and CGLIB-based proxies).
 *
 * 默认是false, 所以默认使用的是jdk动态代理
 *
 * 设置成true 则使用cglib进行动态代理, 结果就是
 * 生成的代理对象是继承于目标对象的
 *
 * 设置成false, 则使用的jdk默认的动态代理, 结果就是
 * 生成的代理对象是实现于目标对象实现的接口的, 与目标对象的关联是: 实现于同一接口
 *
 * jdk动态代理是基于接口来生成代理对象的原因:
 *  因为jdk动态代理生成的对象中已经继承Proxy类了, 因为java的单继承特性, 所有只能采用实现接口的方式来产生代理对象
 */
@EnableAspectJAutoProxy(proxyTargetClass = true)
@Component
@ComponentScan("com.eugene.sumarry.aop.byAnnotation")
public class AppConfig {
}
