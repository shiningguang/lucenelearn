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
		//��ѯname��a��ͷ��s��β��
//		su.searchByTermRange("name","a","s",10);
		//����attachs���������ͣ�ʹ��TermRange�޷���ѯ
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
		//ƥ��@itat.org��β�������ַ�
		su.searchByWildcard("email", "*@itat.org", 10);
		//ƥ��j��ͷ���������ַ���name
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
		//1������QueryParser����,Ĭ��������Ϊcontent
		QueryParser parser = new QueryParser(Version.LUCENE_35, "content", new StandardAnalyzer(Version.LUCENE_35));
		//�ı�ո��Ĭ�ϲ����������¿��Ըĳ�AND
		//parser.setDefaultOperator(Operator.AND);
		//������һ���ַ���ͨ���ƥ�䣬Ĭ�Ϲر���ΪЧ�ʲ���
		parser.setAllowLeadingWildcard(true);
		//����content�а�����like��
		Query query = parser.parse("like");
		
		//��basketball����football�ģ��ո�Ĭ�Ͼ���OR
		query = parser.parse("basketball football");
		
		//�ı�������ΪnameΪmike
		//query = parser.parse("content:like");
		
		//ͬ������ʹ��*��?������ͨ���ƥ��
//		query = parser.parse("name:j*");
		
		//ͨ���Ĭ�ϲ��ܷ�����λ
//		query = parser.parse("email:*@itat.org");
		
		//ƥ��name��û��mike����content�б�����football�ģ�+��-Ҫ���õ���˵��ǰ��
		query = parser.parse("- name:mike + like");
		
		//ƥ��һ�����䣬ע��:TO�����Ǵ�д
		//query = parser.parse("id:[1 TO 6]");
		
		//������ƥ��ֻ��ƥ�䵽2
		//query = parser.parse("id:{1 TO 3}");
		
		//��ȫƥ��I Like Football��
		//query = parser.parse("\"I like football\"");
		
		//ƥ��I ��football֮����һ�����ʾ����
		//query = parser.parse("\"I football\"~1");
		
		//ģ����ѯ
		//query = parser.parse("name:make~");
		
		//û�а취ƥ�����ַ�Χ���Լ���չParser��
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
