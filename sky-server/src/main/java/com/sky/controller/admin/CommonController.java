package com.sky.controller.admin;

import com.sky.result.Result;
import com.sky.utils.AliOssUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * @Author:ascrm
 * @Date:2024/3/23
 */
@RestController
@RequestMapping("/admin/common")
@Slf4j
public class CommonController {

    @Autowired
    private AliOssUtil aliOssUtil;

    /**
     * 图片上传
     */
    @PostMapping("/upload")
    public Result<String> upload(MultipartFile file) throws IOException {

        log.info("正在上传图片....");
        //获取文件原名
        String originalFilename = file.getOriginalFilename();

        //获取后缀
        assert originalFilename != null;//断言originalName!=null
        int index = originalFilename.lastIndexOf(".");
        String suffix = originalFilename.substring(index);

        //给文件创建新名字
        String prefix = UUID.randomUUID().toString();

        /*
        上传
        1.file.getBytes()就是把文件的具体内转化成字节数组，用于数据传输
        2.url是图片所在的地址
         */
        String url = aliOssUtil.upload(file.getBytes(), prefix + suffix);
        return Result.success(url);
    }
}
