package io.github.jiangbyte.hei.interfaces.response;

/**
 * Hello API 响应体。
 */
public class HelloResponse {

    private Long id;
    private String greetingText;

    public HelloResponse() {
    }

    public HelloResponse(Long id, String greetingText) {
        this.id = id;
        this.greetingText = greetingText;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGreetingText() {
        return greetingText;
    }

    public void setGreetingText(String greetingText) {
        this.greetingText = greetingText;
    }
}
