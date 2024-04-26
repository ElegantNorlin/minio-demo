package cn.wanwan.miniodemo.utils;

import io.minio.*;
import io.minio.errors.*;
import io.minio.messages.Bucket;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class MinioUtil {

    @Resource
    private MinioClient minioClient;

    /**
     * @return List<String>
     * @author wanwan
     * @date 2024-04-26 13:25:00
     * @description 获取存储桶名称列表
     */
    public List<String> getBucketNameList() {
        try {
            List<String> bucketNameList = new ArrayList<>();
            for (Bucket bucket : minioClient.listBuckets()) {
                bucketNameList.add(bucket.name());
            }
            return bucketNameList;
        } catch (ErrorResponseException | InsufficientDataException | InternalException | InvalidKeyException |
                 InvalidResponseException | IOException | NoSuchAlgorithmException | ServerException |
                 XmlParserException e) {
            log.error("【MinioUtil】getBucketNameList方法抛出异常：", e);
        }
        return null;
    }

    /**
     * @return boolean
     * @author wanwan
     * @date 2024-04-26 13:25:00
     * @description 创建存储桶
     */
    public boolean createBucket(String bucketName) {
        try {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            return true;
        } catch (ErrorResponseException | InsufficientDataException | InternalException | InvalidKeyException |
                 InvalidResponseException | IOException | NoSuchAlgorithmException | ServerException |
                 XmlParserException e) {
            log.error("【MinioUtil】createBucket方法抛出异常：", e);
        }
        return false;
    }

    /**
     * @return boolean
     * @author wanwan
     * @date 2024-04-26 13:25:00
     * @description 删除存储桶
     */
    public boolean removeBucket(String bucketName) {
        try {
            minioClient.removeBucket(RemoveBucketArgs.builder().bucket(bucketName).build());
            return true;
        } catch (ErrorResponseException | InsufficientDataException | InternalException | InvalidKeyException |
                 InvalidResponseException | IOException | NoSuchAlgorithmException | ServerException |
                 XmlParserException e) {
            log.error("【MinioUtil】removeBucket方法抛出异常：", e);
        }
        return false;
    }

    /**
     * @return boolean
     * @author wanwan
     * @date 2024-04-26 13:25:00
     * @description 根据路径获取文件
     */
    public InputStream downloadFile(String bucketName, String filePath) {
        GetObjectArgs getObjectArgs = GetObjectArgs.builder()
                .bucket(bucketName)
                .object(filePath)
                .build();
        try {
            return minioClient.getObject(getObjectArgs);
        } catch (ErrorResponseException | InsufficientDataException | InternalException | InvalidKeyException |
                 InvalidResponseException | IOException | NoSuchAlgorithmException | ServerException |
                 XmlParserException e) {
            log.error("【MinioUtil】downloadFile方法抛出异常：", e);
        }
        return null;
    }

    /**
     * @author wanwan
     * @date 2024-04-26 13:25:00
     * @description 上传文件到指定路径，若该文件路径已存在，则覆盖原文件
     */
    public void uploadFile(InputStream inputStream, String bucket, String filePath) {
        try {
            minioClient.putObject(
                PutObjectArgs
                    .builder()
                    .bucket(bucket)
                    .object(filePath)
                    .stream(inputStream,-1,Integer.MAX_VALUE)
                    .build()
            );
        } catch (ErrorResponseException | InsufficientDataException | InvalidKeyException | InternalException |
                 InvalidResponseException | IOException | NoSuchAlgorithmException | ServerException |
                 XmlParserException e) {
            log.error("【MinioUtil】uploadFile方法抛出异常：", e);
        }
    }

    /**
     * @author wanwan
     * @date 2024-04-26 13:25:00
     * @description 判断文件是否存在
     */
    public boolean isFileExist(String bucketName, String filePath) {
        GetObjectArgs getObjectArgs = GetObjectArgs.builder()
                .bucket(bucketName)
                .object(filePath)
                .build();
        try {
            return minioClient.getObject(getObjectArgs) != null;
        } catch (ErrorResponseException | InsufficientDataException | InternalException | InvalidKeyException |
                 InvalidResponseException | IOException | NoSuchAlgorithmException | ServerException |
                 XmlParserException e) {
            log.error("【MinioUtil】isFileExist方法抛出异常：", e);
            return false;
        }
    }
}
