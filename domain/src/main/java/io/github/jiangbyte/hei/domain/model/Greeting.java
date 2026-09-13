package io.github.jiangbyte.hei.domain.model;

import io.github.jiangbyte.hei.domain.core.DomainException;
import io.github.jiangbyte.hei.domain.core.ValueObject;

import java.util.Objects;

/**
 * 问候语文案值对象：不可变，按文本内容相等。
 */
public final class Greeting implements ValueObject {

    private final String text;

    private Greeting(String text) {
        this.text = text;
    }

    /**
     * 创建问候语；空白文案视为非法。
     */
    public static Greeting of(String text) {
        // 1. 拒绝 null / 空白，保证值对象始终合法
        if (text == null || text.isBlank()) {
            throw new DomainException("问候语不能为空");
        }
        // 2. 去除首尾空白后构造不可变实例
        return new Greeting(text.trim());
    }

    public String getText() {
        return text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Greeting greeting = (Greeting) o;
        return Objects.equals(text, greeting.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(text);
    }

    @Override
    public String toString() {
        return text;
    }
}
