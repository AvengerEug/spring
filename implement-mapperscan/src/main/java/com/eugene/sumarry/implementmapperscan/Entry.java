package com.eugene.sumarry.implementmapperscan;

import com.eugene.sumarry.implementmapperscan.conf.AppConfig;
import com.eugene.sumarry.implementmapperscan.core.Application;

public class Entry {
    public static void main(String[] args) {

        Application.run(AppConfig.class);
    }
}
