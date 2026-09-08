package io.github.jiangbyte.hei.interfaces.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * JWT 令牌工具：签发与解析。
 */
public class JwtTokenProvider {

    private final JwtProperties properties;
    private final SecretKey secretKey;

    public JwtTokenProvider(JwtProperties properties) {
        this.properties = properties;
        this.secretKey = Keys.hmacShaKeyFor(properties.getSecret().getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 签发 Token。
     *
     * @param userId   用户 ID
     * @param username 用户名
     * @return JWT 字符串
     */
    public String createToken(String userId, String username) {
        Date now = new Date();
        Date expireAt = new Date(now.getTime() + properties.getExpireSeconds() * 1000L);
        return Jwts.builder()
                .issuer(properties.getIssuer())
                .subject(userId)
                .claim("username", username)
                .issuedAt(now)
                .expiration(expireAt)
                .signWith(secretKey)
                .compact();
    }

    /**
     * 解析 Token 为登录用户；失败抛出 UnauthorizedException。
     */
    public LoginUser parseToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(secretKey)
                    .requireIssuer(properties.getIssuer())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            return LoginUser.builder()
                    .userId(claims.getSubject())
                    .username(claims.get("username", String.class))
                    .build();
        } catch (Exception ex) {
            throw new UnauthorizedException("Token 无效或已过期");
        }
    }
}
