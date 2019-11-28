package com.eugene.sumarry.customize.spring.beans;

/**
 * 包含
 *   懒加载(@Lazy),
 *   作用域(@Scope),
 *   描述(@Description),
 *   主要(@Primary)
 */
public interface BeanDefinition {

    String SCOPE_SINGLETON = "singleton";
    String SCOPE_PROTOTYPE = "prototype";

    void setParentName(String parentName);

    String getParentName();

    void setBeanClassName(String beanClassName);

    String getBeanClassName();

    void setScope(String scope);

    String getScope();

    void setLazyInit(boolean lazy);

    default boolean isLazyInit() {
        return false;
    }

    void setPrimary(boolean primary);

    boolean isPrimary();

    default boolean isSingleton() {
        return true;
    }

    default boolean isPrototype() {
        return false;
    }

    String setDescription(String description);

    String getDescription();
}
