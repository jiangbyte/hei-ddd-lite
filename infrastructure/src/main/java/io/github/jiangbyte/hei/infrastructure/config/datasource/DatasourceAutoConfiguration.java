package io.github.jiangbyte.hei.infrastructure.config.datasource;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * 数据源约定自动配置。
 * <p>
 * 当 classpath 存在 HikariDataSource（通常随 spring-boot-starter-jdbc 引入）
 * 且 hei.ddd.datasource.enabled=true（默认）时生效。
 * 实际数据源仍由 Spring Boot 自动配置创建，本类仅作为启用标记与扩展点。
 */
@AutoConfiguration(after = DataSourceAutoConfiguration.class)
@ConditionalOnClass(name = "com.zaxxer.hikari.HikariDataSource")
@ConditionalOnProperty(prefix = "hei.ddd.datasource", name = "enabled", havingValue = "true", matchIfMissing = true)
public class DatasourceAutoConfiguration {
}
