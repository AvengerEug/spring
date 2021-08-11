## Spring多个切面同时增强同一个方法，如何指定顺序？

## 有如下三个解决方案：

### 1、在aspect切面中使用@Order注解

### 2、在aspect切面中实现Ordered接口

### 3、在aspect切面中@Order注解与Ordered接口混合使用