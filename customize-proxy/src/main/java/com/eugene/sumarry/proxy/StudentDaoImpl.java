package com.eugene.sumarry.proxy;

import java.util.Arrays;
import java.util.List;

public class StudentDaoImpl implements StudentDao {

    @Override
    public String getNameById(Long studentId) {
        System.out.println("===================StudentDaoImpl.getNameById===================");
        return "String: " + studentId;
    }

    @Override
    public List<Long> findIdList() {
        System.out.println("===================StudentDaoImpl.findIdList===================");
        return Arrays.asList(new Long[] {
                1L, 2L, 3L
        });
    }
}
