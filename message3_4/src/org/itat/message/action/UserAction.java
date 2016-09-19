package org.itat.message.action;


import java.io.UnsupportedEncodingException;

import javax.annotation.Resource;

import org.apache.struts2.ServletActionContext;
import org.itat.message.iservice.IUserService;
import org.itat.message.util.ActionUtil;
import org.itat.message.vo.User;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

@Controller("userAction")
@Scope("prototype")
public class UserAction extends ActionSupport implements ModelDriven<User>{
	private static final long serialVersionUID = -1831131402578214891L;
	private User user;
	private IUserService userService;
	private String confirmPwd;
	private String oldPwd;
	

	public String getOldPwd() {
		return oldPwd;
	}
	public void setOldPwd(String oldPwd) {
		this.oldPwd = oldPwd;
	}
	public String getConfirmPwd() {
		return confirmPwd;
	}
	public void setConfirmPwd(String confirmPwd) {
		this.confirmPwd = confirmPwd;
	}
	public IUserService getUserService() {
		return userService;
	}
	@Resource
	public void setUserService(IUserService userService) {
		this.userService = userService;
	}


	public User getModel() {
		if(user==null) user = new User();
		return user;
	}
	
	
	public String addInput() {
		return Action.SUCCESS;
	}
	
	public String add() {
		user.setHiddenMsg(0);
		user.setStatus(0);
		user.setType(1);
		userService.add(user);
		ActionContext.getContext().put("urlAction", "user_loginInput.action");
		return ActionUtil.REDIRECT;
	}
	
	public void validateAdd() {
		checkUsernameAndPassword();
		if(!user.getPassword().equals(confirmPwd)) {
			this.addFieldError("confirmPwd","两次输入的密码不正确!");
		}
	}
	
	public String updatePasswordInput() {
		return SUCCESS;
	}
	
	public String updatePassword() {
		User tu = userService.load(user.getId());
		if(!tu.getPassword().equals(oldPwd)) {
			this.addFieldError("password","原始密码不正确");
			return INPUT;
		}
		tu.setPassword(user.getPassword());
		userService.update(tu);
		ActionContext.getContext().put("urlAction", "message_list.action");
		return ActionUtil.REDIRECT;
	}
	
	public String loginInput() {
		return Action.LOGIN;
	}
	
	public String login() {
		User loginUser = userService.login(user.getUsername(), user.getPassword());
		ActionContext.getContext().getSession().put("loginUser",loginUser);
		ActionContext.getContext().put("urlAction", "message_list.action");
		return ActionUtil.REDIRECT;
	}
	
	public String list() throws UnsupportedEncodingException {
		ServletActionContext.getRequest().setCharacterEncoding("UTF-8");
		ActionContext.getContext().put("pages", userService.find(user.getUsername()));
		return SUCCESS;
	}
	
	public String show() {
		User u = userService.load(user.getId());
		ActionContext.getContext().put("user", u);
		return SUCCESS;
	}
	
	public void validateLogin() {
		checkUsernameAndPassword();
	}
	
	public String logout() {
		ActionContext.getContext().getSession().remove("loginUser");
		ActionContext.getContext().put("urlAction", "user_loginInput.action");
		return ActionUtil.REDIRECT;
	}
	
	private void checkUsernameAndPassword() {
		if(user.getUsername()==null||"".equals(user.getUsername().trim())) {
			this.addFieldError("username","用户名不能为空");
		}
		if(user.getPassword()==null||"".equals(user.getPassword().trim())) {
			this.addFieldError("password","用户名密码不能为空");
		}
	}
	
	public String authError() {
		return SUCCESS;
	}
}
