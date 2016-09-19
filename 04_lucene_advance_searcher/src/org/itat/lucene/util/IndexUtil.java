package org.itat.lucene.util;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.NumericField;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.StaleReaderException;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.NRTManager;
import org.apache.lucene.search.NRTManagerReopenThread;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.SearcherManager;
import org.apache.lucene.search.SearcherWarmer;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.util.Version;


public class IndexUtil {
	private String[] ids = {"1","2","3","4","5","6"};
	private String[] emails = {"aa@itat.org","bb@itat.org","cc@cc.org","dd@sina.org","ee@zttc.edu","ff@itat.org"};
	private String[] contents = {
			"welcome to visited the space,I like book",
			"hello boy, I like pingpeng ball",
			"my name is cc I like game",
			"I like football",
			"I like football and I like basketball too",
			"I like movie and swim"
	};
	private Date[] dates = null;
	private int[] attachs = {2,3,1,4,5,5};
	private String[] names = {"zhangsan","lisi","john","jetty","mike","jake"};
	private Directory directory = null;
	private Map<String,Float> scores = new HashMap<String,Float>();
	private SearcherManager mgr = null;
	private NRTManager nrtMgr = null;
	private IndexWriter writer = null;
	
	public IndexUtil() {
		try {
			setDates();
			scores.put("itat.org",2.0f);
			scores.put("zttc.edu", 1.5f);
			directory = FSDirectory.open(new File("d:/lucene/index02"));
			writer = new IndexWriter(directory,new IndexWriterConfig(Version.LUCENE_35,new StandardAnalyzer(Version.LUCENE_35)));
			nrtMgr = new NRTManager(writer,new SearcherWarmer() {
				@Override
				public void warm(IndexSearcher s) throws IOException {
					System.out.println("reopen");
				}
			});
			//����NRTManager��Reopen�߳�
			NRTManagerReopenThread reopen = new NRTManagerReopenThread(nrtMgr, 5.0,0.025);
			reopen.setDaemon(true);
			reopen.setName("NrtManager Reopen Thread");
			reopen.start();
			mgr = nrtMgr.getSearcherManager(true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void setDates() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			dates = new Date[ids.length];
			dates[0] = sdf.parse("2010-02-19");
			dates[1] = sdf.parse("2012-01-11");
			dates[2] = sdf.parse("2011-09-19");
			dates[3] = sdf.parse("2010-12-22");
			dates[4] = sdf.parse("2012-01-01");
			dates[5] = sdf.parse("2011-05-19");
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	public void undelete() {
		//ʹ��IndexReader���лָ�
		try {
			IndexReader reader = IndexReader.open(directory,false);
			//�ָ�ʱ�������IndexReader��ֻ��(readOnly)����Ϊfalse
			reader.undeleteAll();
			reader.close();
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (StaleReaderException e) {
			e.printStackTrace();
		} catch (LockObtainFailedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void merge() {
		IndexWriter writer = null;
		try {
			writer = new IndexWriter(directory,
					new IndexWriterConfig(Version.LUCENE_35,new StandardAnalyzer(Version.LUCENE_35)));
			//�Ὣ�����ϲ�Ϊ2�Σ��������еı�ɾ�������ݻᱻ���
			//�ر�ע�⣺�˴�Lucene��3.5֮�󲻽���ʹ�ã���Ϊ�����Ĵ����Ŀ�����
			//Lucene���������Զ������
			writer.forceMerge(2);
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (LockObtainFailedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(writer!=null) writer.close();
			} catch (CorruptIndexException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void forceDelete() {
		IndexWriter writer = null;
		
		try {
			writer = new IndexWriter(directory,
					new IndexWriterConfig(Version.LUCENE_35,new StandardAnalyzer(Version.LUCENE_35)));
			writer.forceMergeDeletes();
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (LockObtainFailedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(writer!=null) writer.close();
			} catch (CorruptIndexException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void delete() {
		try {
			nrtMgr.deleteDocuments(new Term("id","2"));
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (LockObtainFailedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void commit() {
		try {
			writer.commit();
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public void update() {
		try {
			/*
			 * Lucene��û���ṩ���£�����ĸ��²�����ʵ���������������ĺϼ�
			 * ��ɾ��֮�������
			 */
			Document doc = new Document();
			doc.add(new Field("id","11",Field.Store.YES,Field.Index.NOT_ANALYZED_NO_NORMS));
			doc.add(new Field("email",emails[0],Field.Store.YES,Field.Index.NOT_ANALYZED));
			doc.add(new Field("content",contents[0],Field.Store.NO,Field.Index.ANALYZED));
			doc.add(new Field("name",names[0],Field.Store.YES,Field.Index.NOT_ANALYZED_NO_NORMS));
			nrtMgr.updateDocument(new Term("id","1"), doc);
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (LockObtainFailedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void query() {
		try {
			IndexReader reader = IndexReader.open(directory);
			//ͨ��reader������Ч�Ļ�ȡ���ĵ�������
			System.out.println("numDocs:"+reader.numDocs());
			System.out.println("maxDocs:"+reader.maxDoc());
			System.out.println("deleteDocs:"+reader.numDeletedDocs());
			reader.close();
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void index() {
		IndexWriter writer = null;
		try {
			writer = new IndexWriter(directory, new IndexWriterConfig(Version.LUCENE_35, new StandardAnalyzer(Version.LUCENE_35)));
			writer.deleteAll();
			Document doc = null;
			for(int i=0;i<ids.length;i++) {
				doc = new Document();
				doc.add(new Field("id",ids[i],Field.Store.YES,Field.Index.NOT_ANALYZED_NO_NORMS));
				doc.add(new Field("email",emails[i],Field.Store.YES,Field.Index.NOT_ANALYZED));
				doc.add(new Field("content",contents[i],Field.Store.NO,Field.Index.ANALYZED));
				doc.add(new Field("name",names[i],Field.Store.YES,Field.Index.NOT_ANALYZED_NO_NORMS));
				//�洢����
				doc.add(new NumericField("attach",Field.Store.YES,true).setIntValue(attachs[i]));
				//�洢����
				doc.add(new NumericField("date",Field.Store.YES,true).setLongValue(dates[i].getTime()));
				String et = emails[i].substring(emails[i].lastIndexOf("@")+1);
				System.out.println(et);
				if(scores.containsKey(et)) {
					doc.setBoost(scores.get(et));
				} else {
					doc.setBoost(0.5f);
				}
				writer.addDocument(doc);
			}
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (LockObtainFailedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(writer!=null)writer.close();
			} catch (CorruptIndexException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void search01() {
		try {
			IndexReader reader = IndexReader.open(directory);
			IndexSearcher searcher = new IndexSearcher(reader);
			TermQuery query = new TermQuery(new Term("content","like"));
			TopDocs tds = searcher.search(query, 10);
			for(ScoreDoc sd:tds.scoreDocs) {
				Document doc = searcher.doc(sd.doc);
				System.out.println("("+sd.doc+"-"+doc.getBoost()+"-"+sd.score+")"+
						doc.get("name")+"["+doc.get("email")+"]-->"+doc.get("id")+","+
						doc.get("attach")+","+doc.get("date"));
			}
			reader.close();
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void search02() {
		IndexSearcher searcher = mgr.acquire();
		try {
//			mgr.maybeReopen();//�ж��Ƿ���Ҫ���´�һ��searcher
			TermQuery query = new TermQuery(new Term("content","like"));
			TopDocs tds = searcher.search(query, 10);
			for(ScoreDoc sd:tds.scoreDocs) {
				Document doc = searcher.doc(sd.doc);
				System.out.println(doc.get("id")+"---->"+
						doc.get("name")+"["+doc.get("email")+"]-->"+doc.get("id")+","+
						doc.get("attach")+","+doc.get("date"));
			}
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				mgr.release(searcher);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
