package org.lucene.util;

import java.util.HashMap;
import java.util.Map;

public class SimpleSamewordContext implements SamewordContext {
	
	Map<String,String[]> maps = new HashMap<String,String[]>();
	public SimpleSamewordContext() {
		maps.put("中国",new String[]{"天朝","大陆"});
		maps.put("我",new String[]{"咱","俺"});
	}

	@Override
	public String[] getSamewords(String name) {
		return maps.get(name);
	}

}
