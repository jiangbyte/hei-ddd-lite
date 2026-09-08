package io.github.jiangbyte.hei.interfaces.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 登录校验拦截器：仅当 Handler 上存在 {@link RequireLogin} 时校验 JWT；
 * 未标注的接口默认公开，无需额外配置公开路径。
 */
public class LoginAuthInterceptor implements HandlerInterceptor {

    private final JwtProperties properties;
    private final JwtTokenProvider jwtTokenProvider;

    public LoginAuthInterceptor(JwtProperties properties, JwtTokenProvider jwtTokenProvider) {
        this.properties = properties;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (!(handler instanceof HandlerMethod handlerMethod)) {
            return true;
        }
        RequireLogin requireLogin = handlerMethod.getMethodAnnotation(RequireLogin.class);
        if (requireLogin == null) {
            requireLogin = handlerMethod.getBeanType().getAnnotation(RequireLogin.class);
        }
        // 无注解：默认公开
        if (requireLogin == null) {
            return true;
        }

        String header = request.getHeader(properties.getHeader());
        if (header == null || header.isBlank()) {
            throw new UnauthorizedException("未登录或缺少 Token");
        }
        String prefix = properties.getTokenPrefix();
        String token = header.startsWith(prefix) ? header.substring(prefix.length()).trim() : header.trim();
        if (token.isEmpty()) {
            throw new UnauthorizedException("未登录或 Token 为空");
        }

        LoginUser loginUser = jwtTokenProvider.parseToken(token);
        AuthContext.set(loginUser);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        AuthContext.clear();
    }
}
