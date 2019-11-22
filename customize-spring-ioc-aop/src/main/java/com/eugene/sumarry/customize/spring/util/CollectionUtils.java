package com.eugene.sumarry.customize.spring.util;

import java.util.Map;

public final class CollectionUtils {

    public static boolean isEmpty(Map<?, ?> map) {
        return (map == null || map.isEmpty());
    }
}
