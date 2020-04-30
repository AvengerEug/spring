package com.eugene.sumarry.dynamic.mul.datasource.service.impl;

import com.eugene.sumarry.dynamic.mul.datasource.dao.Student2Dao;
import com.eugene.sumarry.dynamic.mul.datasource.model.Student2;
import com.eugene.sumarry.dynamic.mul.datasource.service.Student2Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class Student2ServiceImpl implements Student2Service {

    @Autowired
    private Student2Dao student2Dao;

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void create(Student2 student2) {
        student2Dao.insert(student2);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void createWithException(Student2 student2) {
        student2Dao.insert(student2);
        throw new RuntimeException();
    }
}
