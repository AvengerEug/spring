## 使用springmvc中自带的上传文件功能

* [官方文档:https://docs.spring.io/spring/docs/current/spring-framework-reference/web.html#mvc-multipart](https://docs.spring.io/spring/docs/current/spring-framework-reference/web.html#mvc-multipart)

### 使用Apache Commons Fileupload jar包实现文件上传
* 添加一个`CommonsMultipartResolver`类型的bean, 并且名字一定叫`multipartResolver`
* 项目依赖**commons-fileupload** jar包
* 添加api接口
    ```java
      /**
       * fileResource为表单中选择文件的input的name
       */
      @PostMapping("/upload.do")
      public void index(@RequestPart("fileResource") MultipartFile multipartFile) {
          try {
              System.out.println(multipartFile);
              InputStream io = multipartFile.getInputStream();
              // 把文件写在在classpath下的sss.png文件
              FileCopyUtils.copy(io, new FileOutputStream(new File(FileUploadController.class.getResource("/").getPath() + "sss.png")));
          } catch (IOException e) {
              e.printStackTrace();
          }
  
      }
    ```
    
### 使用servlet3.0自带文件上传功能

## 问题点
* Q: 为什么类型为`CommonsMultipartResolver`的bean的名字一定叫`multipartResolver`?
* A: 因为spring只允许一个处理文件的类，这个类是个接口，把它定死后，我们可以给不同的实现，这样的话扩展性
     就比较高，我们可以自己写一个处理文件上传的类
     
