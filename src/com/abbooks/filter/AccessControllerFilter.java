package com.abbooks.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

public class AccessControllerFilter implements Filter {

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void doFilter(ServletRequest arg0, ServletResponse arg1, FilterChain arg2)
			throws IOException, ServletException {
//		System.out.println("跨域请求设置");
		if(arg1 instanceof HttpServletResponse){
			HttpServletResponse response=(HttpServletResponse) arg1;
			response.addHeader("Access-Control-Allow-Origin", "*");
			response.addHeader("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE");
    	response.addHeader("Access-Control-Allow-Headers", "Content-Type, Authorization, Accept,X-Requested-With");
			
		}
		arg2.doFilter(arg0, arg1);
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {

	}

}
