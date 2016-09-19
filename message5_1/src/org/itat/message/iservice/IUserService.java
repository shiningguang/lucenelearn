package org.itat.message.iservice;

import java.util.List;

import org.itat.message.util.PageObject;
import org.itat.message.vo.Message;
import org.itat.message.vo.User;

public interface IUserService {
	
	/**
	 * 添加用户
	 * 判断用户名是否唯一，如果不唯一抛出异常
	 * @param u 要添加的用户
	 */
	public void add(User u);
	/**
	 * 更新用户
	 * 不能更新用户名
	 * @param u 要更新的用户
	 */
	public void update(User u);
	/**
	 * 删除用户
	 * 删除用户首先需要删除用户所发表的所有评论和留言
	 * @param id
	 */
	public void delete(int id);
	/**
	 * 根据用户id来获取相应的用户
	 * @param id 需要获取用户的主键id
	 * @return 需要获取的用户
	 */
	public User load(int id);
	/**
	 * 获取所有用户列表
	 * @return 所有用户列表
	 */
	public List<User> list();
	/**
	 * 用户 登录
	 * @param username 用户的登录用户名
	 * @param pwd 用户的登录密码
	 * @return
	 */
	public User login(String username,String pwd);
	/**
	 * 根据分页获取用户信息
	 * @param pageOffset 从哪一个位置开始查询
	 * @param pageSize 每页显示多少条
	 * @return
	 */
	public PageObject<User> find(String name);
	/**
	 * 获取某个用户的所有留言对象
	 * @param userId
	 * @return
	 */
	public List<Message> listMsgByUser(int userId);
	/**
	 * 根据用户名获取用户对象
	 * @param username
	 * @return
	 */
	public User loadByUsername(String username);
	/**
	 * 检查用户是否存在
	 * @param username
	 * @return
	 */
	public boolean checkUser(String username);
	
}
