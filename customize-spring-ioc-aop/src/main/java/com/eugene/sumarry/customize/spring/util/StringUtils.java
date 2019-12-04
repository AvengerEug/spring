package com.eugene.sumarry.customize.spring.util;

public final class StringUtils {

    public static boolean hasLength(String str) {
        return (str != null && !str.isEmpty());
    }

    public static boolean hasText(String str) {
        return str != null && str.length() > 0;
    }

    public static String defaultBeanNameCreator(String classSimpleName) {
        String myBeanName = classSimpleName.substring(classSimpleName.lastIndexOf(".") + 1);

        myBeanName = myBeanName.substring(0, 1).toLowerCase() + myBeanName.substring(1);

        return myBeanName;
    }
}
