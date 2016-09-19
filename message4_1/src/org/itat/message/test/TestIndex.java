package org.itat.message.test;

import org.itat.message.iservice.IIndexService;
import org.itat.message.util.SystemContext;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestIndex {
	private static BeanFactory factory;
	private static IIndexService indexService;
	
	@BeforeClass
	public static void init() {
		factory = new ClassPathXmlApplicationContext("beans.xml");
		indexService = (IIndexService)factory.getBean("indexService");
	}
	
	@Test
	public void testReconstructor() {
		SystemContext.setRealPath("D:/kh/j2ee/msg_myeclipse/message3_4/WebRoot");
		indexService.updateReconstructorIndex();
//		indexService.updateSetIndex();
	}
}
