# 测试servlet 3.0 SPI功能

## 原理

* webapp类型的项目在tomcat启动后会去classpath路径下找
  META-INF/services/javax.servlet.ServletContainerInitializer文件中的内容,
  里面存放的是ServletContainerInitializer的实现类, 所以tomcat启动后会实例化这
  些类并执行他们的onStartup放啊, 里面传了两个参数`Set<Class<?>> c, ServletContext ctx`
  c里面的元素会根据当前类是否添加`@HandlesTypes`注解来注入, 假设当前执行的类中添加了
  `@HandlesTypes`注解, 并执行了value(eg: @HandlesTypes(Eugene.class)), 那么变量c里面
  存的元素就是所有`Eugene.class`这个接口的实现类, 然后我们还能获取到`ServletContext`,
  有了`ServletContext`，那么我们就能往里面添加**Servlet, Listener, Filter**了，
  这样，我们就能省去web.xml来添加**Servlet, Listener, Filter**了。

## 启动此项目
  在servlet3dot0spi项目根目录执行命令**mvn tomcat7:run**
  eg: E:\java-backend\basic\spring\spring-mvc\servlet3dot0spi>mvn tomcat7:run

## 注意事项

* 使用maven构建项目时, 一定要选择`webapp`模块, 因为你指定是webapp模块后
  tomcat启动时才会去执行SPI功能, 及扫描配置的`javax.servlet.ServletContainerInitializer`接口的实现类
  构建普通的maven项目并不会实现spi功能, 除非你使用tomcat源码启动项目, 并指定以webApp的形式启动(tomcat.addWebapp()使用此api),
  项目启动时才会按照webapp的项目去启动, SPI功能才会被执行