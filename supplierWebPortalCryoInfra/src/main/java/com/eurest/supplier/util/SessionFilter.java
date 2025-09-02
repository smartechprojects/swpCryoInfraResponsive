package com.eurest.supplier.util;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet Filter implementation class SessionFilter
 */

public class SessionFilter implements Filter {
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest peticion = (HttpServletRequest) request;
		HttpSession session = peticion.getSession(false);
		HttpServletResponse respuesta = (HttpServletResponse) response;
		
		String path = ((HttpServletRequest) request).getRequestURI();
		if (path.contains("login")) {
			chain.doFilter(request, response);
		}else {
			if (session != null) {
				chain.doFilter(request, response);
			} else {
				respuesta.sendRedirect("login");
			}
		}
	}

	@Override
	public void destroy() {
// TODO Auto-generated method stub
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
// TODO Auto-generated method stub
	}
}