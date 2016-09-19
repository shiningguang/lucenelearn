package org.itat.message.iservice;

import java.io.File;
import java.util.List;

import org.itat.message.vo.Attachment;

public interface IAttachmentService {
	
	public Attachment add(Attachment attach,File attachFile);
	
	public void deleteById(int id);
	
	public void deleteByMsg(int msgId);
	
	public List<Attachment> listByMsg(int msgId);
	
	public Attachment load(int id);
	
	public void update(Attachment att);
	/**
	 * 清理没有指向任何留言的附件信息
	 */
	public void updateClearAttach();
}
