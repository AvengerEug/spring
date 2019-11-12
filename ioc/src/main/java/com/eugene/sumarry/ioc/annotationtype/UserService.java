package com.eugene.sumarry.ioc.annotationtype;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
     * byName的几种情况:
     *   1. xml配置的byName, 会根据set方法来注入
     *   2. @Resource注解的byName, 会根据属性名(其实这个属性名就是bean的名字),
     *      这个属性名又分是注解中的属性名还是变量名. 总而言之, 不管是@Resource注解中的
     *      name属性名还是要依赖注入的变量名, 在@Resource的byName方式下, 这个名字一定
     *      就是bean的名字
     *   3. @Autowired注解当注入的类型有多个时, 会退化成@Resource的功能
     *
     * byType注入类型当有多个相同类型bean的时候, 可以采用如下几种方法(不考虑xml配置的情况)解决:
     * 1. 将属性名设置成 要注入bean的名字(原理是会降级成@Resource注入模式, 即下述的第三点)
     * 2. 在一个bean中添加@Primary注解, 表示当遇到多个类型的时候, 使用此bean进行注入
     * 3. 修改成@Resource注解, 添加存在bean的set方法或者注解中添加bean的名称
     * 4. @Autowired和@Qualifier结合使用, 并在@Qualifier注解中添加指定注入bean的name
     *
     *
     * spring 单例与单例之间的循环引用是ok的, 但是如果相互引用的bean中有原型的scope对象的话, 那么会报错
     */
    @Autowired
    private UserDao userDaoImpl1Eugene;

    @Autowired
    private String name;

    @Autowired
    private IndexDao indexDao;

    @Autowired
    private PrototypeUtils prototypeUtils;

    public void sout(String name) {
        System.out.println("UserService" + this.hashCode());
        // 使用@Lookup注解 或者使用spring上下文获取bean
        System.out.println("BasicService" + prototypeUtils.getBasicService(name).hashCode());
        System.out.println("带参构造方法" + prototypeUtils.getBasicService(name).getUserName());
        System.out.println("不带参构造方法" + prototypeUtils.getBasicService().getUserName());
    }
}
