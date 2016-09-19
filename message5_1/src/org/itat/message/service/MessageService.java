package org.itat.message.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.itat.message.exception.MessageException;
import org.itat.message.idao.ICommentDao;
import org.itat.message.idao.IMessageDao;
import org.itat.message.idao.IUserDao;
import org.itat.message.iservice.IAttachmentService;
import org.itat.message.iservice.IIndexService;
import org.itat.message.iservice.IMessageService;
import org.itat.message.util.IndexUtil;
import org.itat.message.util.PageObject;
import org.itat.message.vo.Attachment;
import org.itat.message.vo.IndexField;
import org.itat.message.vo.Message;
import org.itat.message.vo.User;
import org.springframework.stereotype.Service;

@Service("messageService")
public class MessageService implements IMessageService {
	private IMessageDao messageDao;
	private ICommentDao commentDao;
	private IUserDao  userDao;
	private IAttachmentService attachmentService;
	private IIndexService indexService;

	public IIndexService getIndexService() {
		return indexService;
	}
	@Resource
	public void setIndexService(IIndexService indexService) {
		this.indexService = indexService;
	}
	public IAttachmentService getAttachmentService() {
		return attachmentService;
	}
	@Resource
	public void setAttachmentService(IAttachmentService attachmentService) {
		this.attachmentService = attachmentService;
	}
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

	public void add(Message m, int userId,Integer[] atts) {
		User u = userDao.load(userId);
		if(u==null) throw new MessageException("要添加留言的用户不存在!");
		m.setUser(u);
		m.setCreateDate(new Date());
		List<String> fieldContents = new ArrayList<String>();
		List<String> attsName = new ArrayList<String>();
		try {
			//把留言的内容添加到field的content
			fieldContents.add(new Tika().parseToString(new ByteArrayInputStream(m.getContent().getBytes())));
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TikaException e) {
			e.printStackTrace();
		}
		if(atts!=null) {
			for(Integer aid:atts) {
				Attachment att = attachmentService.load(aid);
				att.setMessage(m);
				attachmentService.update(att);
				IndexUtil.attach2Index(fieldContents,attsName,att);
			}
		}
		messageDao.add(m);
		IndexField field = IndexUtil.msg2IndexField(m);
		field.setAtths(attsName);
		field.setContent(fieldContents);
		indexService.addIndex(field,true);
	}

	public void update(Message m) {
		messageDao.update(m);
	}

	public void delete(int id) {
		indexService.deleteIndex("0_"+id, IndexUtil.MSG_TYPE);
		commentDao.deleteByMessage(id);
		attachmentService.deleteByMsg(id);
		messageDao.delete(id);
	}

	public Message load(int id) {
		return messageDao.load(id);
	}

	public List<Message> listByTitle(String keyword) {
		if(keyword==null||"".equals(keyword.trim())) {
			return messageDao.list("select m from Message m join fetch m.user");
		} else {
			return messageDao.list("select m from Message m join fetch m.user where m.title like ?"
					,"%"+keyword+"%");
		}
	}

	public List<Message> listByContent(String keyword) {
		if(keyword==null||"".equals(keyword.trim())) {
			return messageDao.list("select m from Message m join fetch m.user");
		} else {
			return messageDao.list("select m from Message m join fetch m.user where (m.title like ? or m.content like ?)"
					,new Object[]{"%"+keyword+"%","%"+keyword+"%"});
		}
	}

	public PageObject<Message> findByTitle(String keyword) {
		if(keyword==null||"".equals(keyword.trim())) {
			return messageDao.find("select m from Message m join fetch m.user");
		} else {
			return messageDao.find("select m from Message m join fetch m.user where m.title like ?"
					,"%"+keyword+"%");
		}
	}

	public PageObject<Message> findByContent(String keyword) {
		if(keyword==null||"".equals(keyword.trim())) {
			return messageDao.find("select m from Message m join fetch m.user");
		} else {
			return messageDao.find("select m from Message m join fetch m.user where (m.title like ? or m.content like ?)"
					,new Object[]{"%"+keyword+"%","%"+keyword+"%"});
		}
	}

	public List<Message> listByUser(int userId) {
		return messageDao.listByUser(userId);
	}

	public PageObject<Message> findByCondition(String condition) {
		return messageDao.find(condition);
	}

}
