package com.atguigu.spzx.gateway.filter;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson2.JSON;
import com.alibaba.nacos.common.utils.StringUtils;
import com.atguigu.spzx.model.entity.user.UserInfo;
import com.atguigu.spzx.model.vo.common.Result;
import com.atguigu.spzx.model.vo.common.ResultCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.List;


/**
 * <p>
 * 全局Filter，统一处理会员登录
 * </p>
 */
@Slf4j
@Component
public class AuthGlobalFilter implements GlobalFilter, Ordered {

    @Autowired
    private RedisTemplate<String , String> redisTemplate;

    private AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //获取请求对象
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();
        log.info("path {}" + path);

        //包含以下路径就需要登录才能访问
        if (antPathMatcher.match("/api/**/auth/**", path)) {
            UserInfo userInfo = getUserInfo(request);
            if (userInfo==null){
                log.info("没有权限");
                ServerHttpResponse response = exchange.getResponse();
                return out(response,ResultCodeEnum.LOGIN_AUTH);
            }
        }
        //放行
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }

    private Mono<Void> out(ServerHttpResponse response, ResultCodeEnum resultCodeEnum) {
        Result result = Result.build(null, resultCodeEnum);
        byte[] bits = JSONObject.toJSONString(result).getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = response.bufferFactory().wrap(bits);
        //指定编码，否则在浏览器中会中文乱码
        response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
        return response.writeWith(Mono.just(buffer));
    }

    private UserInfo getUserInfo(ServerHttpRequest request) {
        //登录接口，直接放行
        HttpHeaders headers = request.getHeaders();
        List<String> tokenList = headers.get("token");
        String token = "";
        if (tokenList != null) {
            token = tokenList.get(0);
        }
//            利用token从redis中获取信息
        if (!StringUtils.isEmpty(token)) {
            //返回的是json结果
            String userInfoJson = redisTemplate.opsForValue().get("user:login:" + token);
            UserInfo userInfo = JSON.parseObject(userInfoJson, UserInfo.class);
            return userInfo;
        }
        return null;

    }
}