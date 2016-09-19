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
		//Sort.INDEXORDER通过doc的id进行排序
		st.searcherBySort("java",Sort.INDEXORDER);
		
		//使用默认的评分排序
		//st.seacher("java", Sort.RELEVANCE);
		
//		st.seacher("java", null);
		
		//通过文件的大小排序
//		st.seacher("java",new Sort(new SortField("size",SortField.INT)));
		//通过日期排序
		//st.seacher("java",new Sort(new SortField("date",SortField.LONG)));
		//通过文件名排序
		//st.seacher("java", new Sort(new SortField("filename", SortField.STRING)));
		
		//通过设置SortField最后一个参数设置是否反转排序
		//st.seacher("java", new Sort(new SortField("filename", SortField.STRING,true)));
	
		//st.seacher("java", new Sort(new SortField("size",SortField.INT),SortField.FIELD_SCORE));
	}
	
	@Test
	public void test02() {
		Filter tr = new TermRangeFilter("filename", "java.hhh","java.she",true, true);
		tr = NumericRangeFilter.newIntRange("size",500,900,true,true);
		//可以通过一个Query进行过滤
		tr = new QueryWrapperFilter(new WildcardQuery(new Term("filename","*.txt")));
		st.searcherByFilter("java", tr);
	}
	
	@Test
	public void test03() {
		Query q = new WildcardQuery(new Term("filename","A*"));
		st.searcherByQuery(q);
	}
}
