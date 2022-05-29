package com.wnsrich.reggie.controller;

import com.wnsrich.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.UUID;

@RestController
@RequestMapping("/common")
@Slf4j
public class CommonController {

    @Value("${reggie.path}")
    private String basePath;

    /**
     *  文件上传
     * @param file 要和前端的表单名一致
     * @return
     */
    @PostMapping("/upload")
    public R<String> upload(MultipartFile file){

        File dir = new File(basePath);
        if (!dir.exists()) dir.mkdirs();

        // 获取文件原始名
        String OriginalFilename = file.getOriginalFilename();
        log.info("文件名字"+OriginalFilename);
        //获取文件后缀，如.jpg,.png等
        String suffix = OriginalFilename.substring((OriginalFilename.lastIndexOf(".")));
        // 利用UUID创建新的文件名
        String newFileName = UUID.randomUUID().toString() + suffix;

        // 如果目录不存在则创建


        try {
            // file是一个临时文件，需要转存
            file.transferTo(new File(basePath+newFileName));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return R.success(newFileName);
    }

    /**
     * 文件回显
     * @param response
     * @param name
     */
    @GetMapping("/download")
    public void download(HttpServletResponse response,String name){
        try(
            // 读取临时文件
            FileInputStream fis = new FileInputStream(new File(basePath+name));
            //将读取的文件写回浏览器
            ServletOutputStream fos = response.getOutputStream();
             ) {

            // 让客户端知道返回类型
            response.setContentType("/image/jpeg");
            // 读取数组
            byte[] bytes = new byte[1024];
            int len = 0;
            while((len = fis.read(bytes)) != -1){
                fos.write(bytes,0,len);
                // 刷新
                fos.flush();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
