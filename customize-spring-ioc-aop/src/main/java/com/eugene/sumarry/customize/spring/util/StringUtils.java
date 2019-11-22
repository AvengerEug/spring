package com.eugene.sumarry.customize.spring.util;

public final class StringUtils {

    public static boolean hasLength(String str) {
        return (str != null && !str.isEmpty());
    }

    public static boolean hasText(String str) {
        return str != null && str.length() > 0;
    }
}
