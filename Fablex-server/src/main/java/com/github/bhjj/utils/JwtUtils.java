package com.github.bhjj.utils;

import com.github.bhjj.properties.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Objects;

/**
 * @author ZhangXianDuo
 * @date 2025/3/9
 */
@Component
@Slf4j
public class JwtUtils {

    @Autowired
    private JwtProperties jwtProperties;
    /**
     * 定义系统标识头常量(指定的系统发放令牌，实现颗粒化给予权限)
     */
    private static final String HEADER_SYSTEM_KEY = "systemKeyHeader";

    /**
     * 根据用户ID生成JWT
     *
     * @param uid       用户ID
     * @param systemKey 系统标识
     * @return JWT
     */
    public String generateToken(Long uid, String systemKey) {
        return Jwts.builder()
                .setHeaderParam(HEADER_SYSTEM_KEY, systemKey)
                .setSubject(uid.toString())
                .setExpiration(new Date(System.currentTimeMillis() + jwtProperties.getExpire()))
                .signWith(Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8)))
                .compact();
    }

    /**
     * 解析JWT返回用户ID
     *
     * @param token     JWT
     * @param systemKey 系统标识
     * @return 用户ID
     */
    public Long parseToken(String token, String systemKey) {
        Jws<Claims> claimsJws;
        try {
            claimsJws = Jwts.parser()
                    .setSigningKey(Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8)))
                    .build()
                    .parseClaimsJws(token);
            // OK, we can trust this JWT
            // 判断该 JWT 是否属于指定系统
            if (Objects.equals(claimsJws.getHeader().get(HEADER_SYSTEM_KEY), systemKey)) {
                return Long.parseLong(claimsJws.getBody().getSubject());
            }
        } catch (JwtException e) {
            log.warn("JWT解析失败:{}", token);
            // don't trust the JWT!
        }
        return null;
    }

}