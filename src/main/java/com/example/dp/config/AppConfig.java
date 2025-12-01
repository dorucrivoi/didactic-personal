package com.example.dp.config;

import com.example.dp.filter.HttpServletRequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.handler.MappedInterceptor;

@Configuration
public class AppConfig {

    private final HttpServletRequestInterceptor requestInterceptor;

    public AppConfig(final HttpServletRequestInterceptor requestInterceptor){
        this.requestInterceptor = requestInterceptor;
    }

    @Bean
    MappedInterceptor interceptor(){
        return new MappedInterceptor(new String[]{"/api/**"}, requestInterceptor);
    }

}
