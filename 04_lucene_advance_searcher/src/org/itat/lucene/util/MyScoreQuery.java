package org.itat.lucene.util;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.FieldCache;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.function.CustomScoreProvider;
import org.apache.lucene.search.function.CustomScoreQuery;
import org.apache.lucene.search.function.FieldScoreQuery;
import org.apache.lucene.search.function.FieldScoreQuery.Type;
import org.apache.lucene.search.function.ValueSourceQuery;

public class MyScoreQuery {
	
	public void searchByScoreQuery() {
		try {
			IndexSearcher searcher = new IndexSearcher(IndexReader.open(FileIndexUtils.getDirectory()));
			Query q = new TermQuery(new Term("content","java"));
			//1、创建一个评分域
			FieldScoreQuery fd = new FieldScoreQuery("score",Type.INT);
			//2、根据评分域和原有的Query创建自定义的Query对象
			MyCustomScoreQuery query = new MyCustomScoreQuery(q,fd);
			TopDocs tds = null;
			tds = searcher.search(query, 100);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			for(ScoreDoc sd:tds.scoreDocs) {
				Document d = searcher.doc(sd.doc);
				System.out.println(sd.doc+":("+sd.score+")" +
						"["+d.get("filename")+"【"+d.get("path")+"】--->"+
						d.get("size")+"-----"+sdf.format(new Date(Long.valueOf(d.get("date"))))+"]");
			}
			searcher.close();
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void searchByFileScoreQuery() {
		try {
			IndexSearcher searcher = new IndexSearcher(IndexReader.open(FileIndexUtils.getDirectory()));
			Query q = new TermQuery(new Term("content","java"));
			//1、创建一个评分域
			FilenameScoreQuery query = new FilenameScoreQuery(q);
			TopDocs tds = null;
			tds = searcher.search(query, 100);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			for(ScoreDoc sd:tds.scoreDocs) {
				Document d = searcher.doc(sd.doc);
				System.out.println(sd.doc+":("+sd.score+")" +
						"["+d.get("filename")+"【"+d.get("path")+"】--->"+
						d.get("size")+"-----"+sdf.format(new Date(Long.valueOf(d.get("date"))))+"]");
			}
			searcher.close();
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("serial")
	private class FilenameScoreQuery extends CustomScoreQuery {

		public FilenameScoreQuery(Query subQuery) {
			super(subQuery);
		}
		
		@Override
		protected CustomScoreProvider getCustomScoreProvider(IndexReader reader)
				throws IOException {
			return new FilenameScoreProvider(reader);
		}
		
	}
	
	private class DateScoreProvider extends CustomScoreProvider {
		long[] dates = null;
		public DateScoreProvider(IndexReader reader) {
			super(reader);
			try {
				dates = FieldCache.DEFAULT.getLongs(reader, "date");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		@Override
		public float customScore(int doc, float subQueryScore, float valSrcScore)
				throws IOException {
			long date = dates[doc];
			long today = new Date().getTime();
			long year = 1000*60*60*24*365;
			if(today-date<=year) {
				//为其加分
			}
			return super.customScore(doc, subQueryScore, valSrcScore);
		}
		
	}
	
	private class FilenameScoreProvider extends CustomScoreProvider {
		String[] filenames = null;
		public FilenameScoreProvider(IndexReader reader) {
			super(reader);
			try {
				filenames = FieldCache.DEFAULT.getStrings(reader, "filename");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		@Override
		public float customScore(int doc, float subQueryScore, float valSrcScore)
				throws IOException {
			//如何根据doc获取相应的field的值
			/*
			 * 在reader没有关闭之前，所有的数据会存储要一个域缓存中，可以通过域缓存获取很多有用的信息
			 * filenames = FieldCache.DEFAULT.getStrings(reader, "filename");可以获取所有的filename域的信息
			 */
			String filename = filenames[doc];
			if(filename.endsWith(".txt")||filename.endsWith(".ini")) {
				return subQueryScore*1.5f;
			}
			return subQueryScore/1.5f;
		}
	}
	
	
	@SuppressWarnings("serial")
	private class MyCustomScoreQuery extends CustomScoreQuery {
		
		public MyCustomScoreQuery(Query subQuery, ValueSourceQuery valSrcQuery) {
			super(subQuery, valSrcQuery);
		}
		
		@Override
		protected CustomScoreProvider getCustomScoreProvider(IndexReader reader)
				throws IOException {
			//默认情况实现的评分是通过原有的评分*传入进来的评分域所获取的评分来确定最终打分的
			//为了根据不同的需求进行评分，需要自己进行评分的设定
			/**
			 * 自定评分的步骤
			 * 创建一个类继承于CustomScoreProvider
			 * 覆盖customScore方法
			 */
			return new MyCustomScoreProvider(reader);
		}
		
	}
	
	private class MyCustomScoreProvider extends CustomScoreProvider {

		public MyCustomScoreProvider(IndexReader reader) {
			super(reader);
		}
		
		/**
		 * subQueryScore表示默认文档的打分
		 * valSrcScore表示的评分域的打分
		 */
		@Override
		public float customScore(int doc, float subQueryScore, float valSrcScore)
				throws IOException {
			return subQueryScore/valSrcScore;
		}
		
	}
}
