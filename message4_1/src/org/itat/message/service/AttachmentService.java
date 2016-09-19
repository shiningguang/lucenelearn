package org.itat.message.service;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.itat.message.idao.IAttachmentDao;
import org.itat.message.idao.IMessageDao;
import org.itat.message.iservice.IAttachmentService;
import org.itat.message.util.SystemContext;
import org.itat.message.vo.Attachment;
import org.springframework.stereotype.Service;

@Service("attachmentService")
public class AttachmentService implements IAttachmentService {
	private IMessageDao messageDao;
	private IAttachmentDao attachmentDao;
	
	
	public IAttachmentDao getAttachmentDao() {
		return attachmentDao;
	}
	@Resource
	public void setAttachmentDao(IAttachmentDao attachmentDao) {
		this.attachmentDao = attachmentDao;
	}
	public IMessageDao getMessageDao() {
		return messageDao;
	}
	@Resource
	public void setMessageDao(IMessageDao messageDao) {
		this.messageDao = messageDao;
	}

	public Attachment add(Attachment attach,File attachFile) {
		//先添加对象，再进行文件的上传
		try {
			attach.setCreateDate(new Date());
			attach.setNewName(generatorNewFileName(attach.getOldName()));
			attachmentDao.add(attach);
			String realPath = SystemContext.getRealPath();
			FileUtils.copyFile(attachFile, new File(realPath+"/upload/"+attach.getNewName()));
			return attach;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void deleteById(int id) {
		attachmentDao.deletebyId(id);
	}

	public void deleteByMsg(int msgId) {
		attachmentDao.deletbyMsg(msgId);
	}

	public List<Attachment> listByMsg(int msgId) {
		return attachmentDao.list("select a from Attachment a join fetch a.message m where m.id=?",msgId);
	}

	public Attachment load(int id) {
		return attachmentDao.load(id);
	}
	
	private String generatorNewFileName(String oldName) {
		String newName = null;
		newName = String.valueOf(new Date().getTime())+
				  "."+FilenameUtils.getExtension(oldName);
		return newName;
	}
	@Override
	public void update(Attachment att) {
		attachmentDao.add(att);
	}
	@Override
	public void updateClearAttach() {
		String hql = "from Attachment where message is null";
		List<Attachment> atts = attachmentDao.list(hql);
		for(Attachment att:atts) {
			System.out.println(att.getId());
			boolean b = attachmentDao.delByObj(att);
			if(b) {
				System.out.println("-----清除了附件"+att.getId()+","+
						att.getNewName()+"["+att.getOldName()+"]"+"---------");
			}
		}
	}

}
