package io.github.jiangbyte.hei.application;

import io.github.jiangbyte.hei.application.command.CreateHelloCommand;
import io.github.jiangbyte.hei.application.core.ApplicationService;
import io.github.jiangbyte.hei.application.dto.HelloView;
import io.github.jiangbyte.hei.application.query.GetHelloQuery;
import io.github.jiangbyte.hei.domain.core.BizException;
import io.github.jiangbyte.hei.domain.core.DomainEventPublisher;
import io.github.jiangbyte.hei.domain.factory.HelloFactory;
import io.github.jiangbyte.hei.domain.model.Hello;
import io.github.jiangbyte.hei.domain.repository.HelloRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Hello 应用服务：编排用例、控制事务边界，协调工厂 / 仓储 / 事件发布。
 */
@Service
public class HelloApplicationService implements ApplicationService {

    private final HelloRepository helloRepository;
    private final DomainEventPublisher domainEventPublisher;
    private final HelloFactory helloFactory = new HelloFactory();

    public HelloApplicationService(HelloRepository helloRepository,
                                   DomainEventPublisher domainEventPublisher) {
        this.helloRepository = helloRepository;
        this.domainEventPublisher = domainEventPublisher;
    }

    /**
     * 创建 Hello：工厂创建 → 仓储保存 → 拉取并发布领域事件。
     */
    @Transactional
    public HelloView create(CreateHelloCommand command) {
        // 1. 通过工厂创建合法聚合（含创建事件）
        Hello hello = helloFactory.create(command.getGreetingText());
        // 2. 持久化聚合
        Hello saved = helloRepository.save(hello);
        // 3. 拉取并发布领域事件（事务内同步发布，脚手架简化）
        domainEventPublisher.publish(saved.pullDomainEvents());
        // 4. 组装应用读模型返回
        return toView(saved);
    }

    /**
     * 按 ID 查询 Hello。
     */
    @Transactional(readOnly = true)
    public HelloView get(GetHelloQuery query) {
        // 1. 按标识加载聚合
        Hello hello = helloRepository.findById(query.getHelloId())
                .orElseThrow(() -> new BizException("HELLO_NOT_FOUND", "Hello 不存在: " + query.getHelloId()));
        // 2. 组装读模型
        return toView(hello);
    }

    /**
     * 默认问候占位（公开演示接口仍可用）。
     */
    @Transactional(readOnly = true)
    public String greet() {
        return "hello world";
    }

    private static HelloView toView(Hello hello) {
        return new HelloView(hello.getId(), hello.greet());
    }
}
