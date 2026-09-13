package io.github.jiangbyte.hei.infrastructure.event;

import io.github.jiangbyte.hei.domain.core.DomainEvent;
import io.github.jiangbyte.hei.domain.core.DomainEventPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * 基于 Spring ApplicationEventPublisher 的同步领域事件发布实现。
 */
@Component
public class SpringDomainEventPublisher implements DomainEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(SpringDomainEventPublisher.class);

    private final ApplicationEventPublisher applicationEventPublisher;

    public SpringDomainEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public void publish(DomainEvent event) {
        // 1. 空事件忽略
        if (event == null) {
            return;
        }
        // 2. 记录调试信息，便于脚手架联调
        log.debug("发布领域事件: type={}, aggregateId={}", event.getClass().getSimpleName(), event.aggregateId());
        // 3. 同步派发到 Spring 事件总线
        applicationEventPublisher.publishEvent(event);
    }
}
