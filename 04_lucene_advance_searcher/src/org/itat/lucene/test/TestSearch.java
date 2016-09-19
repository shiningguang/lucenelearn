package org.itat.lucene.test;

import org.apache.lucene.index.Term;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.NumericRangeFilter;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.QueryWrapperFilter;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.TermRangeFilter;
import org.apache.lucene.search.WildcardQuery;
import org.itat.lucene.util.FileIndexUtils;
import org.itat.lucene.util.SearchTest;
import org.junit.Before;
import org.junit.Test;

public class TestSearch {
	private SearchTest st;
	
	@Before
	public void init() {
		st = new SearchTest();
	}
	

	@Test
	public void index() {
		FileIndexUtils.index(true);
	}
	
	@Test
	public void test01() {
		//Sort.INDEXORDERͨ��doc��id��������
		st.searcherBySort("java",Sort.INDEXORDER);
		
		//ʹ��Ĭ�ϵ���������
		//st.seacher("java", Sort.RELEVANCE);
		
//		st.seacher("java", null);
		
		//ͨ���ļ��Ĵ�С����
//		st.seacher("java",new Sort(new SortField("size",SortField.INT)));
		//ͨ����������
		//st.seacher("java",new Sort(new SortField("date",SortField.LONG)));
		//ͨ���ļ�������
		//st.seacher("java", new Sort(new SortField("filename", SortField.STRING)));
		
		//ͨ������SortField���һ�����������Ƿ�ת����
		//st.seacher("java", new Sort(new SortField("filename", SortField.STRING,true)));
	
		//st.seacher("java", new Sort(new SortField("size",SortField.INT),SortField.FIELD_SCORE));
	}
	
	@Test
	public void test02() {
		Filter tr = new TermRangeFilter("filename", "java.hhh","java.she",true, true);
		tr = NumericRangeFilter.newIntRange("size",500,900,true,true);
		//����ͨ��һ��Query���й���
		tr = new QueryWrapperFilter(new WildcardQuery(new Term("filename","*.txt")));
		st.searcherByFilter("java", tr);
	}
	
	@Test
	public void test03() {
		Query q = new WildcardQuery(new Term("filename","A*"));
		st.searcherByQuery(q);
	}
}
