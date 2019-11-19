package com.eugene.sumarry.aop.studyannotation;


import java.lang.reflect.Field;

public class BuildUtils {

    public static String buildEntitySql(Object object) throws Exception {


        StringBuilder sb = new StringBuilder("INSERT INTO");
        // 获取参数的类对象
        Class clz = object.getClass();

        // 判断该类中是否有Entity注解
        if (clz.isAnnotationPresent(Entity.class)) {
            String name = ((Entity) clz.getAnnotation(Entity.class)).value();
            sb.append(" ").append(name).append(" (");
            Object values[] = new Object[clz.getDeclaredFields().length];
            for (int i = 0; i < clz.getDeclaredFields().length; i++) {
                Field field = clz.getDeclaredFields()[i];
                sb.append(field.getName());

                field.setAccessible(true);
                values[i] = field.get(object);

                if (i != clz.getDeclaredFields().length - 1) {
                    sb.append(", ");
                }
            }

            sb.append(") VALUES(");
            int i = 0;
            for (Object value : values) {
                sb.append(value.toString());
                if (i++ != values.length - 1) {
                    sb.append(", ");
                }
            }

            sb.append(")");

        }
        return sb.toString();
    }
}
