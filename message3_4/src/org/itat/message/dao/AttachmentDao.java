package org.itat.message.dao;


import java.io.File;
import java.util.List;

import javax.annotation.Resource;

import org.itat.message.idao.IAttachmentDao;
import org.itat.message.iservice.IIndexService;
import org.itat.message.util.IndexUtil;
import org.itat.message.util.SystemContext;
import org.itat.message.vo.Attachment;
import org.springframework.stereotype.Repository;

@Repository("attachmentDao")
public class AttachmentDao extends BaseDao<Attachment> implements
		IAttachmentDao{
	
	private IIndexService indexService;
	
	public IIndexService getIndexService() {
		return indexService;
	}
	@Resource
	public void setIndexService(IIndexService indexService) {
		this.indexService = indexService;
	}

	public void deletbyMsg(int msgId) {
		deleteFile(msgId);
		super.updateByHql("delete a from Attachment a where a.message=?",msgId);
	}
	
	/**
	 * 删除附件的文件
	 */
	@SuppressWarnings("unchecked")
	private void deleteFile(int msgId) {
		List<Object[]> files = this.getSession()
			.createQuery("select id,newName,message.id from Attachment where message.id=?")
			.setParameter(0, msgId).list();
		String realPath = SystemContext.getRealPath()+"/upload/";
		for(Object[] fn:files) {
			File f = new File(realPath+fn[1]);
			String id = fn[2]+"_"+fn[0];
			indexService.deleteIndex(id,IndexUtil.ATTACHMENT_TYPE);
			f.delete();
		}
								
	}

	@Override
	public void deletebyId(int id) {
		Attachment attachment = load(id);
		delete(attachment);
	}

	@Override
	public boolean delByObj(Attachment t) {
		String realPath = SystemContext.getRealPath()+"/upload/";
		File f = new File(realPath+t.getNewName());
		boolean b = f.delete();
		if(b) delete(t);
		return b;
	}
}
