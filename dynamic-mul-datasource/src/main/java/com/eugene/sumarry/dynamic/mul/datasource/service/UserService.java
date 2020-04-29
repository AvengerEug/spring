package com.eugene.sumarry.dynamic.mul.datasource.service;

import com.eugene.sumarry.dynamic.mul.datasource.model.User;

import java.util.List;

public interface UserService {

    void add(User user);

    List<User> list();

    void mulCaseWithoutTransaction(User user);

    void mulCaseRequiredTransaction(User user);

    void mulCaseNotSupportedTransaction(User user);

    void mulCaseRequiresNewTransaction(User user);
}
