package com.eugene.sumarry.dynamic.mul.datasource.Enum;


public enum DataSourceType {

    MASTER(0), SLAVE(1);

    private int value;

    private DataSourceType(int value) {
        this.value = value;
    }

}
