package org.itat.message.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

public class MsgSessionContext {
	private final static Map<String,HttpSession> sessions = new HashMap<String,HttpSession>();

	public static synchronized void  addSession(HttpSession session) {
		if(session!=null) {
			sessions.put(session.getId(), session);
			System.out.println("添加了session:"+session.getId());
		}
	}
	
	public static synchronized void  removeSession(String sessionId) {
		if(sessionId!=null) {
			sessions.remove(sessionId);
			System.out.println("移除了session:"+sessionId);
		}
	}
	
	public static HttpSession getSession(String sessionId) {
		return sessions.get(sessionId);
	}
}
