package com.eugene.sumarry.dynamic.mul.datasource.common;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.stereotype.Component;

@Component
public class DynamicDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        System.out.println("当前真实数据源: " + DataSourceExchange.get());
        return DataSourceExchange.get();
    }
}
