package com.eugene.sumarry.dynamic.mul.datasource.common;

import com.eugene.sumarry.dynamic.mul.datasource.Enum.DataSourceType;

public class DataSourceExchange {

    private static ThreadLocal<DataSourceType> handler = new ThreadLocal<DataSourceType>() {
        /**
         * 当调用ThreadLocal.get方法时，或里面没有东西，则会调用initialValue方法
         * @return
         */
        @Override
        protected DataSourceType initialValue() {
            return DataSourceType.MASTER;
        }
    };

    public static void put(DataSourceType dataSourceType) {
        handler.set(dataSourceType);
    }

    public static DataSourceType get() {
        return handler.get();
    }

    public static void clear() {
        handler.remove();
    }

}
