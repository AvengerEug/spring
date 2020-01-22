package com.eugene.sumarry.springmvc.uploadfile;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

@ComponentScan("com.eugene.sumarry.springmvc.uploadfile")
public class AppConfig {

    /**
     * 这个bean的名字必须叫multipartResolver
     * @return
     */
    @Bean
    public CommonsMultipartResolver multipartResolver() {
        return new CommonsMultipartResolver();
    }

}
