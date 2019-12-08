package com.eugene.sumarry.implementmapperscan.conf;

import com.eugene.sumarry.implementmapperscan.anno.MapperScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.eugene.sumarry.implementmapperscan")
@MapperScan("")
public class AppConfig {
}
