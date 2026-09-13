/**
 * 持久化适配：实现领域仓储端口。
 *
 * <p><b>放什么</b>：{@code *RepositoryImpl}、后续 Mapper/PO 映射。
 * <b>不放什么</b>：领域规则、Controller。
 *
 * <p><b>如何扩展</b>：脚手架默认内存实现；接库时保留接口，替换实现并启用 datasource/mybatis 自动配置。
 */
package io.github.jiangbyte.hei.infrastructure.persistence;
