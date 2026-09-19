package io.github.jiangbyte.hei.infrastructure.config.minio;

import io.minio.GetObjectArgs;
import io.minio.PutObjectArgs;
import io.minio.MinioClient;
import io.minio.RemoveObjectArgs;

import java.io.InputStream;

/**
 * MinIO 对象存储门面：封装常见上传/下载/删除操作。
 */
public class MinioObjectStorage {

    private final MinioClient minioClient;
    private final MinioProperties properties;

    public MinioObjectStorage(MinioClient minioClient, MinioProperties properties) {
        this.minioClient = minioClient;
        this.properties = properties;
    }

    /**
     * 上传对象到默认桶。
     */
    public void upload(String objectName, InputStream stream, long size, String contentType) throws Exception {
        minioClient.putObject(PutObjectArgs.builder()
                .bucket(properties.getBucket())
                .object(objectName)
                .stream(stream, size, -1)
                .contentType(contentType)
                .build());
    }

    /**
     * 从默认桶下载对象。
     */
    public InputStream download(String objectName) throws Exception {
        return minioClient.getObject(GetObjectArgs.builder()
                .bucket(properties.getBucket())
                .object(objectName)
                .build());
    }

    /**
     * 删除默认桶中的对象。
     */
    public void remove(String objectName) throws Exception {
        minioClient.removeObject(RemoveObjectArgs.builder()
                .bucket(properties.getBucket())
                .object(objectName)
                .build());
    }
}
