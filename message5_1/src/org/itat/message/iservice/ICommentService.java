package org.itat.message.iservice;

import org.itat.message.util.PageObject;
import org.itat.message.vo.Comment;

public interface ICommentService {
	/**
	 * 添加评论
	 * 需要向判断留言和用户对象是否存在
	 * @param c 添加的评论对象
	 * @param userId 添加的用户对象的id
	 * @param messageId 添加的留言对象的id
	 */
	public Comment add(Comment c,int userId,int messageId);
	
	/**
	 * 删除留言
	 * @param id
	 */
	public void delete(int id);
	/**
	 * 根据留言对象来分页获取所有的评论
	 * @param messageId
	 * @return
	 */
	public PageObject<Comment> findByMsg(int messageId);
	/**
	 * 根据用户对象来分页获取所有的评论
	 * @param userId
	 * @return
	 */
	public PageObject<Comment> findByUser(int userId);
	/**
	 * 获取所有评论信息
	 * @return
	 */
	public PageObject<Comment> findAllComment();
}
