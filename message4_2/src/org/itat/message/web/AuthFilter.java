package org.itat.message.web;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.itat.message.util.AuthUtil;
import org.itat.message.vo.User;

public class AuthFilter implements Filter {

	public void destroy() {

	}

	@SuppressWarnings("unchecked")
	public void doFilter(ServletRequest req, ServletResponse resp,
			FilterChain chain) throws IOException, ServletException {
		//访问任意页面时先通过servletContext获取权限信息，进行相应的判断
		//1、获取要访问的路径
		HttpServletRequest hReq = (HttpServletRequest)req;
		HttpServletResponse hResp = (HttpServletResponse)resp;
		String path = hReq.getRequestURI();
		String rPath = path.substring(hReq.getContextPath().length());
		String sessionId = req.getParameter("jsessionId");
		User loginUser = (User)hReq.getSession().getAttribute("loginUser");
		/**
		 * 专门用来判断uploadify等flash的上传组件的
		 */
		if(loginUser==null&&sessionId!=null&&!"".equals(sessionId.trim())) {
			loginUser = (User)MsgSessionContext.getSession(sessionId).getAttribute("loginUser");
		}
		int roleId = 0;
		if(loginUser!=null) {
			roleId = loginUser.getType();
		}
		boolean canAccess = AuthUtil.checkAuth(roleId, 
				(Map<Integer,List<String>>)hReq.getSession().getServletContext().getAttribute("auths"), 
				rPath);
		if(!canAccess) {
			hResp.sendRedirect("user_authError.action");
			return;
		}
		chain.doFilter(req, hResp);
	}

	public void init(FilterConfig config) throws ServletException {

	}

}
