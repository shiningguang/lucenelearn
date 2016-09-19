package org.itat.lucene.solr.test;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CommonsHttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Before;
import org.junit.Test;

public class SolrTest {
	private final static String URL = "http://localhost:8080/solr";
	private CommonsHttpSolrServer server = null;
	
	@Before
	public void init() {
		try {
			server = new CommonsHttpSolrServer(URL);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void test01() {
		try {
			SolrInputDocument doc = new SolrInputDocument();
			//id是唯一的主键，当多次添加的时候，最后添加的相同id的域会覆盖前面的域
			doc.addField("id","1");
			doc.addField("msg_title", "这是我的第一个solrj的程序");
			doc.addField("msg_content","我的solrj的程序究竟能不能跑得起来呢？");
			server.add(doc);
			server.commit();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (SolrServerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void test02() {
		try {
			List<SolrInputDocument> docs = new ArrayList<SolrInputDocument>();
			SolrInputDocument doc = new SolrInputDocument();
			doc.addField("id", "2");
			doc.addField("msg_title", "很好！solr可以工作了");
			doc.addField("msg_content","slor总算可以正式工作了");
			docs.add(doc);
			doc = new SolrInputDocument();
			doc.addField("id", "3");
			doc.addField("msg_title", "测试一下solr的添加");
			doc.addField("msg_content","看看能不能添加一个列表信息");
			docs.add(doc);
			server.add(docs);
			server.commit();
		} catch (SolrServerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void test03() {
		try {
			List<Message> msgs = new ArrayList<Message>();
			msgs.add(new Message("4","基于java bean的添加",
					 new String[]{"通过java bean完成添加","java bean的添加附件"}));
			msgs.add(new Message("5","基于java bean的列表数据的添加",
					 new String[]{"测试如何通过一个对象完成添加","通过对象完成添加的附件"}));
			server.addBeans(msgs);
			server.commit();
		} catch (SolrServerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void test04() {
		try {
			//定义查询字符串
			SolrQuery query = new SolrQuery("*");
			query.setStart(0);
			query.setRows(3);
			QueryResponse resp = server.query(query);
			//查询出来的结果都保存在SolrDocumentList中
			SolrDocumentList sdl = resp.getResults();
			System.out.println(sdl.getNumFound());
			for(SolrDocument sd:sdl) {
//				System.out.println(sd);
				System.out.println(sd.getFieldValue("msg_title")+","+sd.getFieldValue("msg_content"));
			}
		} catch (SolrServerException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void test05() {
		try {
			SolrQuery query = new SolrQuery("*");
			query.setStart(0);
			query.setRows(3);
			QueryResponse resp = server.query(query);
			//可以直接查询相应的bean对象，但是不是很常用
			List<Message> list = resp.getBeans(Message.class);
			System.out.println(list.size());
			for(Message msg:list) {
				System.out.println(msg.getTitle());
			}
		} catch (SolrServerException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void test06() {
		try {
			SolrQuery query = new SolrQuery("测试");
			query.setHighlight(true).setHighlightSimplePre("<span class='highligter'>")
								.setHighlightSimplePost("</span>")
								.setStart(0).setRows(5);
			query.setParam("hl.fl", "msg_title,msg_content");
			QueryResponse resp = server.query(query);
			//查询出来的结果都保存在SolrDocumentList中
			SolrDocumentList sdl = resp.getResults();
			System.out.println(sdl.getNumFound());
			for(SolrDocument sd:sdl) {
				String id = (String)sd.getFieldValue("id");
				System.out.println(resp.getHighlighting().get(id).get("msg_content"));
			}
		} catch (SolrServerException e) {
			e.printStackTrace();
		}
	}
}
