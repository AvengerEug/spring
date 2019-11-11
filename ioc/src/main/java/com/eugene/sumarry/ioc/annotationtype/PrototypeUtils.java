package com.eugene.sumarry.ioc.annotationtype;

import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.stereotype.Component;

@Component
public abstract class PrototypeUtils {

    @Lookup
    public abstract BasicService getBasicService();

    @Lookup
    public abstract BasicService getBasicService(String name);
}
