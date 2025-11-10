package com.springcloud.msvc_payments.infrastructure.integration.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
public class FeignRequestInterceptor implements RequestInterceptor {

    private static final String USER_ID_HEADER = "X-User-ID";
    private static final String USER_ROLES_HEADER = "X-User-Roles";
    private static final String USER_EMAIL_HEADER = "X-User-Email";

    @Override
    public void apply(RequestTemplate template) {
        ServletRequestAttributes attributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            String[] headersToForward = {USER_ID_HEADER, USER_ROLES_HEADER, USER_EMAIL_HEADER};

            for (String headerName : headersToForward) {
                String headerValue = request.getHeader(headerName);
                if (headerValue != null) {
                    template.header(headerName, headerValue);
                }
            }
        }
    }
}
