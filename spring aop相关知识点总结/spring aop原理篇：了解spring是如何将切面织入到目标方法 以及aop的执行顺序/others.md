spring aop注意细节：

* 此方法org.springframework.aop.aspectj.annotation.AnnotationAwareAspectJAutoProxyCreator#findCandidateAdvisors主要是去找实现了Advisor接口的bean，目前我们的程序没有bean实现了这个接口，但是如果我们的程序开启了事务功能的话，那么这个接口就能找到实现了Advisor接口的bean了，因此，此接口在第一次调用的时候，主要是去找事务的增强器。

* 在找通知的过程中，会将每个通知转换成InstantiationModelAwarePointcutAdvisorImpl类型的对象，其中这个对象中有一个特别重要的属性，就是：**instantiatedAdvice**，这个属性是Advice的类型的。其中，针对我们的通知类型，转化成对应的Advice。其转换类型如下表中所示：

  | 切面中定义的通知类型 | 切面中定义的通知类型的注解 | Advice类型                  |
  | -------------------- | -------------------------- | --------------------------- |
  | AtBefore             | @Before                    | AspectJMethodBeforeAdvice   |
  | AtAfter              | @After                     | AspectJAfterAdvice          |
  | AtAfterReturning     | @AfterReturning            | AspectJAfterReturningAdvice |
  | AtAfterThrowing      | @AfterThrowing             | AspectJAfterThrowingAdvice  |
  | AtAround             | @Around                    | AspectJAroundAdvice         |

  其对应的源码如下所示：

  ```java
  AbstractAspectJAdvice springAdvice;
  
  switch (aspectJAnnotation.getAnnotationType()) {
      case AtBefore:
          springAdvice = new AspectJMethodBeforeAdvice(
              candidateAdviceMethod, expressionPointcut, aspectInstanceFactory);
          break;
      case AtAfter:
          springAdvice = new AspectJAfterAdvice(
              candidateAdviceMethod, expressionPointcut, aspectInstanceFactory);
          break;
      case AtAfterReturning:
          springAdvice = new AspectJAfterReturningAdvice(
              candidateAdviceMethod, expressionPointcut, aspectInstanceFactory);
          AfterReturning afterReturningAnnotation = (AfterReturning) aspectJAnnotation.getAnnotation();
          if (StringUtils.hasText(afterReturningAnnotation.returning())) {
              springAdvice.setReturningName(afterReturningAnnotation.returning());
          }
          break;
      case AtAfterThrowing:
          springAdvice = new AspectJAfterThrowingAdvice(
              candidateAdviceMethod, expressionPointcut, aspectInstanceFactory);
          AfterThrowing afterThrowingAnnotation = (AfterThrowing) aspectJAnnotation.getAnnotation();
          if (StringUtils.hasText(afterThrowingAnnotation.throwing())) {
              springAdvice.setThrowingName(afterThrowingAnnotation.throwing());
          }
          break;
      case AtAround:
          springAdvice = new AspectJAroundAdvice(
              candidateAdviceMethod, expressionPointcut, aspectInstanceFactory);
          break;
      case AtPointcut:
          if (logger.isDebugEnabled()) {
              logger.debug("Processing pointcut '" + candidateAdviceMethod.getName() + "'");
          }
          return null;
      default:
          throw new UnsupportedOperationException(
              "Unsupported advice type on method: " + candidateAdviceMethod);
  }
  
  ```

* 这段代码：

  ```java
  Object[] specificInterceptors = getAdvicesAndAdvisorsForBean(bean.getClass(), beanName, null);
  ```

  是把事务和aop的通知都找出来，比如我们在xml中开启事务时，肯定添加过这个标签：**aop:advisor** --> 这个就是事务的切面，最终会在这个方法中被找出来，然后将我们切面中的通知都转换成一个个的interceptor拦截器。最终，我们的通知就会变成一个个的拦截器并使用责任链的设计模式来调用。

* 创建代理对象那里，这个属性proxyTargetClass设置为true，正常情况下就是使用cglib代理。

* 使用arthas查看下代理对象的内部的逻辑，实际上就是一个简单的调用h.invoke方法，而这个h就是在这个方法中：org.springframework.aop.framework.JdkDynamicAopProxy#getProxy(java.lang.ClassLoader)的如下代码：

  ```java
  @Override
  public Object getProxy(@Nullable ClassLoader classLoader) {
      if (logger.isDebugEnabled()) {
          logger.debug("Creating JDK dynamic proxy: target source is " + this.advised.getTargetSource());
      }
      Class<?>[] proxiedInterfaces = AopProxyUtils.completeProxiedInterfaces(this.advised, true);
      findDefinedEqualsAndHashCodeMethods(proxiedInterfaces);
      return Proxy.newProxyInstance(classLoader, proxiedInterfaces, this); // @1
  }
  
  ```

  的@1处指向的代码的this对象，而这个this就是JdkDynamicAopProxy，最终这个h就是JdkDynamicAopProxy对象，进而调用到它的invoke方法