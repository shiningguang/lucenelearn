package org.itat.message.util;

import java.io.IOException;
import java.io.StringWriter;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

public class JsonJacksonUtil {
	private final static JsonJacksonUtil util = new JsonJacksonUtil();
	private final static JsonFactory factory = new JsonFactory();
	
	public static JsonJacksonUtil getInstance() {
		return util;
	}
	
	public String obj2json(Object obj) {
		//通过jsonFactory创建JsonGenerator,要传入相应的输出流
		StringWriter out = new StringWriter();
		JsonGenerator jg = null;
		try {
			jg = factory.createJsonGenerator(out);
			//创建对象mapper
			ObjectMapper mapper = new ObjectMapper();
			mapper.writeValue(jg, obj);
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			try {
				if(jg!=null) jg.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return out.getBuffer().toString();
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Object json2obj(String json,Class clazz) {
		ObjectMapper mapper = new ObjectMapper();
		Object o = null;
		try {
			o = mapper.readValue(json, clazz);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return o;
	}
}
