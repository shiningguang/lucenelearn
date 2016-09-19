package org.itat.lucene.util;

public interface FilterAccessor {

	public String[] values();
	
	public String getField();
	
	public boolean set();
}
