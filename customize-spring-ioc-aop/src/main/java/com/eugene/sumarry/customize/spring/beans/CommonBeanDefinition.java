package com.eugene.sumarry.customize.spring.beans;

public abstract class CommonBeanDefinition implements BeanDefinition {

    private String parentName;
    private String beanClassName;
    private String scope;
    private boolean lazy;
    private boolean primary;
    private String description;
    private Class<?> beanClass;

    @Override
    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    @Override
    public String getParentName() {
        return parentName;
    }

    @Override
    public void setBeanClassName(String beanClassName) {
        this.beanClassName = beanClassName;
    }

    @Override
    public String getBeanClassName() {
        return this.beanClassName;
    }

    @Override
    public void setScope(String scope) {
        this.scope = scope;
    }

    @Override
    public String getScope() {
        return scope;
    }

    @Override
    public void setLazyInit(boolean lazy) {
        this.lazy = lazy;
    }

    @Override
    public void setPrimary(boolean primary) {
        this.primary = primary;
    }

    @Override
    public boolean isPrimary() {
        return this.primary;
    }

    @Override
    public String setDescription(String description) {
        return this.description = description;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public boolean isLazyInit() {
        return this.lazy;
    }

    @Override
    public boolean isSingleton() {
        return SCOPE_SINGLETON.equals(getScope());
    }

    @Override
    public boolean isPrototype() {
        return SCOPE_PROTOTYPE.equals(getScope());
    }

    @Override
    public Class<?> getBeanClass() {
        return beanClass;
    }

    @Override
    public void setBeanClass(Class<?> beanClass) {
        this.beanClass = beanClass;
    }
}
