package com.atguigu.spzx.manager.service.impl;

import com.atguigu.spzx.manager.properties.MinioProperties;
import com.atguigu.spzx.manager.service.FileUploadService;
import com.atguigu.spzx.utils.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileUploadServiceImpl implements FileUploadService {

    @Autowired
    private MinioProperties minioProperties ;

    @Override
    public String fileUpload(MultipartFile multipartFile) {

        try {
            return FileUtil.upload(multipartFile,
                    "http://127.0.0.1:9000",
                    "spzx-bucket",
                    "Qxj6SqNo6LlOQ0ctPnnV",
                    "zvxUyFYzryql0FGQ3HC1MzK953i2Z0Co6t6BitAb");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}