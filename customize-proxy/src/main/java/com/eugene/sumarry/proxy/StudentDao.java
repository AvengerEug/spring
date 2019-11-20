package com.eugene.sumarry.proxy;

import java.util.List;

public interface StudentDao {

    String getNameById(Long studentId);

    List<Long> findIdList();

}
