
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
 * 留言对象
 * @author Konghao
 *
 */
@Entity
@Table(name="t_message")
public class Message {
	private int id;
	private String title;
	private String content;
	private User user;
	private Date createDate;
	/**
	 * @return 留言 标识
	 */
	@Id
	@GeneratedValue
	public int getId() {
		return id;
	}
	/**
	 * @param 设置留言标识
	 */
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * @return 留言标题
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param 设置留言的标题
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * @return 留言内容
	 */
	public String getContent() {
		return content;
	}
	/**
	 * @param 设置留言内容
	 */
	public void setContent(String content) {
		this.content = content;
	}
	/**
	 * @return 留言的发布人，数据库中存储的是user_id的外键
	 */
	@ManyToOne
	@JoinColumn(name="user_id")
	public User getUser() {
		return user;
	}
	/**
	 * @param 设置留言的发布人，数据库中存储的是user_id的外键
	 */
	public void setUser(User user) {
		this.user = user;
	}
	/**
	 * @return 留言的发布日期
	 */
	@Column(name="create_date")
	public Date getCreateDate() {
		return createDate;
	}
	/**
	 * @param 设置留言的发布日期
	 */
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
}
