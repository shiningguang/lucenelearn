package org.itat.message.service;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.itat.message.exception.MessageException;
import org.itat.message.idao.ICommentDao;
import org.itat.message.idao.IMessageDao;
import org.itat.message.idao.IUserDao;
import org.itat.message.iservice.IUserService;
import org.itat.message.util.PageObject;
import org.itat.message.vo.Message;
import org.itat.message.vo.User;
import org.springframework.stereotype.Service;

@Service("userService")
public class UserService implements IUserService {
	private IUserDao userDao;
	private IMessageDao messageDao;
	private ICommentDao commentDao;
	
	public ICommentDao getCommentDao() {
		return commentDao;
	}
	@Resource
	public void setCommentDao(ICommentDao commentDao) {
		this.commentDao = commentDao;
	}
	public IMessageDao getMessageDao() {
		return messageDao;
	}
	@Resource
	public void setMessageDao(IMessageDao messageDao) {
		this.messageDao = messageDao;
	}
	public IUserDao getUserDao() {
		return userDao;
	}
	@Resource
	public void setUserDao(IUserDao userDao) {
		this.userDao = userDao;
	}

	public void add(User u) {
		User tu = loadByUsername(u.getUsername());
		if(tu!=null) throw new MessageException("要添加的用户已经存在，不能添加");
		u.setCreateDate(new Date());
		userDao.add(u);
	}

	public void update(User u) {
		userDao.update(u);
	}

	public void delete(int id) {
		if(messageDao.listByUser(id).size()>0) {
			throw new MessageException("要删除的用户还有留言信息，不能删除");
		}
		commentDao.deleteByUser(id);
		userDao.delete(id);
	}

	public User load(int id) {
		return userDao.load(id);
	}

	public List<User> list() {
		return userDao.list("from User");
	}

	public User login(String username, String pwd) {
		User u = loadByUsername(username);
		if(u==null) throw new MessageException("用户不存在！");
		if(!u.getPassword().equals(pwd)) throw new MessageException("用户密码不正确！");
		if(u.getStatus()==1) throw new MessageException("该用户已经被停用，请与管理员联系");
		return u;
	}

	public PageObject<User> find(String name) {
		if(name==null||"".equals(name.trim())) {
			return userDao.find("from User");
		} else {
			return userDao.find("from User where nickname like ? or username like ?",
					new Object[]{"%"+name+"%","%"+name+"%"});
		}
	}

	public List<Message> listMsgByUser(int userId) {
		return messageDao.listByUser(userId);
	}
	public User loadByUsername(String username) {
		return userDao.loadByHql("from User where username=?",username);
	}
	public boolean checkUser(String username) {
		long count = (Long)userDao.loadObjByHQL("select count(*) from User where username=?",username);
		return count>0?true:false;
	}

}
