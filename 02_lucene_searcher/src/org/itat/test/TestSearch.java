package org.itat.test;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Query;
import org.apache.lucene.util.Version;
import org.itat.index.FileIndexUtils;
import org.itat.index.SearcherUtil;
import org.junit.Before;
import org.junit.Test;

public class TestSearch {
	private SearcherUtil su;
	@Before
	public void init() {
		su = new SearcherUtil();
	}
	
	@Test
	public void testCopyFiles() {
		try {
			File file = new File("d:/lucene/example/");
			for(File f:file.listFiles()) {
				String destFileName = FilenameUtils.getFullPath(f.getAbsolutePath())+
						FilenameUtils.getBaseName(f.getName())+".she";
				FileUtils.copyFile(f, new File(destFileName));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void searchByTerm() {
		su.searchByTerm("content","i",3);
	}
	
	@Test
	public void searchByTermRange() {
		//查询name以a开头和s结尾的
//		su.searchByTermRange("name","a","s",10);
		//由于attachs是数字类型，使用TermRange无法查询
		su.searchByTermRange("attach","2","10", 5);
	}
	
	@Test
	public void searchByNumRange() {
		su.searchByNumricRange("attach",2,10, 5);
	}
	
	@Test
	public void searchByPrefix() {
		su.searchByPrefix("content", "s", 10);
	}
	
	@Test
	public void searchByWildcard() {
		//匹配@itat.org结尾的所有字符
		su.searchByWildcard("email", "*@itat.org", 10);
		//匹配j开头的有三个字符的name
		su.searchByWildcard("name", "j???", 10);
	}
	
	@Test
	public void searchByBoolean() {
		su.searchByBoolean(10);
	}
	
	@Test
	public void searchByPhrase() {
		su.searchByPhrase(10);
	}
	
	@Test
	public void searchByFuzzy() {
		su.searchByFuzzy(10);
	}
	
	@Test
	public void searchByQueryParse() throws ParseException {
		//1、创建QueryParser对象,默认搜索域为content
		QueryParser parser = new QueryParser(Version.LUCENE_35, "content", new StandardAnalyzer(Version.LUCENE_35));
		//改变空格的默认操作符，以下可以改成AND
		//parser.setDefaultOperator(Operator.AND);
		//开启第一个字符的通配符匹配，默认关闭因为效率不高
		parser.setAllowLeadingWildcard(true);
		//搜索content中包含有like的
		Query query = parser.parse("like");
		
		//有basketball或者football的，空格默认就是OR
		query = parser.parse("basketball football");
		
		//改变搜索域为name为mike
		//query = parser.parse("content:like");
		
		//同样可以使用*和?来进行通配符匹配
//		query = parser.parse("name:j*");
		
		//通配符默认不能放在首位
//		query = parser.parse("email:*@itat.org");
		
		//匹配name中没有mike但是content中必须有football的，+和-要放置到域说明前面
		query = parser.parse("- name:mike + like");
		
		//匹配一个区间，注意:TO必须是大写
		//query = parser.parse("id:[1 TO 6]");
		
		//闭区间匹配只会匹配到2
		//query = parser.parse("id:{1 TO 3}");
		
		//完全匹配I Like Football的
		//query = parser.parse("\"I like football\"");
		
		//匹配I 和football之间有一个单词距离的
		//query = parser.parse("\"I football\"~1");
		
		//模糊查询
		//query = parser.parse("name:make~");
		
		//没有办法匹配数字范围（自己扩展Parser）
		//query = parser.parse("attach:[2 TO 10]");
		su.searchByQueryParse(query, 10);
	}
	
	@Test
	public void indexFile() {
		FileIndexUtils.index(true);
	}
	
	@Test
	public void testSearchPage01() {
		su.searchPage("java", 2,20);
		System.out.println("-------------------------------");
//		su.searchNoPage("java");
		su.searchPageByAfter("java", 2,20);
	}
	
	@Test
	public void testSearchPage02() {
		su.searchPageByAfter("java", 3,20);
	}
	
}
