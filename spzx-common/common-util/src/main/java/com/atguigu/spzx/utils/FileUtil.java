package com.atguigu.spzx.utils;

import cn.hutool.core.lang.UUID;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

public class FileUtil {
    /**
     * 文件上传
     *
     * @param multipartFile
     * @param endpoint:    上传地址
     * @param username:     账户
     * @param password:     密码
     * @return
     * @throws Exception
     */
    public static String upload(MultipartFile multipartFile, String endpoint, String bucketName, String username, String password) throws Exception {
        // 创建一个Minio的客户端对象
        MinioClient minioClient = MinioClient.builder()
                .endpoint(endpoint)
                .credentials(username, password)
                .build();

        boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket("spzx-bucket").build());

        // 如果不存在，那么此时就创建一个新的桶
        if (!found) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
        } else {  // 如果存在打印信息
            System.out.println("Bucket already exists.");
        }
        //`20231122\34f64ee3ed6042d38daf6755beefb88f.jpg`
        String originalFilename = multipartFile.getOriginalFilename();
        String fileName = UUID.randomUUID().toString().replace("-", "") + "."
                + originalFilename.split("\\.")[1];
        PutObjectArgs putObjectArgs = PutObjectArgs.builder()
                .bucket(bucketName)
                .stream(multipartFile.getInputStream(), multipartFile.getInputStream().available(), -1)
                .object(fileName)
                .build();
        minioClient.putObject(putObjectArgs);

        // 构建fileUrl
        String fileUrl = endpoint + File.separator + bucketName + File.separator + fileName;
        System.out.println(fileUrl);
        return fileUrl;
    }

}
