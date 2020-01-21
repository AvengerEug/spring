package com.eugene.sumarry.servlet3dot0spi.initializer;


import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.HandlesTypes;
import java.util.Set;

@HandlesTypes(Eugene.class)
public class MyClassServletContainerInitializer implements ServletContainerInitializer {

    @Override
    public void onStartup(Set<Class<?>> c, ServletContext ctx) throws ServletException {
        for (Class<?> aClass : c) {
            System.out.println(aClass);
        }
        System.out.println("---------MyClassServletContainerInitializer-------");
    }
}
