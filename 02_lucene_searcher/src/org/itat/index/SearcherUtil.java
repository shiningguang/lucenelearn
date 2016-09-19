package org.itat.index;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.NumericField;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.FuzzyQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.PrefixQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TermRangeQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.WildcardQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.util.Version;

public class SearcherUtil {
	private Directory directory;
	private IndexReader reader;
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
	private Map<String,Float> scores = new HashMap<String,Float>();
	
	public SearcherUtil() {
//		directory = new RAMDirectory();
		try {
			directory = FSDirectory.open(new File("d:/lucenc/index03"));
			setDates();
			scores.put("itat.org",2.0f);
			scores.put("zttc.edu", 1.5f);
			index();
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
	
	public IndexSearcher getSearcher() {
		try {
			if(reader==null) {
				reader = IndexReader.open(directory);
			} else {
				IndexReader tr = IndexReader.openIfChanged(reader);
				if(tr!=null) {
					reader.close();
					reader = tr;
				}
			}
			return new IndexSearcher(reader);
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public IndexSearcher getSearcher(Directory directory) {
		try {
			if(reader==null) {
				reader = IndexReader.open(directory);
			} else {
				IndexReader tr = IndexReader.openIfChanged(reader);
				if(tr!=null) {
					reader.close();
					reader = tr;
				}
			}
			return new IndexSearcher(reader);
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void searchByTerm(String field,String name,int num) {
		try {
			IndexSearcher searcher = getSearcher();
			Query query = new TermQuery(new Term(field,name));
			TopDocs tds = searcher.search(query, num);
			System.out.println("һ����ѯ��:"+tds.totalHits);
			for(ScoreDoc sd:tds.scoreDocs) {
				Document doc = searcher.doc(sd.doc);
				System.out.println(doc.get("id")+"---->"+
						doc.get("name")+"["+doc.get("email")+"]-->"+doc.get("id")+","+
						doc.get("attach")+","+doc.get("date"));
			}
			searcher.close();
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void searchByTermRange(String field,String start,String end,int num) {
		try {
			IndexSearcher searcher = getSearcher();
			Query query = new TermRangeQuery(field,start,end,true, true);
			TopDocs tds = searcher.search(query, num);
			System.out.println("һ����ѯ��:"+tds.totalHits);
			for(ScoreDoc sd:tds.scoreDocs) {
				Document doc = searcher.doc(sd.doc);
				System.out.println(doc.get("id")+"---->"+
						doc.get("name")+"["+doc.get("email")+"]-->"+doc.get("id")+","+
						doc.get("attach")+","+doc.get("date"));
			}
			searcher.close();
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void searchByNumricRange(String field,int start,int end,int num) {
		try {
			IndexSearcher searcher = getSearcher();
			Query query = NumericRangeQuery.newIntRange(field,start, end,true,true);
			TopDocs tds = searcher.search(query, num);
			System.out.println("һ����ѯ��:"+tds.totalHits);
			for(ScoreDoc sd:tds.scoreDocs) {
				Document doc = searcher.doc(sd.doc);
				System.out.println(doc.get("id")+"---->"+
						doc.get("name")+"["+doc.get("email")+"]-->"+doc.get("id")+","+
						doc.get("attach")+","+doc.get("date"));
			}
			searcher.close();
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void searchByPrefix(String field,String value,int num) {
		try {
			IndexSearcher searcher = getSearcher();
			Query query = new PrefixQuery(new Term(field,value));
			TopDocs tds = searcher.search(query, num);
			System.out.println("һ����ѯ��:"+tds.totalHits);
			for(ScoreDoc sd:tds.scoreDocs) {
				Document doc = searcher.doc(sd.doc);
				System.out.println(doc.get("id")+"---->"+
						doc.get("name")+"["+doc.get("email")+"]-->"+doc.get("id")+","+
						doc.get("attach")+","+doc.get("date"));
			}
			searcher.close();
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void searchByWildcard(String field,String value,int num) {
		try {
			IndexSearcher searcher = getSearcher();
			//�ڴ����value�п���ʹ��ͨ���:?��*,?��ʾƥ��һ���ַ���*��ʾƥ���������ַ�
			Query query = new WildcardQuery(new Term(field,value));
			TopDocs tds = searcher.search(query, num);
			System.out.println("һ����ѯ��:"+tds.totalHits);
			for(ScoreDoc sd:tds.scoreDocs) {
				Document doc = searcher.doc(sd.doc);
				System.out.println(doc.get("id")+"---->"+
						doc.get("name")+"["+doc.get("email")+"]-->"+doc.get("id")+","+
						doc.get("attach")+","+doc.get("date"));
			}
			searcher.close();
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void searchByBoolean(int num) {
		try {
			IndexSearcher searcher = getSearcher();
			BooleanQuery query = new BooleanQuery();
			/*
			 * BooleanQuery�������Ӷ���Ӳ�ѯ
			 * Occur.MUST��ʾ�������
			 * Occur.SHOULD��ʾ���Գ���
			 * Occur.MUSE_NOT��ʾ���ܳ���
			 */
			query.add(new TermQuery(new Term("name","zhangsan")), Occur.MUST_NOT);
			query.add(new TermQuery(new Term("content","game")),Occur.SHOULD);
			TopDocs tds = searcher.search(query, num);
			System.out.println("һ����ѯ��:"+tds.totalHits);
			for(ScoreDoc sd:tds.scoreDocs) {
				Document doc = searcher.doc(sd.doc);
				System.out.println(doc.get("id")+"---->"+
						doc.get("name")+"["+doc.get("email")+"]-->"+doc.get("id")+","+
						doc.get("attach")+","+doc.get("date"));
			}
			searcher.close();
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void searchByPhrase(int num) {
		try {
			IndexSearcher searcher = getSearcher();
			PhraseQuery query = new PhraseQuery();
			query.setSlop(3);
			query.add(new Term("content","pingpeng"));
			//��һ��Term
			query.add(new Term("content","i"));
			//��������֮��ĵڶ���Term
//			query.add(new Term("content","football"));
			TopDocs tds = searcher.search(query, num);
			System.out.println("һ����ѯ��:"+tds.totalHits);
			for(ScoreDoc sd:tds.scoreDocs) {
				Document doc = searcher.doc(sd.doc);
				System.out.println(doc.get("id")+"---->"+
						doc.get("name")+"["+doc.get("email")+"]-->"+doc.get("id")+","+
						doc.get("attach")+","+doc.get("date"));
			}
			searcher.close();
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void searchByFuzzy(int num) {
		try {
			IndexSearcher searcher = getSearcher();
			FuzzyQuery query = new FuzzyQuery(new Term("name","mase"),0.4f,0);
			System.out.println(query.getPrefixLength());
			System.out.println(query.getMinSimilarity());
			TopDocs tds = searcher.search(query, num);
			System.out.println("һ����ѯ��:"+tds.totalHits);
			for(ScoreDoc sd:tds.scoreDocs) {
				Document doc = searcher.doc(sd.doc);
				System.out.println(doc.get("id")+"---->"+
						doc.get("name")+"["+doc.get("email")+"]-->"+doc.get("id")+","+
						doc.get("attach")+","+doc.get("date"));
			}
			searcher.close();
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void searchByQueryParse(Query query,int num) {
		try {
			IndexSearcher searcher = getSearcher();
			TopDocs tds = searcher.search(query, num);
			System.out.println("һ����ѯ��:"+tds.totalHits);
			for(ScoreDoc sd:tds.scoreDocs) {
				Document doc = searcher.doc(sd.doc);
				System.out.println(doc.get("id")+"---->"+
						doc.get("name")+"["+doc.get("email")+"]-->"+doc.get("id")+","+
						doc.get("attach")+","+doc.get("date")+"=="+sd.score);
			}
			searcher.close();
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void searchPage(String query,int pageIndex,int pageSize) {
		try {
			Directory dir = FileIndexUtils.getDirectory();
			IndexSearcher searcher = getSearcher(dir);
			QueryParser parser = new QueryParser(Version.LUCENE_35,"content",new StandardAnalyzer(Version.LUCENE_35));
			Query q = parser.parse(query);
			TopDocs tds = searcher.search(q, 500);
			ScoreDoc[] sds = tds.scoreDocs;
			int start = (pageIndex-1)*pageSize;
			int end = pageIndex*pageSize;
			for(int i=start;i<end;i++) {
				Document doc = searcher.doc(sds[i].doc);
				System.out.println(sds[i].doc+":"+doc.get("path")+"-->"+doc.get("filename"));
			}
			
			searcher.close();
		} catch (org.apache.lucene.queryParser.ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * ����ҳ��ͷ�ҳ��С��ȡ��һ�ε����һ��ScoreDoc
	 */
	private ScoreDoc getLastScoreDoc(int pageIndex,int pageSize,Query query,IndexSearcher searcher) throws IOException {
		if(pageIndex==1)return null;//����ǵ�һҳ�ͷ��ؿ�
		int num = pageSize*(pageIndex-1);//��ȡ��һҳ������
		TopDocs tds = searcher.search(query, num);
		return tds.scoreDocs[num-1];
	}
	
	public void searchPageByAfter(String query,int pageIndex,int pageSize) {
		try {
			Directory dir = FileIndexUtils.getDirectory();
			IndexSearcher searcher = getSearcher(dir);
			QueryParser parser = new QueryParser(Version.LUCENE_35,"content",new StandardAnalyzer(Version.LUCENE_35));
			Query q = parser.parse(query);
			//�Ȼ�ȡ��һҳ�����һ��Ԫ��
			ScoreDoc lastSd = getLastScoreDoc(pageIndex, pageSize, q, searcher);
			//ͨ�����һ��Ԫ��������ҳ��pageSize��Ԫ��
			TopDocs tds = searcher.searchAfter(lastSd,q, pageSize);
			for(ScoreDoc sd:tds.scoreDocs) {
				Document doc = searcher.doc(sd.doc);
				System.out.println(sd.doc+":"+doc.get("path")+"-->"+doc.get("filename"));
			}
			searcher.close();
		} catch (org.apache.lucene.queryParser.ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void searchNoPage(String query) {
		try {
			Directory dir = FileIndexUtils.getDirectory();
			IndexSearcher searcher = getSearcher(dir);
			QueryParser parser = new QueryParser(Version.LUCENE_35,"content",new StandardAnalyzer(Version.LUCENE_35));
			Query q = parser.parse(query);
			TopDocs tds = searcher.search(q, 20);
			ScoreDoc[] sds = tds.scoreDocs;
			for(int i=0;i<sds.length;i++) {
				Document doc = searcher.doc(sds[i].doc);
				System.out.println(sds[i].doc+":"+doc.get("path")+"-->"+doc.get("filename"));
			}
			
			searcher.close();
		} catch (org.apache.lucene.queryParser.ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
