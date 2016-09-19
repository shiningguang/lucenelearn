package org.itat.message.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.itat.message.idao.IAttachmentDao;
import org.itat.message.idao.IMessageDao;
import org.itat.message.idao.ITempIndexDao;
import org.itat.message.index.SolrContext;
import org.itat.message.iservice.IIndexService;
import org.itat.message.util.IndexUtil;
import org.itat.message.util.PageObject;
import org.itat.message.util.SystemContext;
import org.itat.message.vo.Attachment;
import org.itat.message.vo.Index;
import org.itat.message.vo.IndexField;
import org.itat.message.vo.Message;
import org.itat.message.vo.TempIndex;
import org.springframework.stereotype.Service;

@Service("indexService")
public class IndexService implements IIndexService {
	private ITempIndexDao tempIndexDao;
	private IMessageDao messageDao;
	private IAttachmentDao attachmentDao;
	
	
	public IMessageDao getMessageDao() {
		return messageDao;
	}
	@Resource
	public void setMessageDao(IMessageDao messageDao) {
		this.messageDao = messageDao;
	}
	public IAttachmentDao getAttachmentDao() {
		return attachmentDao;
	}
	@Resource
	public void setAttachmentDao(IAttachmentDao attachmentDao) {
		this.attachmentDao = attachmentDao;
	}
	public ITempIndexDao getTempIndexDao() {
		return tempIndexDao;
	}
	@Resource
	public void setTempIndexDao(ITempIndexDao tempIndexDao) {
		this.tempIndexDao = tempIndexDao;
	}

	@Override
	public void addIndex(IndexField fields,boolean inDatabase) {
		try {
//			if(inDatabase) {
//				TempIndex ti = new TempIndex();
//				ti.setAdd();
//				ti.setObjId(fields.getObjId());
//				ti.setType(fields.getType());
//				tempIndexDao.add(ti);
//			}
			SolrContext.getServer().addBean(fields);
			SolrContext.getServer().commit();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SolrServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void deleteIndex(String id,String type) {
		try {
//			TempIndex ti = new TempIndex();
//			ti.setDelete();
//			///xx_xx
//			ti.setId(Integer.parseInt(id));
//			ti.setType(type);
//			tempIndexDao.add(ti);
			SolrContext.getServer().deleteById(id);
			SolrContext.getServer().commit();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SolrServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void updateIndex(IndexField fields) {
		try {
//			TempIndex ti = new TempIndex();
//			ti.setDelete();
//			///xx_xx
//			ti.setId(fields.getObjId());
//			ti.setType(fields.getType());
//			tempIndexDao.add(ti);
			SolrContext.getServer().deleteById(fields.getId());
			SolrContext.getServer().addBean(fields);
			SolrContext.getServer().commit();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SolrServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	@Override
	public PageObject<Index> findByIndex(String condition) {
		if(condition==null) condition = "";
		PageObject<Index> pages = new PageObject<Index>();
		List<Index> datas = new ArrayList<Index>();
		try {
			int pageSize = SystemContext.getPageSize();
			int pageOffset = SystemContext.getPageOffset();
			SolrQuery query = new SolrQuery(condition);
			query.setHighlight(true)
				.setHighlightSimplePre("<span class='lighter'>")
				.setHighlightSimplePost("</span>")
				.setParam("hl.fl", "msg_title,msg_content")
				.setStart(pageOffset).setRows(pageSize);
			QueryResponse resp = SolrContext.getServer().query(query);
			SolrDocumentList sdl = resp.getResults();
			for(SolrDocument sd:sdl) {
				String id = (String)sd.getFieldValue("id");
				int msgId = (Integer)sd.getFieldValue("msg_id");
				String title = (String)sd.getFieldValue("msg_title");
				Date date = (Date)sd.getFieldValue("msg_date");
				List<String> contents = (List)sd.getFieldValues("msg_content");
				StringBuffer sb = new StringBuffer();
				for(String con:contents) {
					sb.append(con);
				}
				System.out.println(sb.toString());
				String sc = sb.toString();
				Index index = new Index();
				index.setCreateDate(date);
				index.setTitle(title);
				
				if(sc.length()>=150) {
					sc = sc.substring(0,150);
				}
				index.setSummary(sc);
				index.setMsgId(msgId);
				if(resp.getHighlighting().get(id)!=null) {
					List<String> htitle = resp.getHighlighting().get(id).get("msg_title");
					if(htitle!=null) index.setTitle(htitle.get(0));
					List<String> hcontent = resp.getHighlighting().get(id).get("msg_content");
					if(hcontent!=null)index.setSummary(hcontent.get(0));
				}
				datas.add(index);
			}
			
			pages.setDatas(datas);
			pages.setOffset(pageOffset);
			pages.setPageSize(pageSize);
			pages.setTotalRecord(new Long(sdl.getNumFound()).intValue());
		} catch (SolrServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return pages;
	}
	

	@Override
	public void updateCommitIndex() {
		tempIndexDao.delAll();
	}

	@Override
	public void updateReconstructorIndex() {
		/**
		 * 将数据库中的所有对象取出，创建相应的IndexField完成索引的重构
		 */
		try {
			SolrContext.getServer().deleteByQuery("*:*");
			List<Message> messages = messageDao.list("from Message");
			indexMessages(messages);
			SolrContext.getServer().commit();
			tempIndexDao.delAll();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SolrServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void indexMessages(List<Message> messages) {
		for(Message msg:messages) {
			indexMessage(msg);
		}
	}
	
	private void indexMessage(Message msg) {
		List<String> contents = new ArrayList<String>();
		List<String> ans = new ArrayList<String>();
		contents.add(msg.getContent());
		List<Attachment> atts = attachmentDao.listByMessage(msg.getId());
		for(Attachment att:atts) {
			IndexUtil.attach2Index(contents, ans,att);
		}
		IndexField field = IndexUtil.msg2IndexField(msg);
		field.setContent(contents);
		field.setAtths(ans);
		addIndex(field,false);
	}
	@Override
	public void updateSetIndex() {
		try {
			List<TempIndex> tis = tempIndexDao.list("from TempIndex");
			for(TempIndex ti:tis) {
				if(ti.getType().equals(IndexUtil.MSG_TYPE)) {
					Message msg = messageDao.load(ti.getObjId());
					indexMessage(msg);
				}
			}
			SolrContext.getServer().commit();
			tempIndexDao.delAll();
		} catch (SolrServerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
