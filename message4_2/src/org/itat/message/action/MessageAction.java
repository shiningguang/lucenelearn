package org.itat.message.action;

import javax.annotation.Resource;

import org.itat.message.iservice.ICommentService;
import org.itat.message.iservice.IMessageService;
import org.itat.message.util.ActionUtil;
import org.itat.message.util.PageObject;
import org.itat.message.util.SystemContext;
import org.itat.message.vo.Message;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

@Controller("messageAction")
@Scope("prototype")
public class MessageAction extends ActionSupport implements ModelDriven<Message>{
	private static final long serialVersionUID = -3266057605774486873L;
	private IMessageService messageService;
	private ICommentService commentService;
	private Message message;
	private String type;
	private String keyword;
	private int userId;
	private Integer[] myatt;
	
	

	public Message getMessage() {
		return message;
	}
	public void setMessage(Message message) {
		this.message = message;
	}
	public ICommentService getCommentService() {
		return commentService;
	}
	@Resource
	public void setCommentService(ICommentService commentService) {
		this.commentService = commentService;
	}

	public Integer[] getMyatt() {
		return myatt;
	}

	public void setMyatt(Integer[] myatt) {
		this.myatt = myatt;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public IMessageService getMessageService() {
		return messageService;
	}

	@Resource
	public void setMessageService(IMessageService messageService) {
		this.messageService = messageService;
	}


	public String list() {
		PageObject<Message> pages = null;
		SystemContext.setSort("m.createDate");
		SystemContext.setOrder("desc");
		if("title".equals(type)) {
			pages = messageService.findByTitle(keyword);
		} else if("content".equals(type)) {
			pages = messageService.findByContent(keyword);
		} else {
			pages = messageService.findByTitle(null);
		}
		ActionContext.getContext().put("pages", pages);
		return ActionUtil.MSG_LIST;
	}


	public Message getModel() {
		if(message==null) message = new Message();
		return message;
	}
	
	public String addInput() {
		return SUCCESS;
	}
	
	public String add() {
		messageService.add(message, userId,myatt);
		ActionContext.getContext().put("urlAction", "message_list.action");
		return ActionUtil.REDIRECT;
	}
	
	public String show() {
		Message tm = messageService.load(message.getId());
		setMessage(tm);
		ActionContext.getContext().put("comments", commentService.findByMsg(message.getId()));
		return SUCCESS;
	}
}
