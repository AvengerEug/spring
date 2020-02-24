package com.eugene.sumarry.resourcecodestudy.autowiredbyconstructor;

import org.springframework.stereotype.Component;

@Component
public class AutowiredConstructor {

    private Bean1 bean1;
    private Bean2 bean2;


    public AutowiredConstructor(Bean1 bean1, Bean2 bean2) {
        this.bean1 = bean1;
        this.bean2 = bean2;
    }

}
