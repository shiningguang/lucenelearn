package org.lucene.util;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.Formatter;
import org.apache.lucene.search.highlight.Fragmenter;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.search.highlight.SimpleSpanFragmenter;
import org.apache.lucene.util.Version;
import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;

import com.chenlb.mmseg4j.analysis.MMSegAnalyzer;

public class CopyOfSearcherUtil {

	public void searcher01() {
		try {
			IndexSearcher searcher = new IndexSearcher(IndexReader.open(FileIndexUtils.getDirectory()));
			TermQuery query = new TermQuery(new Term("content","��"));
			TopDocs tds = searcher.search(query,20);
			for(ScoreDoc sd:tds.scoreDocs) {
				Document doc = searcher.doc(sd.doc);
				System.out.println(doc.get("title"));
			}
			searcher.close();
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void searcherByHighlighter(String name) {
		try {
			Analyzer a = new MMSegAnalyzer();
			IndexSearcher searcher = new IndexSearcher(IndexReader.open(FileIndexUtils.getDirectory()));
			//QueryParser parser = new QueryParser(Version.LUCENE_35,"title",a);
			MultiFieldQueryParser parser = new MultiFieldQueryParser(Version.LUCENE_35, new String[]{"title","content"}, a);
			Query query = parser.parse(name);
			TopDocs tds = searcher.search(query, 20);
			for(ScoreDoc sd:tds.scoreDocs) {
				Document doc = searcher.doc(sd.doc);
				String title = doc.get("title");
				title = lighterStr(a, query, title, "title");
				System.out.println(title);
				System.out.println("**************************************************************************************************");
				String content = new Tika().parseToString(new File(doc.get("path")));
				content = lighterStr(a, query, content, "content");
				System.out.println(content);
				System.out.println("------------------------------------------------------------------------------------------------------------------");
			}
			searcher.close();
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (InvalidTokenOffsetsException e) {
			e.printStackTrace();
		} catch (TikaException e) {
			e.printStackTrace();
		}
	}
	
	private String lighterStr(Analyzer a,Query query,String txt,String fieldname) throws IOException, InvalidTokenOffsetsException {
		String str =  null;
		QueryScorer scorer = new QueryScorer(query);
		Fragmenter fragmenter = new SimpleSpanFragmenter(scorer);
		Formatter fmt = new SimpleHTMLFormatter("<b>", "</b>");
		Highlighter lighter = new Highlighter(fmt, scorer);
		lighter.setTextFragmenter(fragmenter);
		str = lighter.getBestFragment(a, fieldname,txt);
		if(str==null)return txt;
		return str;
	}
	
	public void lighter01() {
		try {
			String txt = "�Ұ������찲�ţ��찲���ϲ����,ΰ������ë��ϯ��ָ��������ǰ������ǰ���������������뿪��������˼���Ļ����Ǹ��ϵĽ�����ΰ��ĸ�ʥ��sadfsadnfl.sajdfl;aksjdf;lsadfsadfm.asd���ǿ϶���������̵Ļ������������Ʒ����Ǹ�ɵ��㻪�ɹ�˾�Ļ���ںؿ����ǶԻ������ǹ��찲�ż�  �����绰����ʼ���� ���������Ǵ����  ������������";
			Query query = new QueryParser(Version.LUCENE_35, "f", new MMSegAnalyzer()).parse("���� ΰ��");
			QueryScorer scorer = new QueryScorer(query);
			Fragmenter fragment = new SimpleSpanFragmenter(scorer);
			Formatter formatter = new SimpleHTMLFormatter("<span style='color:red'>", "</span>");
			Highlighter highlighter = new Highlighter(formatter,scorer);
			highlighter.setTextFragmenter(fragment);
			String str = highlighter.getBestFragment(new MMSegAnalyzer(), "f", txt);
			System.out.println(str);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidTokenOffsetsException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
}