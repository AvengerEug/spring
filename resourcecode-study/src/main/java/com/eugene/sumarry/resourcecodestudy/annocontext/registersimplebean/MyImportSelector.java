package com.eugene.sumarry.resourcecodestudy.annocontext.registersimplebean;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

public class MyImportSelector implements ImportSelector {

    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {

        if (importingClassMetadata.hasAnnotation(EnableProxy.class.getName())) {
            return new String[] {
                    UserDaoImplPostProcessor.class.getName()
            };
        }

        return new String[0];
    }
}
