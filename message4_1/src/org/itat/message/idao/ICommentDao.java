package org.itat.message.idao;

import org.itat.message.vo.Comment;


public interface ICommentDao extends IBaseDao<Comment>{
	public void deleteByUser(int id);
	public void deleteByMessage(int id);
}
