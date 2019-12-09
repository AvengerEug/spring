package com.eugene.sumarry.implementmapperscan.conf;

import com.eugene.sumarry.implementmapperscan.anno.MapperScan;
import com.eugene.sumarry.implementmapperscan.beans.MyImportBeanDefinitionRegistrar;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan("com.eugene.sumarry.implementmapperscan")
@MapperScan("com.eugene.sumarry.implementmapperscan.dao")
@Import( { MyImportBeanDefinitionRegistrar.class })
public class AppConfig {
}
