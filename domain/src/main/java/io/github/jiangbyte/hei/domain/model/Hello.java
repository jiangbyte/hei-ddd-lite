package io.github.jiangbyte.hei.domain.model;

import io.github.jiangbyte.hei.domain.core.AggregateRoot;

/**
 * Hello 领域实体/聚合占位。
 * <p>
 * 职责：承载 Hello 相关的领域状态与简单不变式；真实业务字段与行为在此扩展。
 * 当前为空壳，仅用于演示分层落点。
 */
public class Hello extends AggregateRoot<Long> {

    private static final long serialVersionUID = 1L;

    @Override
    public Long getId() {
        // 占位：后续补充持久化标识
        return null;
    }
}
