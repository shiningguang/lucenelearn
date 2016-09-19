package org.itat.message.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class PropertiesUtils {
	private static Properties jdbcProperties;
	private static Properties daoProperties;
	private static Map<String,Properties> maps = new HashMap<String, Properties>();
	
	/**
	 * 使用map来实现第二种单例模式
	 * @param name
	 * @return
	 */
	public static Properties createProperties(String name) {
		if(maps.get(name)!=null) {
			return maps.get(name);
		} else {
			Properties properties = new Properties();
			try {
				properties.load(PropertiesUtils.class
						.getClassLoader()
						.getResourceAsStream("org/itat/message/util/"+name+".properties"));
			} catch (IOException e) {
				return null;
			}
			maps.put(name,properties);
			return properties;
		}
	}
	
	/*
	 * 使用单例模式来读取properties文件
	 */
	public static Properties createJDBCProperties() {
		if(jdbcProperties==null) {
			jdbcProperties = new Properties();
			try {
				jdbcProperties.load(PropertiesUtils.class
						.getClassLoader()
						.getResourceAsStream("org/itat/message/util/jdbc.properties"));
			} catch (IOException e) {
				e.printStackTrace();
			}
			return jdbcProperties;
		} else {
			return jdbcProperties;
		}
	}
	
	public static Properties createDaoProperties() {
		if(daoProperties==null) {
			daoProperties = new Properties();
			try {
				daoProperties.load(PropertiesUtils.class
						.getClassLoader()
						.getResourceAsStream("org/itat/message/util/dao.properties"));
			} catch (IOException e) {
				e.printStackTrace();
			}
			return daoProperties;
		} else {
			return daoProperties;
		}
	}
}
