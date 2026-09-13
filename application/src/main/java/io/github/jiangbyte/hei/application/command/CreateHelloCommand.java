package io.github.jiangbyte.hei.application.command;

import io.github.jiangbyte.hei.application.core.Command;

/**
 * 创建 Hello 写用例入参。
 */
public class CreateHelloCommand implements Command {

    private final String greetingText;

    public CreateHelloCommand(String greetingText) {
        this.greetingText = greetingText;
    }

    public String getGreetingText() {
        return greetingText;
    }
}
