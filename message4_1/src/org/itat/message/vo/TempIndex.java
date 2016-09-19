package org.itat.message.vo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.itat.message.util.IndexUtil;

@Entity
@Table(name="temp_index")
public class TempIndex {
	
	

	private int id;
	private int objId;
	private String type;//Message,Attachment
	private int operator;
	
	public void setOperator(int operator) {
		this.operator = operator;
	}

	public void setDelete() {
		operator = IndexUtil.DEL_OP;
	}
	
	public void setAdd() {
		operator = IndexUtil.ADD_OP;
	}
	
	public void setUpdate() {
		operator = IndexUtil.UPDATE_OP;
	}
	
	@Id
	@GeneratedValue
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	/**
	 * 要操作的对象id，可能是MEssage也可能是Attachment
	 * @return
	 */
	@Column(name="obj_id")
	public int getObjId() {
		return objId;
	}
	public void setObjId(int objId) {
		this.objId = objId;
	}
	/**
	 * 需要操作的对象类型，Message和Attachment
	 * @return
	 */
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * 操作的类型是Add还是Delete或者Update
	 * @return
	 */
	public int getOperator() {
		return operator;
	}
	
}
