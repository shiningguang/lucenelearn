package org.lucene.test;

import java.io.File;
import java.io.IOException;

import org.apache.tika.exception.TikaException;
import org.junit.Test;
import org.lucene.util.FileIndexUtils;
import org.lucene.util.IndexUtil;
import org.lucene.util.SearcherUtil;

public class TestIndex {

	@Test
	public void testIndex() {
		IndexUtil iu = new IndexUtil();
		iu.index();
	}
	
	@Test
	public void testTika01() {
		IndexUtil iu = new IndexUtil();
		System.out.println(iu.fileToTxt(new File("d:/lucene/example2/2009、10级无法注册学籍数据请示.doc")));
	}
	
	@Test
	public void testToka02() throws IOException, TikaException {
		IndexUtil iu = new IndexUtil();
		System.out.println(iu.tikaTool(new File("d:/lucene/example2/index.html")));
	}
	
	@Test
	public void testIndex02() {
		FileIndexUtils.index(true);
	}
	
	@Test
	public void testSearcher01() {
		SearcherUtil su = new SearcherUtil();
		su.searcher01();
	}
	
	@Test
	public void testLighter01() {
		SearcherUtil su = new SearcherUtil();
		su.lighter01();
	}
	
	@Test
	public void testLighter02() {
		SearcherUtil su = new SearcherUtil();
		su.searcherByHighlighter("c语言");
//		su.searcherByFastHighlighter("title:c语言");
	}
}
