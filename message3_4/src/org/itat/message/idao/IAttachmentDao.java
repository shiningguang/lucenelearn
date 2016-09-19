package org.itat.message.idao;

import org.itat.message.vo.Attachment;


public interface IAttachmentDao extends IBaseDao<Attachment>{
	public void deletbyMsg(int msgId);
	
	public void deletebyId(int id);
	
	public boolean delByObj(Attachment att);
	
}
