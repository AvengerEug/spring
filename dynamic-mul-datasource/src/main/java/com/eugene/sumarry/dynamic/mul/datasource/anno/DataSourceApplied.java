package com.eugene.sumarry.dynamic.mul.datasource.anno;

import com.eugene.sumarry.dynamic.mul.datasource.Enum.DataSourceType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DataSourceApplied {

    DataSourceType value() default DataSourceType.MASTER;
}
