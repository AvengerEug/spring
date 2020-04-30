package com.eugene.sumarry.dynamic.mul.datasource.service;

import com.eugene.sumarry.dynamic.mul.datasource.model.Student1;

public interface Student1Service {

    void create(Student1 student1);

    void withoutAnyTransaction();

    void withRequiredTransaction();

    void withNotSupportedTransaction();

    void withRequiresNewTransaction();

    void withNestedTransaction();
}
