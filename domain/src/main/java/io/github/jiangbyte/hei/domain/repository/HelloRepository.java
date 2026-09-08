package io.github.jiangbyte.hei.domain.repository;

import io.github.jiangbyte.hei.domain.core.Repository;
import io.github.jiangbyte.hei.domain.model.Hello;

/**
 * Hello 领域仓储接口。
 * <p>
 * 职责：定义 Hello 聚合的持久化能力契约；具体实现位于 infrastructure 层。
 * 当前为空壳，仅继承通用仓储约定。
 */
public interface HelloRepository extends Repository<Hello, Long> {
}
