package org.itat.message.index;

import java.net.MalformedURLException;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.CommonsHttpSolrServer;

public class SolrContext {
	private final static String URL = "http://localhost:8080/solr";
	private static CommonsHttpSolrServer server = null;
	static{
		try {
			server = new CommonsHttpSolrServer(URL);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
	
	public static SolrServer getServer() {
		return server;
	}
	
}
