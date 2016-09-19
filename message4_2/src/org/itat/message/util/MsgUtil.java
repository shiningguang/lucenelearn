package org.itat.message.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MsgUtil {
	public static String  formatDate(String pattern,Date date) {
		String str = null;
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);//MM/dd hh:ss
		str = sdf.format(date);
		return str;
	}
}
