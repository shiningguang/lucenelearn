package org.itat.lucene.util;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.util.Version;

public class CustomParserUtil {

	public void searcherByQuery(String value) {
		try {
			IndexSearcher searcher = new IndexSearcher(IndexReader.open(FileIndexUtils.getDirectory()));
			TopDocs tds = null;
			CustomParser parser = new CustomParser(Version.LUCENE_35, "content", new StandardAnalyzer(Version.LUCENE_35));
			Query queryStr = parser.parse(value);
			System.out.println(queryStr);
			tds = searcher.search(queryStr, 50);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			for(ScoreDoc sd:tds.scoreDocs) {
				Document d = searcher.doc(sd.doc);
				System.out.println(sd.doc+":("+sd.score+")" +
						"["+d.get("filename")+"¡¾"+d.get("path")+"¡¿--->"+
						d.get("size")+"-----"+sdf.format(new Date(Long.valueOf(d.get("date"))))+"]");
			}
			searcher.close();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			System.err.println(e.getMessage());
		}
	}
}
