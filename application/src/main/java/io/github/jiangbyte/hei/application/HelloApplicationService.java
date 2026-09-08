package io.github.jiangbyte.hei.application;

import io.github.jiangbyte.hei.application.core.ApplicationService;
import org.springframework.stereotype.Service;

/**
 * Hello 应用服务占位。
 * <p>
 * 职责：编排 Hello 相关用例、定义事务边界；协调领域对象与仓储。
 * 当前仅提供问候占位方法，真实业务逻辑在此扩展。
 */
@Service
public class HelloApplicationService implements ApplicationService {

    /**
     * 问候用例占位：演示应用服务对外提供能力。
     *
     * @return 问候文案
     */
    public String greet() {
        return "hello world";
    }
}
