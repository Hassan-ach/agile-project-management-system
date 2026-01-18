package com.ensa.agile.infrastructure.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Component
public class FilterChainExceptionHanlder extends OncePerRequestFilter {

    private HandlerExceptionResolver resolver;

    public FilterChainExceptionHanlder(@Qualifier("handlerExceptionResolver")
                                       HandlerExceptionResolver resolver) {
        this.resolver = resolver;
    }

    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
        throws IOException, ServletException {
        try {
            filterChain.doFilter(request, response);
        } catch (Exception ex) {
            resolver.resolveException(request, response, null, ex);
        }
    }
}
