package org.itat.message.action;

import javax.annotation.Resource;

import org.itat.message.iservice.IIndexService;
import org.itat.message.util.ActionUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

@Controller("indexAction")
@Scope("prototype")
public class IndexAction extends ActionSupport{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private IIndexService indexService;
	private String con;
	
	

	public String getCon() {
		return con;
	}
	public void setCon(String con) {
		this.con = con;
	}
	public IIndexService getIndexService() {
		return indexService;
	}
	@Resource
	public void setIndexService(IIndexService indexService) {
		this.indexService = indexService;
	}

	public String show() {
		return SUCCESS;
	}
	
	public String commit() {
		indexService.updateCommitIndex();
		ActionContext.getContext().put("urlAction", "index_show.action");
		return ActionUtil.REDIRECT;
	}
	
	public String reconstructor() {
		indexService.updateReconstructorIndex();
		ActionContext.getContext().put("urlAction", "index_show.action");
		return ActionUtil.REDIRECT;	
	}
	
	public String updateSet() {
		indexService.updateSetIndex();
		ActionContext.getContext().put("urlAction", "index_show.action");
		return ActionUtil.REDIRECT;	
	}
	
	public String search() {
		ActionContext.getContext().put("pages", indexService.findByIndex(con));
		return SUCCESS;
	}
}
