package com.weifengqin.interceptor;


import com.weifengqin.dto.UserDto;
import com.weifengqin.utils.UserHolder;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

/**
 * @author qin start
 * @create 2023-10-18-15:12
 */
public class LoginInterceptor implements HandlerInterceptor {




    /**
     * 拦截请求之前
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        UserDto userDTO = UserHolder.getUser();

        if(Objects.isNull(userDTO)){
            response.setStatus(401);
            return false;
        }

        // 有用户就放行
        return true;

    }

    /**
     * 处理完请求之后，销毁对象
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserHolder.removeUser();
    }
}
