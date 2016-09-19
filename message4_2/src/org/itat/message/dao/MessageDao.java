package org.itat.message.dao;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.itat.message.idao.IMessageDao;
import org.itat.message.vo.Message;
import org.springframework.stereotype.Repository;

@Repository("messageDao")
public class MessageDao extends BaseDao<Message> implements IMessageDao {

	public List<Message> listByUser(int id) {
		return this.list("from Message m where m.user.id=?",id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<Integer,String> listMessageContent(List<Integer> ids) {
		String hql = "select id,content from Message where id in(:ids)";
		List<Object[]> content = this.getSession().createQuery(hql).setParameterList("ids", ids).list();
		Map<Integer,String> maps = new HashMap<Integer, String>();
		for(Object[] obj:content) {
			maps.put((Integer)obj[0],(String)obj[1]);
		}
		return maps;
	}

}
