package com.aoin.bookstore.web.filter;

import java.io.*;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.*;

import com.aoin.bookstore.dao.User;

@WebFilter(filterName="AdminPrivilegeFilter",urlPatterns= {"/admin/*"})
public class AdminPrivilegeFilter implements Filter{

	@Override
	public void init(FilterConfig filterconfig) throws ServletException {
	}
	
	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;
		
		User user = (User) request.getSession().getAttribute("user");
		if(user != null && "超级用户".equals(user.getRole())) {
			chain.doFilter(request, response);
			return;
		}
		response.sendRedirect(request.getContextPath()+"/error/privilege.jsp");
	}

	@Override
	public void destroy() {
	}
}
