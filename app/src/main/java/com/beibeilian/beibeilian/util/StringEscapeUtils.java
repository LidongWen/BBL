package com.beibeilian.beibeilian.util;

public class StringEscapeUtils {

	public static String escapeSql(String str) {
		if (str == null) {
			return null;
		}
		return StringUtils.replace(str, "'", "''");
	}

}
