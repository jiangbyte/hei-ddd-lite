package io.github.jiangbyte.hei.interfaces.web;

import cn.hutool.core.util.StrUtil;
import io.github.jiangbyte.hei.interfaces.response.R;
import io.github.jiangbyte.hei.interfaces.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 认证接口占位。
 * <p>
 * 职责：演示 JWT 签发；真实项目应校验账号密码后再发 Token。
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 登录占位：任意非空用户名即可签发 Token（仅用于骨架演示）。
     */
    @PostMapping("/login")
    public R<Map<String, Object>> login(@RequestParam(defaultValue = "admin") String username) {
        if (StrUtil.isBlank(username)) {
            return R.fail("VALIDATION_ERROR", "用户名不能为空");
        }
        // 占位：真实项目用用户主键；此处用用户名充当 userId
        String token = jwtTokenProvider.createToken(username, username);
        Map<String, Object> data = new HashMap<>();
        data.put("token", token);
        data.put("tokenType", "Bearer");
        data.put("username", username);
        return R.ok(data);
    }
}
