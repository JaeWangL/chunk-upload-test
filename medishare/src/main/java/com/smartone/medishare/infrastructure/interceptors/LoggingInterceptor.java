package com.smartone.medishare.infrastructure.interceptors;

import com.smartone.medishare.shared.helpers.RandomHelpers;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class LoggingInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // Request 부분
        var correlationId = RandomHelpers.randomIdenfier();
        MDC.put("correlationId", correlationId);
        MDC.put("method", request.getMethod());
        MDC.put("uri", request.getRequestURI());
        MDC.put("userAgent", request.getHeader("User-Agent"));
        MDC.put("acceptLanguage", request.getHeader("Accept-Language"));

        request.setAttribute("correlationId", correlationId);
        request.setAttribute("startTime", System.currentTimeMillis());

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        // Response 부분
        Long startTime = (Long) request.getAttribute("startTime");
        long timeTaken = System.currentTimeMillis() - startTime;

        MDC.put("status", Integer.toString(response.getStatus()));
        MDC.put("timeTaken", Long.toString(timeTaken));

        if (ex != null) {
            // exception 이 존재하지 않을때만 info 로그 출력
            LoggerFactory.getLogger(this.getClass()).info("Request processed");
        }

        // 다른 Thread나 요청에 영향을 주지않게 하기위해 로그 출력 후 MDC 제거
        MDC.clear();
    }
}