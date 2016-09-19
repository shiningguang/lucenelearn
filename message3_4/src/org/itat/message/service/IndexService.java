package org.itat.message.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.NumericField;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.NRTManager;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.tika.exception.TikaException;
import org.itat.message.idao.IAttachmentDao;
import org.itat.message.idao.IMessageDao;
import org.itat.message.idao.ITempIndexDao;
import org.itat.message.index.LuceneContext;
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
			if(inDatabase) {
				TempIndex ti = new TempIndex();
				ti.setAdd();
				ti.setObjId(fields.getObjId());
				ti.setType(fields.getType());
				tempIndexDao.add(ti);
			}
			NRTManager nrtMgr = LuceneContext.getInstance().getNRTManager();
			Document doc = field2Doc(fields);
			nrtMgr.addDocument(doc);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void deleteIndex(String id,String type) {
		try {
			TempIndex ti = new TempIndex();
			ti.setDelete();
			///xx_xx
			ti.setId(Integer.parseInt(id.split("_")[1]));
			ti.setType(type);
			tempIndexDao.add(ti);
			LuceneContext.getInstance().getNRTManager().deleteDocuments(new Term("id",id));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void updateIndex(IndexField fields) {
		try {
			TempIndex ti = new TempIndex();
			ti.setDelete();
			///xx_xx
			ti.setId(fields.getObjId());
			ti.setType(fields.getType());
			tempIndexDao.add(ti);
			NRTManager nrtMgr = LuceneContext.getInstance().getNRTManager();
			Document doc = field2Doc(fields);
			nrtMgr.updateDocument(new Term("id",fields.getId()), doc);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public PageObject<Index> findByIndex(String condition) {
		IndexSearcher searcher = LuceneContext.getInstance().getSearcher();
		PageObject<Index> pages = new PageObject<Index>();
		List<Index> datas = new ArrayList<Index>();
		try {
			int pageSize = SystemContext.getPageSize();
			int pageOffset = SystemContext.getPageOffset();
			MultiFieldQueryParser parser = new MultiFieldQueryParser(LuceneContext.getInstance().getVersion(),
						new String[]{"title","content"}, LuceneContext.getInstance().getAnalyzer());
			Query query = parser.parse(condition);
			TopDocs tds = searcher.searchAfter(getLastDoc(pageOffset,searcher,query),
						  query, pageSize);
			List<Integer> addMsgId = new ArrayList<Integer>();
			int totalRecord = tds.totalHits;
			for(ScoreDoc sd:tds.scoreDocs) {
				Document doc = searcher.doc(sd.doc);
				Index index = new Index();
				index.setCreateDate(new Date(Long.parseLong(doc.get("createDate"))));
				index.setTitle(doc.get("title"));
				index.setSummary(doc.get("title"));
				int parentId = Integer.parseInt(doc.get("parentId"));
				int msgId = 0;
				if(parentId==0) {
					msgId = Integer.parseInt(doc.get("objId"));
				} else {
					msgId = parentId;
				}
				index.setMsgId(msgId);
				if(!addMsgId.contains(msgId)) {
					datas.add(index);
					addMsgId.add(msgId);
				} else {
					totalRecord--;
				}
			}
			pages.setDatas(datas);
			pages.setOffset(pageOffset);
			pages.setPageSize(pageSize);
			pages.setTotalRecord(totalRecord);
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			LuceneContext.getInstance().releaseSearcher(searcher);
		}
		return pages;
	}
	
	private ScoreDoc getLastDoc(int pageOffset,IndexSearcher searcher,Query query) {
		if(pageOffset<=0) return null;
		try {
			TopDocs tds = searcher.search(query,pageOffset-1);
			return tds.scoreDocs[pageOffset-1];
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void updateCommitIndex() {
		tempIndexDao.delAll();
		LuceneContext.getInstance().commitIndex();
	}

	@Override
	public void updateReconstructorIndex() {
		/**
		 * 将数据库中的所有对象取出，创建相应的IndexField完成索引的重构
		 */
		try {
			LuceneContext.getInstance().getNRTManager().deleteAll();
			List<Message> messages = messageDao.list("from Message");
			indexMessages(messages);
			List<Attachment> atths = attachmentDao.list("from Attachment");
			indexAttachments(atths);
			LuceneContext.getInstance().commitIndex();
			tempIndexDao.delAll();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void indexAttachments(List<Attachment> atths) {
		try {
			for(Attachment att:atths) {
				IndexField field = IndexUtil.attach2Field(att);
				if(field!=null) {
					addIndex(field,false);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TikaException e) {
			e.printStackTrace();
		}
	}
	private void indexMessages(List<Message> messages) {
		for(Message msg:messages) {
			IndexField field = IndexUtil.msg2IndexField(msg);
			addIndex(field,false);
		}
	}
	private Document field2Doc(IndexField field) {
		Document doc = new Document();
		doc.add(new Field("id",field.getId(),Field.Store.YES,Field.Index.NOT_ANALYZED_NO_NORMS));
		doc.add(new Field("title",field.getTitle(),Field.Store.YES,Field.Index.ANALYZED));
		doc.add(new Field("content",field.getContent(),Field.Store.NO,Field.Index.ANALYZED));
		doc.add(new NumericField("objId",Field.Store.YES,true).setIntValue(field.getObjId()));
		doc.add(new NumericField("parentId",Field.Store.YES,true).setIntValue(field.getParentId()));
		doc.add(new NumericField("createDate",Field.Store.YES,true).setLongValue(field.getCreateDate().getTime()));
		return doc;
	}
	@Override
	public void updateSetIndex() {
		List<TempIndex> tis = tempIndexDao.list("from TempIndex");
		try {
			for(TempIndex ti:tis) {
				IndexField field = null;
				if(ti.getType().equals(IndexUtil.ATTACHMENT_TYPE)) {
					Attachment attach = attachmentDao.load(ti.getObjId());
					field = IndexUtil.attach2Field(attach);
				} else if(ti.getType().equals(IndexUtil.MSG_TYPE)) {
					Message msg = messageDao.load(ti.getObjId());
					field = IndexUtil.msg2IndexField(msg);
				}
				if(field!=null) this.addIndex(field, false);
			}
			LuceneContext.getInstance().commitIndex();
			tempIndexDao.delAll();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TikaException e) {
			e.printStackTrace();
		}
	}

}
