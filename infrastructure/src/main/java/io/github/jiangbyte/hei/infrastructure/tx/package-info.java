/**
 * 事务占位配置：无数据源时提供可解析的 {@code PlatformTransactionManager}。
 *
 * <p><b>如何扩展</b>：接入真实 DataSource 后删除本包配置，改用数据源 / JPA 事务管理器。
 */
package io.github.jiangbyte.hei.infrastructure.tx;
