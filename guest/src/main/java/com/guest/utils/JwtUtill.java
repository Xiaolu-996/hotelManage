package com.guest.utils;

import com.alibaba.druid.util.StringUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
/**
 * <p>
 *  Jwt工具类
 * </p>
 *
 * @author 张雪萍
 * @since 2020-11-27
 */
@Data
@Component
@AllArgsConstructor
@NoArgsConstructor
//通过application.yaml为该类的key和ttl赋值
@ConfigurationProperties(prefix = "jwtutill")
public class JwtUtill {
    /**签名私钥 */
    private String key;
    /**签名的失效时间 */
    private Long ttl;

    /**
     * 创建token
     * @param stuNumber 用户名，对应user中的stuNumber
     * @return 返回生成的token字符串
     */
    public String createJwt(String stuNumber){
        //设置失效时间，未使用，待确认
        long now = System.currentTimeMillis();//当前时间戳
        long exp = now + this.ttl;//失效时的时间戳
        //创建jwtBuilder
        JwtBuilder jwtBuilder =
                Jwts.builder()
                .setId(stuNumber)
                .setIssuedAt(new Date())
                .setExpiration(new Date(exp))
                .signWith(SignatureAlgorithm.HS256,key);
        //返回token
        return jwtBuilder.compact();
    }

    public String updateJwt(String stuNumber){
        //设置失效时间，未使用，待确认
        long now = System.currentTimeMillis();//当前时间戳
        long exp = now + this.ttl;//失效时的时间戳
        //创建jwtBuilder
        JwtBuilder jwtBuilder =
                Jwts.builder()
                .setId(stuNumber)
                .setIssuedAt(new Date())
                .setExpiration(new Date(exp))
                .signWith(SignatureAlgorithm.HS256,key);
        //返回token
        return jwtBuilder.compact();
    }
    /**
     * 获取token字符串
     * @return 生成的Claims
     */
    public String getToken(HttpServletRequest request){
        String authorization = request.getHeader("Authorization");
        if(! StringUtils.isEmpty(authorization)) {
            //如果authorization不为空，将其解析成token，处理成Claims，返回
            return authorization.replace("Bearer ", "");
        }
        return null;
    }

    /**
     * 解析token字符串，生成Claims，通过Claims可以获取用户的uid等
     * @return 生成的Claims
     */
    public Claims getClaims(HttpServletRequest request){
        return Jwts.parser().setSigningKey(key).parseClaimsJws(getToken(request)).getBody();
    }
}
