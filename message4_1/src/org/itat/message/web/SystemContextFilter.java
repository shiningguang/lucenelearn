package org.itat.message.web;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.itat.message.util.SystemContext;

public class SystemContextFilter implements Filter {
	int pageIndex = 0;
	int pageSize = 0;

	@Override
	public void destroy() {

	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp,
			FilterChain chain) throws IOException, ServletException {
	//获取用户列表
		try{
			int pageOffset = 0;
			try {
				pageOffset = Integer.parseInt(req.getParameter("pager.offset"));
			} catch (NumberFormatException e) {
			}
			String sort = req.getParameter("sort");
			String order = req.getParameter("order");
			SystemContext.setPageSize(pageSize);
			SystemContext.setPageOffset(pageOffset);
			SystemContext.setOrder(order);
			SystemContext.setSort(sort);
			SystemContext.setRealPath(((HttpServletRequest)req).getSession().getServletContext().getRealPath(""));
			chain.doFilter(req, resp);
		} finally {
			SystemContext.removePageOffset();
			SystemContext.removePageSize();
			SystemContext.removeOrder();
			SystemContext.removeSort();
		}
	}

	@Override
	public void init(FilterConfig conf) throws ServletException {
		if(conf.getInitParameter("pageSize")!=null) {
			pageSize = Integer.parseInt(conf.getInitParameter("pageSize"));
		} else {
			pageSize = 15;
		}
		
		if(conf.getInitParameter("pageIndex")!=null) {
			pageIndex = Integer.parseInt(conf.getInitParameter("pageIndex"));
		} else {
			pageIndex = 1;
		}
	}

}
