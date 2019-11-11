package com.eugene.sumarry.ioc.annotationtype;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    /**
     * @Autowired默认是根据byType的方式依赖注入, 若byType的类型的实例不止一个(内部把异常吃掉), 则会根据byName的方式来注入(也就是变成@Resource功能),
     * 此时是根据属性名来注入的, 它会将属性名首字母大写, 前面添加set关键字变成set方法, 然后利用反射调用set方法完成注入, 所以此时的 ***属性名*** 很重要,
     * 与自己添加的set方法无关. 所以此时的byName依赖注入方式与xml配置的byName又有差异, 因为xml配置依赖注入的byName方式是根据显示的set方法名决定的。
     *
     * 但是这个bean name得存在(spring的默认生成方式或者自己自定义name或者自己写一个bean name的生成规则)
     *
     * 重写spring的bean name生成规则步骤:
     *   见MyBeanNameGenerator类
     *
     * 因为UserDao的类型有多个, 所以@Autowired会退化成byName的方式,
     * 但是因为自定义了MyBeanNameGenerator类, 规则为类名首字母小写, 并在后面追加Eugene,
     * 所以需要将属性名改成 userDaoImpl1Eugene
     *
     */
    @Autowired
    private UserDao userDaoImpl1Eugene;

    @Autowired
    private String name;

    @Autowired
    private IndexDao indexDao;

}
