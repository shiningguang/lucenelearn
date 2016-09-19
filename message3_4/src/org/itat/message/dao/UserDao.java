package org.itat.message.dao;


import org.itat.message.idao.IUserDao;
import org.itat.message.vo.User;
import org.springframework.stereotype.Repository;

@Repository("userDao")
public class UserDao extends BaseDao<User> implements IUserDao {


}
