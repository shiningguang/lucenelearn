package org.itat.message.service;

import java.util.Date;

import javax.annotation.Resource;

import org.itat.message.exception.MessageException;
import org.itat.message.idao.ICommentDao;
import org.itat.message.idao.IMessageDao;
import org.itat.message.idao.IUserDao;
import org.itat.message.iservice.ICommentService;
import org.itat.message.util.PageObject;
import org.itat.message.vo.Comment;
import org.itat.message.vo.Message;
import org.itat.message.vo.User;
import org.springframework.stereotype.Service;

@Service("commentService")
public class CommentService implements ICommentService {
	private IMessageDao messageDao;
	private ICommentDao commentDao;
	private IUserDao  userDao;
	
	

	public IMessageDao getMessageDao() {
		return messageDao;
	}
	@Resource
	public void setMessageDao(IMessageDao messageDao) {
		this.messageDao = messageDao;
	}

	public ICommentDao getCommentDao() {
		return commentDao;
	}
	@Resource
	public void setCommentDao(ICommentDao commentDao) {
		this.commentDao = commentDao;
	}

	public IUserDao getUserDao() {
		return userDao;
	}
	@Resource
	public void setUserDao(IUserDao userDao) {
		this.userDao = userDao;
	}


	public Comment add(Comment c, int userId, int messageId) {
		User u = userDao.load(userId);
		if(u==null)throw new MessageException("要添加的评论用户不存在");
		Message m = messageDao.load(messageId);
		if(m==null)throw new MessageException("要添加的评论没有留言对象");
		c.setUser(u);
		c.setMessage(m);
		c.setCreateDate(new Date());
		return commentDao.add(c);
	}

	public void delete(int id) {
		commentDao.delete(id);
	}

	public PageObject<Comment> findByMsg(int messageId) {
		return commentDao.find("select c from Comment c join  c.message m join fetch c.user where m.id=?",messageId);
	}

	public PageObject<Comment> findByUser(int userId) {
		return commentDao.find("select c from Comment c join fetch c.user u join fech c.message  where u.id=?",userId);
	}

	public PageObject<Comment> findAllComment() {
		return commentDao.find("from Comment");
	}

}
