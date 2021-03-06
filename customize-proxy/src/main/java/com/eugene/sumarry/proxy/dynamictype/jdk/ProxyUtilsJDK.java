package com.eugene.sumarry.proxy.dynamictype.jdk;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * 目前只对实现的第一个接口做增强, 后面的接口类型会忽略掉, 生成的代理对象只会是第一个接口的实现类
 */
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
