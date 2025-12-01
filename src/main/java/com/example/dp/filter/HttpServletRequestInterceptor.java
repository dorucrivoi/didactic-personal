package com.example.dp.filter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

@Component
public class HttpServletRequestInterceptor implements HandlerInterceptor {

    public static final String CORRELATION_ID = "X-Correlation-Id";
    private RequestContext requestContext;

    public HttpServletRequestInterceptor(RequestContext requestContext){
        this.requestContext = requestContext;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        String correlationId =  request.getHeader(CORRELATION_ID);
        if (correlationId == null || correlationId.isBlank()) {
            correlationId = UUID.randomUUID().toString();
        }
        // Put into MDC for logging
        MDC.put(CORRELATION_ID, correlationId);
        requestContext.setCorrelationId(correlationId);
        // Add to response
        response.setHeader(CORRELATION_ID, correlationId);

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
        requestContext.unloadCorrelationId();
        MDC.remove(CORRELATION_ID);
    }
}
