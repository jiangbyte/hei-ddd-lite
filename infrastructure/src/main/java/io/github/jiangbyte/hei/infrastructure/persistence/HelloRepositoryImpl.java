package io.github.jiangbyte.hei.infrastructure.persistence;

import io.github.jiangbyte.hei.domain.model.Hello;
import io.github.jiangbyte.hei.domain.repository.HelloRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Hello 仓储实现占位。
 * <p>
 * 职责：对接数据库 / MyBatis-Plus 等持久化技术，实现领域仓储接口。
 * 当前为空壳，方法未真正落库；接入 DB 后在此编写 Mapper 调用。
 */
@Repository
public class HelloRepositoryImpl implements HelloRepository {

    /**
     * 保存占位：后续对接 insert/update。
     */
    @Override
    public Hello save(Hello entity) {
        return entity;
    }

    /**
     * 查询占位：后续对接 selectById。
     */
    @Override
    public Optional<Hello> findById(Long id) {
        return Optional.empty();
    }

    /**
     * 删除占位：后续对接 deleteById。
     */
    @Override
    public void remove(Long id) {
        // 占位：暂无实现
    }
}
