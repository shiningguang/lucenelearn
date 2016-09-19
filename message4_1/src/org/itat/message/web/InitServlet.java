package org.itat.message.web;

import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.itat.message.util.AuthUtil;

public class InitServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = -24818954054590594L;

	@Override
	public void init() throws ServletException {
		Map<Integer,List<String>> auths = AuthUtil.initAuth();
		System.out.println("****************系统初始化完毕***********************");
		System.out.println(auths);
		//将 对象存储到servletContext(application)
		this.getServletContext().setAttribute("auths",auths);
		super.init();
	}
}
