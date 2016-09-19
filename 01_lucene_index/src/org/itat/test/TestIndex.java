package org.itat.test;

import org.itat.index.IndexUtil;
import org.junit.Test;

public class TestIndex {
	
	@Test
	public void testIndex() {
		IndexUtil iu = new IndexUtil();
		iu.index();
	}
	
	@Test
	public void testQuery() {
		IndexUtil iu = new IndexUtil();
		iu.query();
	}
	
	@Test
	public void testDelete() {
		IndexUtil iu = new IndexUtil();
		iu.delete();
	}
	
	@Test
	public void testDelete02() {
		IndexUtil iu = new IndexUtil();
		iu.delete02();
	}
	
	@Test
	public void testUnDelete() {
		IndexUtil iu = new IndexUtil();
		iu.undelete();
	}
	
	@Test
	public void testForceDelete() {
		IndexUtil iu = new IndexUtil();
		iu.forceDelete();
	}
	
	@Test
	public void testMerge() {
		IndexUtil iu = new IndexUtil();
		iu.merge();
	}
	
	@Test
	public void testUpdate() {
		IndexUtil iu = new IndexUtil();
		iu.update();
	}
	
	@Test
	public void testSearch01() {
		IndexUtil iu = new IndexUtil();
		iu.search01();
	}
	
	@Test
	public void testSearch02() {
		IndexUtil iu = new IndexUtil();
		for(int i=0;i<5;i++) {
			iu.search02();
			System.out.println("-----------------------------");
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
