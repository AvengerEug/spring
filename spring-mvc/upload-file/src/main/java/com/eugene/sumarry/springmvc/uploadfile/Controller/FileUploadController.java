package com.eugene.sumarry.springmvc.uploadfile.Controller;

import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/file")
public class FileUploadController {

    /**
     * @RequestPart注解是处理类型为multipart/form-data的表单提交
     * 使用spring提供的上传文件功能
     * @param multipartFile
     */
    @PostMapping("/upload.do")
    public void index(@RequestPart("fileResource") MultipartFile multipartFile) {
        try {
            System.out.println(multipartFile);
            InputStream io = multipartFile.getInputStream();
            // 把文件写在在classpath下的sss.png文件
            FileCopyUtils.copy(io, new FileOutputStream(new File(FileUploadController.class.getResource("/").getPath() + multipartFile.getOriginalFilename())));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
