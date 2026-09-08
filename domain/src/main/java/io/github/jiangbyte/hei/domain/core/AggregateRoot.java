package io.github.jiangbyte.hei.domain.core;

import java.io.Serializable;

/**
 * 聚合根基类：聚合一致性边界的入口，可在此放置简单不变式校验。
 * 复杂跨聚合编排应放在应用服务，而非堆叠在聚合根中。
 *
 * @param <ID> 聚合根标识类型
 */
public abstract class AggregateRoot<ID extends Serializable> extends Entity<ID> {

    private static final long serialVersionUID = 1L;
}
