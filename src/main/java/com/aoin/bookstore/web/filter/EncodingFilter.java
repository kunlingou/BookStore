package com.aoin.bookstore.web.filter;

import java.io.*;
import java.util.Map;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.*;
@WebFilter(filterName="EncodingFilter",urlPatterns= {"/*"})
public class EncodingFilter implements Filter{
	@Override
	public void init(FilterConfig filterconfig) throws ServletException {
	}
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest myrequest = new MyRequest((HttpServletRequest) request);
		response.setContentType("text/html;charset=utf-8");
		chain.doFilter(myrequest,response);
	}
	
	@Override
	public void destroy() {
	}
}

class MyRequest extends HttpServletRequestWrapper{

	private HttpServletRequest request;
	private boolean hasEncode;
	public MyRequest(HttpServletRequest request) {
		super(request);
		this.request = request;
	}
	
	@Override
	public String getParameter(String name) {
		Map<String, String[]> parameterMap = getParameterMap();
		String[] values = parameterMap.get(name);
		if(values == null) {
			return null;
		}
		return values[0];
	}

	@Override
	public Map<String, String[]> getParameterMap() {
		String method = request.getMethod();
		if(method.equalsIgnoreCase("post")) {
			//post
			try {
				request.setCharacterEncoding("utf-8");
				return request.getParameterMap();
			}catch(UnsupportedEncodingException e){
				e.printStackTrace();
			}
		}else if(method.equalsIgnoreCase("get")) {
			//get
			Map<String, String[]> parameterMap = getParameterMap();
			if(!hasEncode) {//确保get手动调整只运行一次
				for(String parameterName : parameterMap.keySet()) {
					String[] values = parameterMap.get(parameterName);
					if(values != null) {
						for(int i=0;i<values.length;i++) {
							try {
								values[i] = new String(values[i].getBytes("ISO-8859-1"),"utf-8");
							}catch(UnsupportedEncodingException e){
								e.printStackTrace();
							}
						}
					}
				}
				hasEncode = true;
			}
			return parameterMap;
		}
		return super.getParameterMap();
	}

	@Override
	public String[] getParameterValues(String name) {
		Map<String, String[]> parameterMap = getParameterMap();
		String[] values = parameterMap.get(name);
		return values;
	}

}
