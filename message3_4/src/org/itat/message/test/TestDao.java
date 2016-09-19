package org.itat.message.test;

import java.util.List;

import org.itat.message.idao.IUserDao;
import org.itat.message.vo.User;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestDao {
	private static BeanFactory factory;
	private static IUserDao userDao;
	
	@BeforeClass
	public static void init() {
		factory = new ClassPathXmlApplicationContext("beans.xml");
		userDao = (IUserDao)factory.getBean("userDao");
	}
	
	@Test
	public void testUserList() {
		List<User> users = userDao.list("from User where nickname like ?",new Object[]{"%张%"});
		for(User u:users) {
			System.out.println(u.getNickname());
		}
	}
	
	@Test
	public void testAddUser() {
		User u = new User();
		u.setUsername("sssdfsdf");
		userDao.add(u);
	}
	@Test
	public void load() {
		User u = userDao.load(1);
		System.out.println(u.getNickname());
	}
}
