## 代理模式

### 静态代理
#### 继承
  * 缺点: 随着需求的递增，每次代理增强都会增加一个类，当要增强多种功能时，会产生链式继承并产生类爆炸
    ```java
        public class UserDaoLogAndTimerImpl extends UserDaoTimerImpl {
        
            @Override
            public void findList() {
                System.out.println("[USER MODULE]查询用户数据 开始");
                super.findList();
            }
        }
    ```
    
    ```java
        public class UserDaoTimerImpl extends UserDaoImpl {
        
            @Override
            public void findList() {
                System.out.println("查询开始时间: " + System.currentTimeMillis());
                super.findList();
            }
        }
    ```
    
    ```java
        UserDao userDao = new UserDaoLogAndTimerImpl();

        // 虽然使用继承的方式对UserDao接口的findList接口进行增强了, 但是需求越多,
        // 类数量就会越多。 比如此时的同时拥有记录日志和时间的需求
        userDao.findList();
    ```

#### 组合
  * 优点: 是使用`装饰者设计模式`
  * 缺点: 比继承稍微少一点点类爆炸
    ```java
        public class UserDaoTimerImpl implements UserDao {
        
            private UserDao target;
        
            public UserDaoTimerImpl(UserDao target) {
                this.target = target;
            }
        
            @Override
            public void findList() {
                System.out.println("记录时间");
                target.findList();
            }
        
            @Override
            public void getById(Long userId) {
                System.out.println("记录时间");
                target.getById(userId);
            }
        }
    ```
    
    ```java
        public class UserDaoLogImpl implements UserDao {
        
            private UserDao target;
        
            public UserDaoLogImpl(UserDao target) {
                this.target = target;
            }
        
            @Override
            public void findList() {
                System.out.println("记录日志");
                target.findList();
            }
        
            @Override
            public void getById(Long userId) {
                System.out.println("记录日志");
                target.getById(userId);
            }
        }
    ```
    
    ```java
        // 同时拥有记录日志和记录时间的功能呢
        UserDao userDao = new UserDaoLogImpl(new UserDaoTimerImpl(new UserDaoImpl()));
        userDao.findList();
    ```

### 动态代理
  * 解决问题: 不需要像静态代理一样手动创建类, `运行时`才将增强后的类创建出来并`编译, 创建对象, 赋值`。

