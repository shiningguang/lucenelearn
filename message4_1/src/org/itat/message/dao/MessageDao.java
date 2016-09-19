package org.itat.message.dao;


import java.util.List;

import org.itat.message.idao.IMessageDao;
import org.itat.message.vo.Message;
import org.springframework.stereotype.Repository;

@Repository("messageDao")
public class MessageDao extends BaseDao<Message> implements IMessageDao {

	public List<Message> listByUser(int id) {
		return this.list("from Message m where m.user.id=?",id);
	}

}
