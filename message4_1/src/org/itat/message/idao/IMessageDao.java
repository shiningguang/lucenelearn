package org.itat.message.idao;

import java.util.List;

import org.itat.message.vo.Message;


public interface IMessageDao extends IBaseDao<Message>{
	public List<Message> listByUser(int id);
}
