package org.itat.message.vo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 留言评论对象
 * @author Administrator
 *
 */
@Entity
@Table(name="t_comment")
public class Comment {
	private int id;
	private String content;
	private Date createDate;
	private User user;
	private Message message;
	
	/**
	 * @return 评论标识
	 */
	@GeneratedValue
	@Id
	public int getId() {
		return id;
	}
	/**
	 * @param 设置评论标识
	 */
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * @return 评论的内容
	 */
	public String getContent() {
		return content;
	}
	/**
	 * @param 设置评论的内容
	 */
	public void setContent(String content) {
		this.content = content;
	}
	/**
	 * @return 评论的创建时间
	 */
	@Column(name="create_date")
	public Date getCreateDate() {
		return createDate;
	}
	/**
	 * @param 设置评论的创建时间
	 */
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	/**
	 * @return 评论的用户通过user_id外键关联
	 */
	@ManyToOne
	@JoinColumn(name="user_id")
	public User getUser() {
		return user;
	}
	/**
	 * @param 设置评论的用户通过user_id外键关联
	 */
	public void setUser(User user) {
		this.user = user;
	}
	/**
	 * @return 评论所属的留言 通过message_id完成管理
	 */
	@ManyToOne
	@JoinColumn(name="message_id")
	public Message getMessage() {
		return message;
	}
	/**
	 * @param 评论所属的留言 通过message_id完成管理
	 */
	public void setMessage(Message message) {
		this.message = message;
	}
	
	
}
