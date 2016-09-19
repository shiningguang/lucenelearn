package org.itat.message.idao;

import java.util.List;

import org.itat.message.util.PageObject;

public interface IBaseDao<T> {
	public T add(T t);
	public void update(T t);
	public void delete(T t);
	public void delete(int id);
	public List<T> list(String hql,Object[] args);
	public List<T> list(String hql);
	public List<T> list(String hql,Object arg);
	public PageObject<T> find(String hql,Object[] args);
	public PageObject<T> find(String hql,Object arg);
	public PageObject<T> find(String hql);
	public T loadByHql(String hql,Object[] args);
	public T loadByHql(String hql,Object arg) ;
	public T loadByHql(String hql);
	public T load(int id);
	
	public Object loadObjByHQL(String hql,Object[] args);
	public Object loadObjByHQL(String hql,Object arg);
	public Object loadObjByHQL(String hql);
}
