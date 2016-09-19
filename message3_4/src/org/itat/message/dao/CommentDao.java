package org.itat.message.dao;


import org.itat.message.idao.ICommentDao;
import org.itat.message.vo.Comment;
import org.springframework.stereotype.Repository;

@Repository("commentDao")
public class CommentDao extends BaseDao<Comment> implements ICommentDao {

	public void deleteByUser(int id) {
		super.updateByHql("delete c from Comment c where c.user.id=?", id);
	}

	public void deleteByMessage(int id) {
		super.updateByHql("delete c from Comment c where c.message.id=?", id);
	}

}
