package org.itat.message.action;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.ServletActionContext;
import org.itat.message.iservice.IAttachmentService;
import org.itat.message.iservice.ICommentService;
import org.itat.message.iservice.IUserService;
import org.itat.message.util.ActionUtil;
import org.itat.message.util.AuthUtil;
import org.itat.message.vo.Attachment;
import org.itat.message.vo.Comment;
import org.itat.message.vo.TranObj;
import org.itat.message.vo.User;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;

@Scope("prototype")
@Controller("jsonAction")
public class JsonAction {
	private String username;
	private String password;
	private int id;
	private String utype;
	private String uvalue;
	private File attach;
	private String attachFileName;
	private String attachContentType;
	private int userId;
	private int messageId;
	private String  content;
	private ICommentService commentService;
	private IAttachmentService attachmentService;
	
	
	
	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getMessageId() {
		return messageId;
	}

	public void setMessageId(int messageId) {
		this.messageId = messageId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public ICommentService getCommentService() {
		return commentService;
	}

	@Resource
	public void setCommentService(ICommentService commentService) {
		this.commentService = commentService;
	}

	public IAttachmentService getAttachmentService() {
		return attachmentService;
	}
	
	@Resource
	public void setAttachmentService(IAttachmentService attachmentService) {
		this.attachmentService = attachmentService;
	}
	public File getAttach() {
		return attach;
	}
	public void setAttach(File attach) {
		this.attach = attach;
	}
	public String getAttachFileName() {
		return attachFileName;
	}
	public void setAttachFileName(String attachFileName) {
		this.attachFileName = attachFileName;
	}
	public String getAttachContentType() {
		return attachContentType;
	}
	public void setAttachContentType(String attachContentType) {
		this.attachContentType = attachContentType;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUtype() {
		return utype;
	}
	public void setUtype(String utype) {
		this.utype = utype;
	}
	public String getUvalue() {
		return uvalue;
	}
	public void setUvalue(String uvalue) {
		this.uvalue = uvalue;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	private IUserService userService;
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}

	public IUserService getUserService() {
		return userService;
	}

	@Resource
	public void setUserService(IUserService userService) {
		this.userService = userService;
	}
	
	public void authRefresh() throws IOException {
		TranObj to = new TranObj();
		try {
			Map<Integer,List<String>> auths = AuthUtil.initAuth();
			ServletActionContext.getRequest().
					getSession().getServletContext().setAttribute("auths", auths);
			to.setSuccess();
			to.setMsg("权限刷新成功!");
			System.out.println("初始化权限成功!!!!!!!");
			System.out.println(auths);
		} catch (Exception e) {
			to.setFailure();
			to.setMsg(e.getMessage());
		}
		ActionUtil.sendJson(to, ServletActionContext.getResponse());
		
	}

	public void checkUser() throws IOException {
		boolean e = userService.checkUser(getUsername());
		TranObj to = null;
		if(e) {
			to = new TranObj(TranObj.FAILURE,"用户已经存在");
		} else {
			to = new TranObj(TranObj.SUCCESS,"用户可以添加");
		}
		ActionUtil.sendJson(to, ServletActionContext.getResponse());
	}
	
	public void login() throws IOException {
		User loginUser = null;
		TranObj to = new TranObj();
		try {
			loginUser = userService.login(username,password);
			to.setSuccess();
			to.setMsg("用户登录成功!");
			ActionContext.getContext().getSession().put("loginUser",loginUser);
		} catch (Exception e) {
			to.setFailure();
			to.setMsg(e.getMessage());
		}
		ActionUtil.sendJson(to, ServletActionContext.getResponse());
	}
	
	public void updateUser() throws IOException {
		TranObj to = new TranObj();
		try {
			User user = userService.load(id);
			user.setProperty(utype, uvalue);
			userService.update(user);
			to.setSuccess();
		} catch (Exception e) {
			to.setFailure();
			to.setMsg(e.getMessage());
		}
		ActionUtil.sendJson(to, ServletActionContext.getResponse());
	}
	
	public void deleteAttach() throws IOException {
		TranObj to = new TranObj();
		try {
			attachmentService.deleteById(id);
			to.setSuccess();
		} catch (Exception e) {
			to.setFailure();
			to.setMsg(e.getMessage());
		}
		ActionUtil.sendJson(to, ServletActionContext.getResponse());
	}
	
	public void uploadAttach() throws IOException {
		TranObj to = null;
		try {
			Attachment attachment = new Attachment();
			attachment.setContentType(attachContentType);
			attachment.setOldName(attachFileName);
			attachment = attachmentService.add(attachment, attach);
			to = new TranObj();
			if(attachment==null) {
				to.setFailure();
				to.setMsg("添加附件对象失败!");
			} else {
				to.setSuccess();
				to.setObj(attachment);
			}
		} catch (Exception e) {
			to.setFailure();
			to.setMsg(e.getMessage());
		}
		ActionUtil.sendJson(to, ServletActionContext.getResponse());
	}
	
	public void commentAdd() throws IOException {
		TranObj to = null;
		try {
			Comment comment = new Comment();
			comment.setContent(content);
			comment.setCreateDate(new Date());
			comment = commentService.add(comment, userId, messageId);
			to = new TranObj();
			if(comment!=null) {
				to.setSuccess();
				to.setObj(comment);
			} else {
				to.setFailure();
				to.setMsg("添加评论失败");
			}
		} catch (Exception e) {
			to.setFailure();
			to.setMsg(e.getMessage());
		}
		ActionUtil.sendJson(to, ServletActionContext.getResponse());
	}
}
