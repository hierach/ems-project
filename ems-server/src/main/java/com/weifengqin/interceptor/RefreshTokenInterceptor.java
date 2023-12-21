package com.weifengqin.interceptor;

import cn.hutool.core.bean.BeanUtil;

import com.weifengqin.dto.UserDto;
import com.weifengqin.utils.RedisConstants;
import com.weifengqin.utils.UserHolder;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author qin start
 * @create 2023-10-18-17:38
 */
public class RefreshTokenInterceptor implements HandlerInterceptor {


    private StringRedisTemplate redisTemplate;


    public RefreshTokenInterceptor(StringRedisTemplate stringRedisTemplate) {
        this.redisTemplate = stringRedisTemplate;
    }

    /**
     * 刷新用户信息，redis
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //1.从token中获取user信息
        String token = request.getHeader("Authorization");

        if(!StringUtils.hasText(token)){
            // 直接放行
            return true;
        }

        Map<Object, Object> userMap = redisTemplate.opsForHash().entries(RedisConstants.LOGIN_USER_KEY + token);

        //2.不能获取到，直接放行
        if(Objects.isNull(userMap)){
            return true;
        }

        //3.能获取到存入threadLocal，并放行
        UserDto userDTO = BeanUtil.fillBeanWithMap(userMap, new UserDto(), false);
        UserHolder.saveUser(userDTO);

        //4.刷新token有效期
        redisTemplate.expire(RedisConstants.LOGIN_USER_KEY + token,RedisConstants.LOGIN_USER_TTL, TimeUnit.MINUTES);


        return true;
    }
}
