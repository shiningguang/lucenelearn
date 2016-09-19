package org.itat.message.dao;


import org.itat.message.idao.ITempIndexDao;
import org.itat.message.vo.TempIndex;
import org.springframework.stereotype.Repository;

@Repository("tempIndexDao")
public class TempIndexDao extends BaseDao<TempIndex> implements ITempIndexDao {

	@Override
	public void delAll() {
		this.getSession().createSQLQuery("truncate table temp_index").executeUpdate();
	}


}
