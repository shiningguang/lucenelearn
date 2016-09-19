package org.itat.message.util;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

public class ActionUtil {
	public final static String MSG_LIST = "message_list";
	public final static String REDIRECT = "redirect";
	
	public static void sendJson(Object obj,HttpServletResponse response) throws IOException {
		response.setContentType("text/html;charset=utf-8");
		String json = JsonJacksonUtil.getInstance().obj2json(obj);
//		System.out.println(json);
		response.getWriter().write(json);
	}
}	
