package org.itat.lucene.test;

import org.itat.lucene.util.CustomFilter;
import org.junit.Test;

public class TestCustomFilter {

	@Test
	public void test01() {
		CustomFilter cf = new CustomFilter();
		cf.searchByCustomFilter();
	}
}
