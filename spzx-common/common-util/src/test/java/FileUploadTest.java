import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;

import java.io.FileInputStream;

public class FileUploadTest {

    public static void main(String[] args) throws Exception {
        // 创建一个Minio的客户端对象
        MinioClient minioClient = MinioClient.builder()
                .endpoint("http://127.0.0.1:9000")
                .credentials("Qxj6SqNo6LlOQ0ctPnnV", "zvxUyFYzryql0FGQ3HC1MzK953i2Z0Co6t6BitAb")
                .build();

        boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket("spzx-bucket").build());

        // 如果不存在，那么此时就创建一个新的桶
        if (!found) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket("spzx-bucket").build());
        } else {  // 如果存在打印信息
            System.out.println("Bucket 'spzx-bucket' already exists.");
        }

        FileInputStream fis = new FileInputStream("D:/animal.jpg") ;
        PutObjectArgs putObjectArgs = PutObjectArgs.builder()
                .bucket("spzx-bucket")
                .stream(fis, fis.available(), -1)
                .object("animal.jpg")
                .build();
        minioClient.putObject(putObjectArgs) ;

        // 构建fileUrl
        String fileUrl = "http://127.0.0.1:9000/spzx-bucket/animal.jpg" ;
        System.out.println(fileUrl);
    }
}