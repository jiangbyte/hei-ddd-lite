package io.github.jiangbyte.hei.infrastructure.config.minio;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * MinIO 连接配置属性。
 */
@ConfigurationProperties(prefix = "hei.ddd.minio")
public class MinioProperties {

    /** 是否启用 MinIO 自动配置 */
    private boolean enabled = true;

    /** 服务端点，例如 http://127.0.0.1:9000 */
    private String endpoint = "http://127.0.0.1:9000";

    /** Access Key */
    private String accessKey = "minioadmin";

    /** Secret Key */
    private String secretKey = "minioadmin";

    /** 默认桶名 */
    private String bucket = "hei";

    /** 启动时是否尝试创建桶 */
    private boolean createBucketIfAbsent = false;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public boolean isCreateBucketIfAbsent() {
        return createBucketIfAbsent;
    }

    public void setCreateBucketIfAbsent(boolean createBucketIfAbsent) {
        this.createBucketIfAbsent = createBucketIfAbsent;
    }
}
