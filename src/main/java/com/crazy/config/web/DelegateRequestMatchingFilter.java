package com.crazy.config.web;

import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.*;
import java.io.IOException;

public class DelegateRequestMatchingFilter implements Filter {
    @SuppressWarnings("unused")
	private RequestMatcher[] ignoredRequests;

    public DelegateRequestMatchingFilter(RequestMatcher... matcher) {
        this.ignoredRequests = matcher;
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
         chain.doFilter(req,resp);
    }

	@Override
	public void destroy() {
		
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		
	}
}