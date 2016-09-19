package org.itat.message.iservice;

import java.util.List;

import org.itat.message.util.PageObject;
import org.itat.message.vo.Message;

public interface IMessageService {
	/**
	 * 添加留言信息
	 * 首先需要判断留言的用户是否存在，如果不存在就抛出异常
	 * @param m 要添加留言对象
	 * @param userId 留言的用户
	 */
	public void add(Message m, int userId,Integer[] atts);
	/**
	 * 修改留言信息
	 * @param m 要修改的留言对象
	 */
	public void update(Message m);
	/**
	 * 删除留言信息
	 * 删除留言之前需要先删除评论
	 * @param id 要删除的留言id
	 */
	public void delete(int id);
	/**
	 * 根据id获取留言对象
	 * @param id 要获取留言对象的id
	 * @return 查询出来的留言对象
	 */
	public Message load(int id);
	/**
	 * 根据条件在标题中进行查询并获取所有的留言信息
	 * @param keyword 要获取留言信息的条件
	 * @return 留言列表
	 */
	public List<Message> listByTitle(String keyword);
	/**
	 * 根据条件在标题或者内容中进行查询并获取所有的留言信息
	 * @param keyword 要获取留言信息的条件
	 * @return 留言列表
	 */
	public List<Message> listByContent(String keyword);
	/**
	 * 根据条件在标题中进行分页查询留言信息
	 * @param keyword 要查询留言的条件
	 * @return 分页查询的留言对象
	 */
	public PageObject<Message> findByTitle(String keyword);
	/**
	 * 根据条件在标题和内容中进行分页查询留言信息
	 * @param keyword 要查询留言的条件
	 * @return 分页查询的留言对象
	 */
	public PageObject<Message> findByContent(String keyword);
	/**
	 * 根据用户信息获取所有的留言对象
	 * @param userId
	 * @return
	 */
	public List<Message> listByUser(int userId);
	/**
	 * 根据条件查询相应的留言列表
	 * @param condition
	 * @return
	 */
	public PageObject<Message> findByCondition(String condition);
}
