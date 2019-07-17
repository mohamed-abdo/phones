package com.softideas.phones.config;


import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

@Component
public class ResponseHeader implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        var response = (HttpServletResponse) servletResponse;
        response.setHeader("Content-Type", "application/json");
        response.setHeader("request-id", UUID.randomUUID().toString());
        filterChain.doFilter(servletRequest, response);
    }
}

