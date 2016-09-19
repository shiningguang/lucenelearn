package org.itat.message.vo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table(name="t_attachment")
public class Attachment {
	private int id;
	private String newName;
	private String oldName;
	private String contentType;
	private Date createDate;
	private Message message;
	
	public Attachment(String newName, String oldName, String contentType,
			Date createDate) {
		super();
		this.newName = newName;
		this.oldName = oldName;
		this.contentType = contentType;
		this.createDate = createDate;
	}
	
	public Attachment(){}
	
	/**
	 * 附件标识
	 * @return
	 */
	@Id
	@GeneratedValue
	public int getId() {
		return id;
	}
	/**
	 * 设置附件标识
	 * @param id
	 */
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * 获取附件的新名称，当附件上传之后，会根据当前的时间来获取一个名称（名称是唯一的）
	 * @return
	 */
	@Column(name="new_name")
	public String getNewName() {
		return newName;
	}
	/**
	 * 设置附件的新名称
	 * @param newName
	 */
	public void setNewName(String newName) {
		this.newName = newName;
	}
	/**
	 * 获取附件的原始名称
	 * @return
	 */
	@Column(name="old_name")
	public String getOldName() {
		return oldName;
	}
	/**
	 * 设置附件的原始名称
	 * @param oldName
	 */
	public void setOldName(String oldName) {
		this.oldName = oldName;
	}
	/**
	 * 获取附件的文件类型
	 * @return
	 */
	@Column(name="content_type")
	public String getContentType() {
		return contentType;
	}
	/**
	 * 设置附件的文件类型
	 * @param contentType
	 */
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	/**
	 * 获取附件的创建时间
	 * @return
	 */
	@Column(name="create_date")
	public Date getCreateDate() {
		return createDate;
	}
	/**
	 * 设置附件 的创建 时间
	 * @param createDate
	 */
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
	@ManyToOne
	@JoinColumn(name="msg_id")
	public Message getMessage() {
		return message;
	}

	public void setMessage(Message message) {
		this.message = message;
	}
	
	
}
