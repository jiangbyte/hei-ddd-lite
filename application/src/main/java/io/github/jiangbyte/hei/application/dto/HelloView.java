package io.github.jiangbyte.hei.application.dto;

/**
 * Hello 用例读模型（应用层视图，供接口层组装响应）。
 */
public class HelloView {

    private final Long id;
    private final String greetingText;

    public HelloView(Long id, String greetingText) {
        this.id = id;
        this.greetingText = greetingText;
    }

    public Long getId() {
        return id;
    }

    public String getGreetingText() {
        return greetingText;
    }
}
