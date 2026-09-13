package io.github.jiangbyte.hei.infrastructure.tx;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.support.SimpleTransactionStatus;

/**
 * 无数据源时的占位事务管理器：保证应用服务 {@code @Transactional} 可解析。
 * 接入真实数据源后可删除本配置，改用 DataSource / JPA 事务管理器。
 */
@Configuration
@EnableTransactionManagement
public class InMemoryTransactionConfiguration {

    @Bean
    public PlatformTransactionManager platformTransactionManager() {
        return new PlatformTransactionManager() {
            @Override
            public TransactionStatus getTransaction(TransactionDefinition definition) throws TransactionException {
                return new SimpleTransactionStatus();
            }

            @Override
            public void commit(TransactionStatus status) throws TransactionException {
                // 内存仓储无外部资源，提交为空操作
            }

            @Override
            public void rollback(TransactionStatus status) throws TransactionException {
                // 内存仓储无外部资源，回滚为空操作
            }
        };
    }
}
