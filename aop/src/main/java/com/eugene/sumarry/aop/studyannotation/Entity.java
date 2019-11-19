package com.eugene.sumarry.aop.studyannotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE) // 可以作用的目标, type: 类, 接口, 枚举
//@Retention(RetentionPolicy.SOURCE)// RetentionPolicy.SOURCE 只保留在源码中, 当jvm编译成二进制文件后, 被该注解修饰的类会无这个注解
//@Retention(RetentionPolicy.CLASS) // 会保存在字节码中, 但是在运行中不会生效
@Retention(RetentionPolicy.RUNTIME) // 只有设置RUNTIME时, 才能在运行中某个类上有这个注解
public @interface Entity {
    String value() default "";
}
