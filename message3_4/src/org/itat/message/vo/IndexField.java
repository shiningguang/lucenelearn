package org.itat.message.vo;

import java.util.Date;

/**
 * 要添加的索引域对象,目的是将相应的对象转换为IndexField之后传输到IIndexService进行更新操作
 * @author Administrator
 *
 */
public class IndexField {
	private String id;
	private String title;
	private String content;
	private int parentId;
	private int objId;
	private Date createDate;
	private String type;
	
	/**
	 * 操作的对象类型，可能是Message或者Attachment
	 * @return
	 */
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * 这个索引的唯一标识：如果是留言使用0_留言id来表示
	 * 如果是附件使用 留言ID_附件ID来表示
	 * @return
	 */
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * 留言的标题
	 * @return
	 */
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	/**
	 * 留言的内容，转换后的内容（通过Tika转换）
	 * @return
	 */
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	/**
	 * 当前对象的父类id，如果是留言，该id为0
	 * @return
	 */
	public int getParentId() {
		return parentId;
	}
	public void setParentId(int parentId) {
		this.parentId = parentId;
	}
	/**
	 * 当前存储对象的id
	 * @return
	 */
	public int getObjId() {
		return objId;
	}
	public void setObjId(int objId) {
		this.objId = objId;
	}
	/**
	 * 创建的时间
	 * @return
	 */
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
}
