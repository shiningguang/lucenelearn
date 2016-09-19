package org.itat.message.dao;

import java.lang.reflect.ParameterizedType;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.itat.message.idao.IBaseDao;
import org.itat.message.util.PageObject;
import org.itat.message.util.SystemContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

@Repository("baseDao")
public class BaseDao<T> extends HibernateDaoSupport implements IBaseDao<T>{
	private Class<T> clz;
	@SuppressWarnings("unchecked")
	private Class<T> getClz() {
		if(clz==null)
			clz = (Class<T>)((ParameterizedType)this.getClass()
						.getGenericSuperclass()).getActualTypeArguments()[0];
		return clz;
	}
	@Resource
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	/**
	 * 添加对象
	 */
	public T add(T t) {
		this.getHibernateTemplate().save(t);
		return t;
	}
	/**
	 * 更新对象
	 */
	public void update(T t) {
		this.getHibernateTemplate().update(t);
	}
	/**
	 * 删除对象
	 */
	public void delete(T t) {
		this.getHibernateTemplate().delete(t);
	}
	/**
	 * 根据ID删除对象
	 */
	public void delete(int id) {
		delete(load(id));
	}
	/**
	 * 根据条件列表数据，不分页
	 */
	@SuppressWarnings("unchecked")
	public List<T> list(String hql,Object[] args) {
		String order = SystemContext.getOrder();
		String sort = SystemContext.getSort();
		if(sort!=null&&!"".equals(sort.trim())) {
			hql = hql+" order by "+sort;
			if(order!=null&&!"".equals(order.trim())) {
				hql = hql+ " "+order;
			} else {
				hql = hql+" asc";
			}
		}
		Query q = this.getSession().createQuery(hql);
		if(args!=null) {
			int index = 0;
			for(Object arg:args) {
				q.setParameter(index++, arg);
			}
		}
		
		return q.list();
	}
	
	public List<T> list(String hql) {
		return list(hql,null);
	}
	
	public List<T> list(String hql,Object arg) {
		return list(hql,new Object[]{arg});
	}
	/**
	 * 根据条件列表数据，需要分页
	 * 分页信息和排序信息均通过ThreadLocal传递
	 */
	@SuppressWarnings("unchecked")
	public PageObject<T> find(String hql,Object[] args) {
		PageObject<T> pages = new PageObject<T>();
		String sort = SystemContext.getSort();
		String order = SystemContext.getOrder();
		int pageSize = SystemContext.getPageSize();
		int pageOffset = SystemContext.getPageOffset();
		String countHql = getCountHql(hql);
		if(sort!=null&&!"".equals(sort.trim())) {
			hql+=" order by "+sort;
			if(order!=null&&!"".equals(order.trim())) {
				hql+=" "+order;
			} else {
				hql+=" asc";
			}
		}
		Query query = setQuery(hql,args);
		Query countQuery = setQuery(countHql,args);
		List<T> datas = query.setFirstResult(pageOffset)
							 .setMaxResults(pageSize).list();
		pages.setDatas(datas);
		pages.setOffset(pageOffset);
		pages.setPageSize(pageSize);
		Long totalRecord = (Long)countQuery.uniqueResult();
		pages.setTotalRecord(totalRecord.intValue());
		return pages;
	} 
	
	public PageObject<T> find(String hql,Object arg) {
		return find(hql,new Object[]{arg});
	}
	
	public PageObject<T> find(String hql) {
		return find(hql,null);
	}
	/**
	 * 根据条件来列表相应的对象
	 */
	@SuppressWarnings("unchecked")
	public T loadByHql(String hql,Object[] args) {
		T t = null;
		Query q = setQuery(hql,args);
		t = (T)q.uniqueResult();
		return t;
	}
	
	public T loadByHql(String hql,Object arg) {
		return loadByHql(hql,new Object[]{arg});
	}
	public T loadByHql(String hql) {
		return loadByHql(hql,null);
	}
	/**
	 * 根据hql执行更新操作
	 * @param hql
	 * @param args
	 */
	protected void updateByHql(String hql,Object[] args) {
		Query q = setQuery(hql,args);
		q.executeUpdate();
	}
	
	protected void updateByHql(String hql,Object arg) {
		updateByHql(hql, new Object[]{arg});
	}
	
	protected void updateByHql(String hql) {
		updateByHql(hql, null);
	}
	
	private String getCountHql(String hql) {
		String str = hql.substring(hql.indexOf("from "));
		str = "select count(*) "+str;
		str = str.replaceAll("fetch","");
		return str;
	}
	/**
	 * 根据id加载对象
	 */
	public T load(int id) {
		return this.getHibernateTemplate().get(getClz(), id);
	}
	
	private Query setQuery(String hql,Object[] args) {
		Query q = this.getSession().createQuery(hql);
		if(args!=null) {
			int index = 0;
			for(Object arg:args) {
				q.setParameter(index++, arg);
			}
		}
		return q;
	}
	public Object loadObjByHQL(String hql, Object[] args) {
		Object t = null;
		Query q = setQuery(hql,args);
		t = (Object)q.uniqueResult();
		return t;
	}
	public Object loadObjByHQL(String hql, Object arg) {
		return loadObjByHQL(hql,new Object[]{arg});
	}
	public Object loadObjByHQL(String hql) {
		return loadObjByHQL(hql,null);
	}
}
