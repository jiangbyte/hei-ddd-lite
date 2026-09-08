package io.github.jiangbyte.hei.domain.core;

import java.io.Serializable;
import java.util.Objects;

/**
 * 领域实体基类：以标识（ID）判定相等性。
 *
 * @param <ID> 实体标识类型
 */
public abstract class Entity<ID extends Serializable> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 获取实体唯一标识。
     */
    public abstract ID getId();

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Entity<?> entity = (Entity<?>) o;
        return getId() != null && Objects.equals(getId(), entity.getId());
    }

    @Override
    public int hashCode() {
        return getId() == null ? System.identityHashCode(this) : Objects.hash(getId());
    }
}
