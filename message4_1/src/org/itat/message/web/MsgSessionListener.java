package org.itat.message.web;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

public class MsgSessionListener implements HttpSessionListener {

	@Override
	public void sessionCreated(HttpSessionEvent hse) {
		MsgSessionContext.addSession(hse.getSession());
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent hse) {
		MsgSessionContext.removeSession(hse.getSession().getId());
	}

}
