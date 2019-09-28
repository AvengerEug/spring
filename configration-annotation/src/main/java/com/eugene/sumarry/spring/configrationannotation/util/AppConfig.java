package com.eugene.sumarry.spring.configrationannotation.util;

import com.eugene.sumarry.spring.configrationannotation.model.Order;
import com.eugene.sumarry.spring.configrationannotation.model.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public User user() {
        return new User();
    }

    @Bean
    public Order order() {
        user();
        return new Order();
    }
}