#### 手写动态代理
  * 手动实现动态代理类(根据上述静态代理的`组合模式`动态生成)

    1. 代理对象期望结果如下(`方法无返回值`):
        ```java
            package com.eugene.sumarry.proxy;
            
            public class UserDaoImpl$CustomizeProxy implements UserDao {
                UserDao target;
                public UserDaoImpl$CustomizeProxy(UserDao target) {
                    this.target =target;
                }
                public void getById(Long arg0) {
                    Long beginTime = System.currentTimeMillis();
                    System.out.println("开始时间: " + beginTime);
                    target.getById(arg0);
                    Long endTime = System.currentTimeMillis();
                    System.out.println("结束时间: " + endTime);
                    System.out.println("花费: " + (endTime - beginTime));
                }
                public void findList() {
                    Long beginTime = System.currentTimeMillis();
                    System.out.println("开始时间: " + beginTime);
                    target.findList();
                    Long endTime = System.currentTimeMillis();
                    System.out.println("结束时间: " + endTime);
                    System.out.println("花费: " + (endTime - beginTime));
                }
            }
        ```
    
    2. 代理对象期望结果如下(`方法有返回值`):
        ```java
            package com.eugene.sumarry.proxy;
            
            public class StudentDaoImpl$CustomizeProxy implements StudentDao {
                StudentDao target;
                public StudentDaoImpl$CustomizeProxy(StudentDao target) {
                    this.target =target;
                }
                public java.util.List findIdList() {
                    Long beginTime = System.currentTimeMillis();
                    System.out.println("开始时间: " + beginTime);
                    java.util.List result = target.findIdList();
                    Long endTime = System.currentTimeMillis();
                    System.out.println("结束时间: " + endTime);
                    System.out.println("花费: " + (endTime - beginTime));
                    return result;
                }
                public java.lang.String getNameById(Long arg0) {
                    Long beginTime = System.currentTimeMillis();
                    System.out.println("开始时间: " + beginTime);
                    java.lang.String result = target.getNameById(arg0);
                    Long endTime = System.currentTimeMillis();
                    System.out.println("结束时间: " + endTime);
                    System.out.println("花费: " + (endTime - beginTime));
                    return result;
                }
            }
        ```

    3. 生成代理对象工具类:
        ```java
            public class ProxyUtils {
            
                private static final String TAB = "\t";
                
                private static final String LINE = "\n";
                
                private static final String SPACE = " ";
                
                private static final String SEMICOLON = ";";
                
                private static final String PACKAGE = "package com.eugene.sumarry.proxy";
                
                private static final String CLASS_NAME_SUFFIX = "$CustomizeProxy";
                
                private static final String LEFT_BIG_BRACKETS = "{";
                
                private static final String RIGHT_BIG_BRACKETS = "}";
                
                private static final String LEFT_BRACKETS = "(";
                
                private static final String RIGHT_BRACKETS = ")";
                
                
                private static String initProxyClassName(Object target) {
                    return target.getClass().getSimpleName() + CLASS_NAME_SUFFIX;
                }
                
                /**
                 * 如何用程序创建一个类并编译成class文件, 最终创建一个对象出来？
                 *
                 * .java文件 -> 编译成 -> .class文件 -> 获得Class对象 -> 使用反射创建对象
                 *
                 * 该类实现的功能, 代理一个对象并对目标对象中的方法做增强, 记录内部逻辑的执行时间
                 * @param object target对象
                 * @param interfaces target实现的接口
                 * @return
                 */
                public static Object newInstance(Object object, Class<?>[] interfaces) {
                    validate(object.getClass(), interfaces);
                
                    // Step1. 初始化类的内容
                    String content = buildClzContent(object, interfaces);
                
                    // Step2. 创建java文件
                    File file = createJAVAFile(content, "c:\\com\\eugene\\sumarry\\proxy\\" + initProxyClassName(object) + ".java");
                
                    // Step3. 编译java文件 compile
                    compileJAVAFile(file);
                
                    // Step4. 获取类加载器
                    Class<?> clz = getClassLoader(object);
                
                
                    // Step5. 创建对象
                    Object proxy = constructorTargetObject(clz, interfaces[0], object);
                
                    return proxy;
                }
                
                private static Object constructorTargetObject(Class<?> clz, Class<?> paramsTypeClass, Object param) {
                    try {
                        Constructor<?> constructor = clz.getConstructor(paramsTypeClass);
                        return constructor.newInstance(param);
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                
                    return null;
                }
                
                private static Class<?> getClassLoader(Object target) {
                    try {
                        URLClassLoader urlClassLoader = new URLClassLoader(new URL[] {new URL("file:C:\\")});
                        return urlClassLoader.loadClass("com.eugene.sumarry.proxy." + initProxyClassName(target));
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                
                    return null;
                }
                
                private static void compileJAVAFile(File file) {
                    try {
                        JavaCompiler javaCompiler = ToolProvider.getSystemJavaCompiler();
                        StandardJavaFileManager standardJavaFileManager = javaCompiler.getStandardFileManager(null, null, null);
                        Iterable iterable = standardJavaFileManager.getJavaFileObjects(file);
                        JavaCompiler.CompilationTask compilationTask = javaCompiler.getTask(null, standardJavaFileManager, null, null, null, iterable);
                        compilationTask.call();
                        standardJavaFileManager.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                
                private static File createJAVAFile(String content, String fileName) {
                    FileWriter fw = null;
                    File file = null;
                    try {
                        file = new File(fileName);
                
                        File parentFile = new File(file.getParent());
                        if (!parentFile.exists()) {
                            parentFile.mkdirs();
                        }
                
                        fw = new FileWriter(file);
                        fw.write(content);
                        fw.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            fw.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            return file;
                        }
                
                    }
                }
                
                private static String buildClzContent(Object object, Class<?>[] interfaces) {
                    String targetClzName = object.getClass().getSimpleName();
                    // 目前只对实现的第一个接口做增强, 后面的接口类型会忽略掉, 所以代理对象的类型只会是第一个接口的实现类
                    Class interfaceClz = interfaces[0];
                    String interfaceName = interfaceClz.getSimpleName();
                
                    StringBuilder sb = new StringBuilder(PACKAGE).append(SEMICOLON);
                    sb.append(LINE).append(LINE);
                    // Step1. 编写类开头信息: 包括实现的接口
                    sb.append("public class").append(SPACE).append(initProxyClassName(object)).append(SPACE)
                    .append("implements ").append(interfaceName).append(SPACE).append(LEFT_BIG_BRACKETS).append(LINE);
                
                    // Step2. 添加目标对象属性
                    sb.append(TAB).append(interfaceName).append(SPACE).append("target").append(SEMICOLON).append(LINE);
                
                    // Step3. 添加带参构造方法 -> 为了注入目标对象
                    sb.append(TAB).append("public ").append(initProxyClassName(object)).append(LEFT_BRACKETS).append(interfaceName).append(SPACE).append("target").append(RIGHT_BRACKETS).append(SPACE).append(LEFT_BIG_BRACKETS).append(LINE)
                            .append(TAB).append(TAB).append("this.target").append(SPACE).append("=").append("target").append(SEMICOLON).append(LINE)
                            .append(TAB).append(RIGHT_BIG_BRACKETS).append(LINE);
                
                    // Step4. 重写接口中的所有方法
                    Method interfaceMethods[] = interfaces[0].getMethods();
                    for (int i = 0; i < interfaceMethods.length; i++) {
                        Method currentMethod = interfaceMethods[i];
                        String returnType = currentMethod.getReturnType().getName();
                        String methodName = currentMethod.getName();
                
                        sb.append(TAB).append("public ").append(returnType).append(SPACE).append(methodName).append(LEFT_BRACKETS);
                
                        // 构建参数
                        Parameter params[] = currentMethod.getParameters();
                        String paramsSB = "";
                        String paramsSBInvoke = "";
                        for (int j = 0; j < params.length; j++) {
                            Parameter paramInner = params[j];
                            paramsSB += paramInner.getType().getSimpleName() + SPACE + paramInner.getName() + ",";
                            paramsSBInvoke += paramInner.getName() + ",";
                        }
                
                        if (params.length > 0) {
                            paramsSB = paramsSB.substring(0, paramsSB.lastIndexOf(","));
                            paramsSBInvoke = paramsSBInvoke.substring(0, paramsSBInvoke.lastIndexOf(","));
                        }
                
                        sb.append(paramsSB);
                
                        sb.append(RIGHT_BRACKETS).append(SPACE).append(LEFT_BIG_BRACKETS).append(LINE)
                            .append(TAB).append(TAB).append("Long beginTime = System.currentTimeMillis();").append(LINE)
                            .append(TAB).append(TAB).append("System.out.println(\"开始时间: \" + beginTime);").append(LINE);
                
                        if ("void".equals(returnType)) {
                            sb.append(TAB).append(TAB).append("target.").append(methodName).append(LEFT_BRACKETS).append(paramsSBInvoke.length() > 0 ? paramsSBInvoke : "").append(RIGHT_BRACKETS).append(SEMICOLON).append(LINE);
                        } else {
                            sb.append(TAB).append(TAB).append(returnType).append(SPACE).append("result = ").append("target.").append(methodName).append(LEFT_BRACKETS).append(paramsSBInvoke.length() > 0 ? paramsSBInvoke : "").append(RIGHT_BRACKETS).append(SEMICOLON).append(LINE);
                        }
                
                        sb.append(TAB).append(TAB).append("Long endTime = System.currentTimeMillis();").append(LINE)
                        .append(TAB).append(TAB).append("System.out.println(\"结束时间: \" + endTime);").append(LINE)
                        .append(TAB).append(TAB).append("System.out.println(\"花费: \" + (endTime - beginTime));").append(LINE);
                
                        if (!"void".equals(returnType)) {
                            sb.append(TAB).append(TAB).append("return result").append(SEMICOLON).append(LINE);
                        }
                        sb.append(TAB).append(RIGHT_BIG_BRACKETS).append(LINE);
                
                    }
                
                    sb.append(RIGHT_BIG_BRACKETS);
                    return sb.toString();
                }
                
                private static void validate(Class<?> clazz, Class<?>[] interfaces) {
                    if (interfaces.length == 0) {
                        throw new RuntimeException("接口长度为0");
                    }
                
                    boolean isImplInterface = false;
                
                    for (Class<?> anInterface : clazz.getInterfaces()) {
                        boolean isBreak = false;
                        for (Class<?> ainterface : interfaces) {
                
                            if (ainterface.equals(anInterface)) {
                                isImplInterface = true;
                                isBreak = true;
                                break;
                            }
                        }
                
                        if (isBreak) break;
                    }
                
                    if (!isImplInterface) {
                        throw new RuntimeException("类未实现传入接口");
                    }
                }
            }
        ```
        
  * 执行
      ```java
          UserDao userDao = (UserDao) ProxyUtils.newInstance(new UserDaoImpl(), new Class<?>[]{UserDao.class});
          userDao.findList();
  
          StudentDao studentDao = (StudentDao) ProxyUtils.newInstance(new StudentDaoImpl(), new Class<?>[]{StudentDao.class});
          System.out.println(studentDao.findIdList());
          System.out.println(studentDao.getNameById(1111199999L));
      ```
      
      ```java
        开始时间: 1574251511084
        ===================查询所有用户数据===================
        结束时间: 1574251516085
        花费: 5001
        
        
        开始时间: 1574251516199
        ===================StudentDaoImpl.findIdList===================
        结束时间: 1574251516200
        花费: 1
        [1, 2, 3]
        
        
        开始时间: 1574251516200
        ===================StudentDaoImpl.getNameById===================
        结束时间: 1574251516200
        花费: 0
        String: 1111199999
      ```

