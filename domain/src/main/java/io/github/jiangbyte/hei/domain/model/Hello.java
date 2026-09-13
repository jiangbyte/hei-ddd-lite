package io.github.jiangbyte.hei.domain.model;

import io.github.jiangbyte.hei.domain.core.AggregateRoot;
import io.github.jiangbyte.hei.domain.core.DomainException;
import io.github.jiangbyte.hei.domain.event.HelloCreatedEvent;
import io.github.jiangbyte.hei.domain.event.HelloGreetingChangedEvent;

import java.util.Objects;

/**
 * Hello 聚合根占位：承载问候状态与简单不变式，供脚手架复制扩展。
 */
public class Hello extends AggregateRoot<Long> {

    private static final long serialVersionUID = 1L;

    private final Long id;
    private Greeting greeting;

    private Hello(Long id, Greeting greeting) {
        this.id = Objects.requireNonNull(id, "id");
        this.greeting = Objects.requireNonNull(greeting, "greeting");
    }

    /**
     * 由工厂重建/创建聚合；登记创建事件。
     */
    public static Hello create(Long id, Greeting greeting) {
        // 1. 构造聚合实例并保证标识与问候语非空
        Hello hello = new Hello(id, greeting);
        // 2. 登记创建事实，供应用服务保存后发布
        hello.registerEvent(new HelloCreatedEvent(id, greeting.getText()));
        return hello;
    }

    /**
     * 从持久化状态还原聚合（不登记事件）。
     */
    public static Hello restore(Long id, Greeting greeting) {
        return new Hello(id, greeting);
    }

    @Override
    public Long getId() {
        return id;
    }

    public Greeting getGreeting() {
        return greeting;
    }

    /**
     * 返回当前问候文案。
     */
    public String greet() {
        return greeting.getText();
    }

    /**
     * 变更问候语；相同文案则忽略，避免空事件。
     */
    public void changeGreeting(Greeting newGreeting) {
        // 1. 校验新问候语
        if (newGreeting == null) {
            throw new DomainException("问候语不能为空");
        }
        // 2. 文案未变化则直接返回
        if (Objects.equals(this.greeting, newGreeting)) {
            return;
        }
        // 3. 更新状态并登记变更事件
        this.greeting = newGreeting;
        registerEvent(new HelloGreetingChangedEvent(id, newGreeting.getText()));
    }
}
