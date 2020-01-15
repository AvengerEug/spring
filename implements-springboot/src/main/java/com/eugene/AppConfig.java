package com.eugene;

import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * @EnableWebMvc
 * 导入了DelegatingWebMvcConfiguration的bean
 * 它是一个WebMvcConfigurationSupport的类型,
 * 同时也是ApplicationContextAware, ServletContextAware类型,
 *
 * 所以在执行ServletContextAware类型的处理器时, 要set一个ServletContext
 * 所以@EnableWebMvc注解的使用要保证spring容器中有ServletContext的bean
 *
 */
@ComponentScan("com.eugene")
@EnableWebMvc
public class AppConfig {

    /**
     * 有了web的启动环境, 这个类才能会回调上
     */
    @Component
    public class MyWebMvcConfigurer implements WebMvcConfigurer {
        @Override
        public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
            System.out.println("load FastJsonHttpMessageConverter----------------------------");
            FastJsonHttpMessageConverter fastJsonHttpMessageConverter = new FastJsonHttpMessageConverter();
            converters.add(fastJsonHttpMessageConverter);
        }
    }
}
