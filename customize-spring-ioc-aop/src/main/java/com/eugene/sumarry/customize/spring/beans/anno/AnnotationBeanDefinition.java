package com.eugene.sumarry.customize.spring.beans.anno;

import com.eugene.sumarry.customize.spring.beans.CommonBeanDefinition;

import java.lang.annotation.Annotation;
import java.util.List;

public class AnnotationBeanDefinition extends CommonBeanDefinition {

    private List<Annotation> annotations;

    public List<Annotation> getAnnotations() {
        return annotations;
    }

    public void setAnnotations(List<Annotation> annotations) {
        this.annotations = annotations;
    }
}
