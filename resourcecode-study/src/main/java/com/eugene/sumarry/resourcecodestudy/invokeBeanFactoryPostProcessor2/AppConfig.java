package com.eugene.sumarry.resourcecodestudy.invokeBeanFactoryPostProcessor2;


import com.eugene.sumarry.resourcecodestudy.invokeBeanFactoryPostProcessor2.anno.EnableProxy;
import com.eugene.sumarry.resourcecodestudy.invokeBeanFactoryPostProcessor2.anno.ImportEugene;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 以java config的方式启动spring  => 全配置类
 */
@EnableProxy // 为UserServiceImpl类添加代理类的开关
@ImportEugene // 导入ImportEugeneImportSelector后置处理器的开关
@Configuration
@ComponentScan("com.eugene.sumarry.resourcecodestudy.invokeBeanFactoryPostProcessor2")
public class AppConfig {

    /**
     * AppConfig中添加了一个配置类
     * @return
     */
    @Bean
    public TestBeanInAppConfig testDao() {
        return new TestBeanInAppConfig();
    }
}
