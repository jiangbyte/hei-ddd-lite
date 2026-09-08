package io.github.jiangbyte.hei.domain.core;

import java.io.Serializable;
import java.util.Optional;

/**
 * 仓储接口约定：由领域层定义、基础设施层实现。
 *
 * @param <T>  聚合/实体类型
 * @param <ID> 标识类型
 */
public interface Repository<T, ID extends Serializable> {

    /**
     * 保存（新增或更新）。
     */
    T save(T entity);

    /**
     * 按标识查询。
     */
    Optional<T> findById(ID id);

    /**
     * 按标识删除。
     */
    void remove(ID id);
}
