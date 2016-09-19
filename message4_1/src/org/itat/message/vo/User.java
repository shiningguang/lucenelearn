package org.itat.message.vo;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.BatchSize;

import com.sun.org.apache.commons.beanutils.BeanUtils;

@Entity
@Table(name="t_user")
@BatchSize(size=15)
public class User {
	private int id;
	private String username;
	private String password;
	private String nickname;
	private int type;
	private int status;
	private String email;
	private int hiddenMsg;
	private Date createDate;
	
	
	public void setProperty(String name,String value) {
		try {
			BeanUtils.copyProperty(this, name, value);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取主键
	 * @return 主键
	 */
	@Id
	@GeneratedValue
	public int getId() {
		return id;
	}
	/**
	 * 设置主键
	 * @param id 传入的主键
	 */
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * 获取登录的用户名，此处的用户名全部用英文或者数字表示，用户名不能重复
	 * @return 用户名
	 */
	public String getUsername() {
		return username;
	}
	/**
	 * 设置登录的用户名，此处的用户名全部用英文或者数字表示，用户名不能重复
	 * @param username
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	/**
	 * 获取用户密码
	 * @return 用户密码
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * 设置用户密码
	 * @param password
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	/**
	 * 获取用户昵称
	 * @return
	 */
	public String getNickname() {
		return nickname;
	}
	/**
	 * 设置用户昵称
	 * @param nickname
	 */
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	/**
	 * 获取用户类型：1、普通用户，3表示超级管理员,0表示匿名用户
	 * @return 用户类型
	 */
	public int getType() {
		return type;
	}
	/**
	 * 设置用户类型：1、普通用户，3表示超级管理员
	 * @param type
	 */
	public void setType(int type) {
		this.type = type;
	}
	/**
	 * 设置用户的状态，有停用和启用两种状态：0表示启用，1表示停用，当用户停用之后无法登录
	 * @return 用户状态
	 */
	public int getStatus() {
		return status;
	}
	/**
	 * 获取用户的状态，有停用和启用两种状态：0表示启用，1表示停用，当用户停用之后无法登录
	 * @param status
	 */
	public void setStatus(int status) {
		this.status = status;
	}
	/**
	 * 设置用户邮箱，邮箱应该唯一，扩展之后可以考虑使用邮箱登录系统是
	 * @return
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * 设置用户邮箱
	 * @param email
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	/**
	 * 获取用户是否被禁止发布信息，当用户被禁止发布信息之后连用户原来信息都会不显示
	 * 0表示不禁止，1表示禁止
	 * @return
	 */
	@Column(name="hidden_msg")
	public int getHiddenMsg() {
		return hiddenMsg;
	}
	/**
	 * 设置用户是否被禁止发布信息，当用户被禁止发布信息之后连用户原来信息都会不显示
	 * 0表示不禁止，1表示禁止
	 * @param hiddenMsg
	 */
	public void setHiddenMsg(int hiddenMsg) {
		this.hiddenMsg = hiddenMsg;
	}
	/**
	 * 设置用户的创建时间
	 * @return
	 */
	@Column(name="create_date")
	public Date getCreateDate() {
		return createDate;
	}
	/**
	 * 获取用户的创建时间
	 * @param createDate
	 */
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
}