#### 手写动态代理升级版本, 代理逻辑可配置
  * 背景: 上述手写的动态代理的增强逻辑是写死的`(记录逻辑执行时间)`, 现在的升级版本是要将逻辑可配
  * 思路: 代理逻辑可配, 那就必须要将逻辑传入生成代理类的逻辑中, 并在增强的过程中, 调用该逻辑。   
         但是, 方法传进去了, 要调用它还需要`该方法的对象, 以及参数(反射知识: method.invoke(object, args))`。   
         所以我们可以分别传入`逻辑方法`, `逻辑方法所属对象`, `逻辑方法所需参数`. **但是**, 这三者东西不就是  
         可以用一个对象来描述吗？ **还有一个问题**, 我们在生成代理对象逻辑中能获取到目标对象的所有信息, 所以  
         我们得从目标对象下手。 同样的, 我们现在是要增强代理对象, 所以还需要用一个组合模式来实现，如下: 
    1. 定义一个类实现`MyInvocationHandler`接口
      ```java
          /**
           * 其实这个类就是增强代理类的代理类,
           *
           * 因为上一个手写动态代理的版本中, 增强的业务逻辑是写死的。
           * 要想把增强的业务逻辑也动态化, 那就把代理对象再代理一遍, 这就是此类产生的原由
           */
          public class ProxyCustomizeProxyObject implements MyInvocationHandler {
          
              /**
               * 这个对象就是代理目标对象的对象, 要实现方法的自定义, 就是在invoke中自定义一些方法
               */
              private Object target;
          
              public ProxyCustomizeProxyObject(Object target) {
                  this.target = target;
              }
          
              @Override
              public Object invoke(Method method, Object ...args) {
                  try {
                      System.out.println("自定义增强方法 before");
                      // 此处执行目标对象的逻辑
                      Object result = method.invoke(target, args);
                      System.out.println("自定义增强方法 after");
                      return result;
                  } catch (IllegalAccessException e) {
                      e.printStackTrace();
                  } catch (InvocationTargetException e) {
                      e.printStackTrace();
                  }
                  return null;
              }
          }
      ```
  
    2. 代理类期望结果`方法无返回值`
      ```java
          package com.eugene.sumarry.proxy;
          
          public class UserDaoImpl$CustomizeProxy implements com.eugene.sumarry.proxy.UserDao {
              com.eugene.sumarry.proxy.dynamictype.jdk.MyInvocationHandler h;
              public UserDaoImpl$CustomizeProxy(com.eugene.sumarry.proxy.dynamictype.jdk.MyInvocationHandler h) {
                  this.h = h;
              }
              public void getById(java.lang.Long userId) {
                  try {
                      java.lang.reflect.Method method = com.eugene.sumarry.proxy.UserDao.class.getMethod("getById", java.lang.Long.class);
                      h.invoke(method, userId);
                  } catch(Exception e) {
                      e.printStackTrace();
                  }
              }
              public void findList() {
                  try {
                      java.lang.reflect.Method method = com.eugene.sumarry.proxy.UserDao.class.getMethod("findList");
                      h.invoke(method, new Object());
                  } catch(Exception e) {
                      e.printStackTrace();
                  }
              }
          }
      ```
      
    3. 代理类期望结果`方法有返回值`
      ```java
        package com.eugene.sumarry.proxy;
        
        public class StudentDaoImpl$CustomizeProxy implements com.eugene.sumarry.proxy.StudentDao {
            com.eugene.sumarry.proxy.dynamictype.jdk.MyInvocationHandler h;
            public StudentDaoImpl$CustomizeProxy(com.eugene.sumarry.proxy.dynamictype.jdk.MyInvocationHandler h) {
                this.h = h;
            }
            public java.lang.String getNameById(java.lang.Long studentId) {
                try {
                    java.lang.reflect.Method method = com.eugene.sumarry.proxy.StudentDao.class.getMethod("getNameById", java.lang.Long.class);
                    java.lang.String result = (java.lang.String)h.invoke(method, studentId);
                    return result;
                } catch(Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
            public java.util.List findIdList() {
                try {
                    java.lang.reflect.Method method = com.eugene.sumarry.proxy.StudentDao.class.getMethod("findIdList");
                    java.util.List result = (java.util.List)h.invoke(method, new Object());
                    return result;
                } catch(Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        }
      ```
      
    4. 生成代理类逻辑
      ```java
        public class ProxyUtilsJDK {
        
            private static final String TAB = "\t";
        
            private static final String LINE = "\n";
        
            private static final String SPACE = " ";
        
            private static final String SEMICOLON = ";";
        
            private static final String PACKAGE = "package com.eugene.sumarry.proxy";
        
            private static final String CLASS_NAME_SUFFIX = "$CustomizeProxy";
        
            private static final String LEFT_BIG_BRACKETS = "{";
        
            private static final String RIGHT_BIG_BRACKETS = "}";
        
            private static final String LEFT_BRACKETS = "(";
        
            private static final String RIGHT_BRACKETS = ")";
        
        
            private static String initProxyClassName(Class<?> targetClz) {
                return targetClz.getSimpleName() + CLASS_NAME_SUFFIX;
            }
        
            /**
             * 如何用程序创建一个类并编译成class文件, 最终创建一个对象出来？
             *
             * .java文件 -> 编译成 -> .class文件 -> 获得Class对象 -> 使用反射创建对象
             *
             * 该类实现的功能, 代理一个对象并对目标对象中的方法做增强, 记录内部逻辑的执行时间
             * @param targetClz target对象class类
             * @param interfaces target实现的接口
             * @param handler 代理代理对象的类
             * @return
             */
            public static Object newInstance(Class<?> targetClz, Class<?>[] interfaces, MyInvocationHandler handler) {
                validate(targetClz, interfaces);
        
                // Step1. 初始化类的内容
                String content = buildClzContent(targetClz, interfaces, handler);
        
                // Step2. 创建java文件
                File file = createJAVAFile(content, "c:\\com\\eugene\\sumarry\\proxy\\" + initProxyClassName(targetClz) + ".java");
        
                // Step3. 编译java文件 compile
                compileJAVAFile(file);
        
                // Step4. 获取类加载器
                Class<?> clz = getClassLoader(targetClz);
        
        
                // Step5. 创建对象
                Object proxy = constructorTargetObject(clz, handler.getClass().getInterfaces()[0], handler);
        
                return proxy;
            }
        
            private static Object constructorTargetObject(Class<?> clz, Class<?> paramsTypeClass, Object param) {
                try {
                    Constructor<?> constructor = clz.getConstructor(paramsTypeClass);
                    return constructor.newInstance(param);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
        
                return null;
            }
        
            private static Class<?> getClassLoader(Class<?> targetClz) {
                try {
                    URLClassLoader urlClassLoader = new URLClassLoader(new URL[] {new URL("file:C:\\")});
                    return urlClassLoader.loadClass("com.eugene.sumarry.proxy." + initProxyClassName(targetClz));
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
        
                return null;
            }
        
            private static void compileJAVAFile(File file) {
                try {
                    JavaCompiler javaCompiler = ToolProvider.getSystemJavaCompiler();
                    StandardJavaFileManager standardJavaFileManager = javaCompiler.getStandardFileManager(null, null, null);
                    Iterable<? extends JavaFileObject> iterable = standardJavaFileManager.getJavaFileObjects(file);
                    JavaCompiler.CompilationTask compilationTask = javaCompiler.getTask(null, standardJavaFileManager, null, null, null, iterable);
                    compilationTask.call();
        
                    standardJavaFileManager.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        
            private static File createJAVAFile(String content, String fileName) {
                FileWriter fw = null;
                File file = null;
                try {
                    file = new File(fileName);
        
                    File parentFile = new File(file.getParent());
                    if (!parentFile.exists()) {
                        parentFile.mkdirs();
                    }
        
                    fw = new FileWriter(file);
                    fw.write(content);
                    fw.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        fw.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        return file;
                    }
        
                }
            }
        
            private static String buildClzContent(Class<?> targetClz, Class<?>[] interfaces, MyInvocationHandler handler) {
                // 目前只对实现的第一个接口做增强, 后面的接口类型会忽略掉, 所以代理对象的类型只会是第一个接口的实现类
                Class interfaceClz = interfaces[0];
                String interfaceName = interfaceClz.getName();
                String proxyProxyType = handler.getClass().getInterfaces()[0].getName();
        
                StringBuilder sb = new StringBuilder(PACKAGE).append(SEMICOLON);
                sb.append(LINE).append(LINE);
                // Step1. 编写类开头信息: 包括实现的接口
                sb.append("public class").append(SPACE).append(initProxyClassName(targetClz)).append(SPACE)
                .append("implements ").append(interfaceName).append(SPACE).append(LEFT_BIG_BRACKETS).append(LINE);
        
                // Step2. 添加目标对象属性
                sb.append(TAB).append(proxyProxyType).append(SPACE).append("h").append(SEMICOLON).append(LINE);
        
                // Step3. 添加带参构造方法 -> 为了注入目标对象
                sb.append(TAB).append("public ").append(initProxyClassName(targetClz)).append(LEFT_BRACKETS).append(proxyProxyType).append(SPACE).append("h").append(RIGHT_BRACKETS).append(SPACE).append(LEFT_BIG_BRACKETS).append(LINE)
                        .append(TAB).append(TAB).append("this.h").append(SPACE).append("=").append(SPACE).append("h").append(SEMICOLON).append(LINE)
                        .append(TAB).append(RIGHT_BIG_BRACKETS).append(LINE);
        
                // Step4. 重写接口中的所有方法
                Method interfaceMethods[] = interfaces[0].getMethods();
                for (int i = 0; i < interfaceMethods.length; i++) {
                    Method currentMethod = interfaceMethods[i];
                    String returnType = currentMethod.getReturnType().getName();
                    String methodName = currentMethod.getName();
        
                    sb.append(TAB).append("public ").append(returnType).append(SPACE).append(methodName).append(LEFT_BRACKETS);
        
                    // 构建参数
                    Parameter params[] = currentMethod.getParameters();
                    String paramsSB = "";
                    String paramsSBInvoke = "";
                    String paramsSBClz = "";
                    for (int j = 0; j < params.length; j++) {
                        Parameter paramInner = params[j];
                        paramsSB += paramInner.getType().getName() + SPACE + paramInner.getName() + ",";
                        paramsSBInvoke += paramInner.getName() + ",";
                        paramsSBClz += paramInner.getType().getName() + ".class,";
                    }
        
                    if (params.length > 0) {
                        paramsSB = paramsSB.substring(0, paramsSB.lastIndexOf(","));
                        paramsSBInvoke = paramsSBInvoke.substring(0, paramsSBInvoke.lastIndexOf(","));
                        paramsSBClz = paramsSBClz.substring(0, paramsSBClz.lastIndexOf(","));
                    }
        
                    sb.append(paramsSB);
        
                    sb.append(RIGHT_BRACKETS).append(SPACE).append(LEFT_BIG_BRACKETS).append(LINE);
                    sb.append(TAB).append(TAB).append("try {").append(LINE);
                    sb.append(TAB).append(TAB).append(TAB).append("java.lang.reflect.Method method = " + interfaceName + ".class.getMethod(\"" + methodName + "\"")
                        .append(paramsSBClz.length() > 0 ? ", " + paramsSBClz + ")" : ")").append(SEMICOLON).append(LINE);
        
                    if ("void".equals(returnType)) {
                        sb.append(TAB).append(TAB).append(TAB).append("h.invoke(method").append(", ").append(paramsSBInvoke.length() > 0 ? paramsSBInvoke : "new Object()").append(RIGHT_BRACKETS).append(SEMICOLON).append(LINE);
                    } else {
                        sb.append(TAB).append(TAB).append(TAB).append(returnType).append(SPACE).append("result = ").append(LEFT_BRACKETS).append(returnType).append(RIGHT_BRACKETS).append("h.invoke(method").append(", ").append(paramsSBInvoke.length() > 0 ? paramsSBInvoke : "new Object()").append(RIGHT_BRACKETS).append(SEMICOLON).append(LINE);
                    }
        
                    if (!"void".equals(returnType)) {
                        sb.append(TAB).append(TAB).append(TAB).append("return result").append(SEMICOLON).append(LINE);
                    }
                    sb.append(TAB).append(TAB).append("} catch(Exception e) {").append(LINE);
                    sb.append(TAB).append(TAB).append(TAB).append("e.printStackTrace()").append(SEMICOLON).append(LINE);
                    sb.append(TAB).append(TAB).append("}").append(LINE);
        
                    if (!"void".equals(returnType)) {
                        sb.append(TAB).append(TAB).append("return null").append(SEMICOLON).append(LINE);
                    }
        
                    sb.append(TAB).append(RIGHT_BIG_BRACKETS).append(LINE);
        
                }
        
                sb.append(RIGHT_BIG_BRACKETS);
                return sb.toString();
            }
        
            private static void validate(Class<?> clazz, Class<?>[] interfaces) {
                if (interfaces.length == 0) {
                    throw new RuntimeException("实现接口数量为0, 至少实现一个接口");
                }
        
                boolean isImplInterface = false;
        
                for (Class<?> anInterface : clazz.getInterfaces()) {
                    boolean isBreak = false;
                    for (Class<?> interfaceInner : interfaces) {
        
                        if (interfaceInner.equals(anInterface)) {
                            isImplInterface = true;
                            isBreak = true;
                            break;
                        }
                    }
        
                    if (isBreak) break;
                }
        
                if (!isImplInterface) {
                    throw new RuntimeException("类未实现传入接口");
                }
            }
        }
      ```
      
    5. 与上一个版本改进的点:
        * 传入参数的修改: 之前需要传入目标对象, 现在需要传`目标对象的class`, `目标对象实现接口的class`以及 `实现MyInvocationHandler接口类实例`
        * 代理对象类内容的修改: 之前是代理目标对象, 现在是代理 **代理对象**
        * 执行目标不一样: 之前是执行代理对象的逻辑, 现在是执行代理 **代理对象** 对象的逻辑, 原生类的逻辑由实现MyInvocationHandler的类去执行
 
    6. 执行步骤与结果:

      ```java
        UserDao proxyDao = (UserDao) ProxyUtilsJDK.newInstance(UserDaoImpl.class, new Class<?>[]{UserDao.class}, new ProxyCustomizeProxyObject(new UserDaoImpl()));
        proxyDao.getById(55555L);

        StudentDao studentDaoProxy = (StudentDao)ProxyUtilsJDK.newInstance(StudentDaoImpl.class, new Class<?>[]{StudentDao.class}, new ProxyCustomizeProxyObject(new StudentDaoImpl()));
        System.out.println(studentDaoProxy.getNameById(5555888L));
      ```
      
      ```text
        自定义增强方法 before
        根据用户ID查找用户数据
        自定义增强方法 after
        
        自定义增强方法 before
        ===================StudentDaoImpl.getNameById===================
        自定义增强方法 after
        String: 5555888
      ```
 