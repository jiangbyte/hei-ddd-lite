package io.github.jiangbyte.hei.interfaces.web;

import io.github.jiangbyte.hei.application.HelloApplicationService;
import io.github.jiangbyte.hei.interfaces.response.R;
import io.github.jiangbyte.hei.interfaces.security.AuthContext;
import io.github.jiangbyte.hei.interfaces.security.RequireLogin;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Hello Web 接口占位。
 * <p>
 * 职责：接收 HTTP 请求，调用应用服务，返回统一响应；不写业务规则。
 */
@RestController
@RequiredArgsConstructor
public class HelloController {

    private final HelloApplicationService helloApplicationService;

    /**
     * 问候接口（公开）：演示接口层 → 应用层调用链。
     */
    @GetMapping("/hello")
    public R<String> hello() {
        return R.ok(helloApplicationService.greet());
    }

    /**
     * 需登录的问候接口：演示 {@link RequireLogin} + JWT 校验。
     */
    @RequireLogin
    @GetMapping("/hello/secure")
    public R<String> secureHello() {
        String username = AuthContext.get() == null ? "unknown" : AuthContext.get().getUsername();
        return R.ok(helloApplicationService.greet() + ", " + username);
    }
}
