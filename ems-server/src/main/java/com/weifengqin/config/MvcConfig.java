package com.weifengqin.config;


import com.weifengqin.interceptor.LoginInterceptor;
import com.weifengqin.interceptor.RefreshTokenInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author qin start
 * @create 2023-10-18-15:24
 */
@Configuration
public class MvcConfig implements WebMvcConfigurer {


    @Autowired
    private StringRedisTemplate redisTemplate;



    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        LoginInterceptor loginInterceptor = new LoginInterceptor();
        RefreshTokenInterceptor refreshTokenInterceptor = new RefreshTokenInterceptor(redisTemplate);




        // 默认order都是0，按添加顺序来。
        // 但是order值越大越靠后
        registry.addInterceptor(loginInterceptor)
                .excludePathPatterns(
                        "/login",
                        "/swagger-ui.html",  // 排除Swagger UI路径
                        "/webjars/**",       // 排除Swagger UI使用的静态资源路径
                        "/v2/api-docs",      // 排除Swagger API文档路径
                        "/swagger-resources/**", // 排除Swagger资源路径,
                        "/doc.html"
                ).order(2);
        registry.addInterceptor(refreshTokenInterceptor).order(1);

    }

    //解决跨域请求
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 设置允许跨域的路径
        registry.addMapping("/**")
                // 设置允许跨域请求的域名
                .allowedOriginPatterns("*")
                // 是否允许cookie
                .allowCredentials(true)
                // 设置允许的请求方式
                .allowedMethods("*")
                // 设置允许的header属性
                .allowedHeaders("*","authorization")
                // 跨域允许时间
                .maxAge(3600);
    }

    //让cors高于拦截器的权限
    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOriginPattern("*");
        config.setAllowCredentials(true);
        config.addAllowedMethod("*");
        config.addAllowedHeader("*");
        UrlBasedCorsConfigurationSource configSource = new UrlBasedCorsConfigurationSource();
        configSource.registerCorsConfiguration("/**", config);
        return new CorsFilter(configSource);
    }






}
