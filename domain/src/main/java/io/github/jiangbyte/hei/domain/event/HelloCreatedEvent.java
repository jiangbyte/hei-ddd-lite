package io.github.jiangbyte.hei.domain.event;

import io.github.jiangbyte.hei.domain.core.DomainEvent;

import java.time.OffsetDateTime;
import java.util.Objects;

/**
 * Hello 聚合已创建事件。
 */
public final class HelloCreatedEvent implements DomainEvent {

    private final Long helloId;
    private final String greetingText;
    private final OffsetDateTime occurredAt;

    public HelloCreatedEvent(Long helloId, String greetingText) {
        this.helloId = Objects.requireNonNull(helloId, "helloId");
        this.greetingText = greetingText;
        this.occurredAt = OffsetDateTime.now();
    }

    public Long getHelloId() {
        return helloId;
    }

    public String getGreetingText() {
        return greetingText;
    }

    @Override
    public OffsetDateTime occurredAt() {
        return occurredAt;
    }

    @Override
    public String aggregateId() {
        return String.valueOf(helloId);
    }
}
