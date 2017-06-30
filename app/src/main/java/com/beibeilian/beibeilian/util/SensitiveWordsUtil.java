package com.beibeilian.beibeilian.util;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class SensitiveWordsUtil {

	public static boolean sensitiveWords(String words) {
		if (words.contains("qq") || words.contains("QQ")
				|| words.contains("扣扣") || words.contains("微信")
				|| words.contains("weixin") || words.contains("手机号")
				|| words.contains("联系方式") || words.contains("陌陌")
				|| words.contains("支付宝") || words.contains("账号")
				|| words.contains("银行卡") || words.contains("卡号")
				|| words.contains("号")) {
			return true;
		}
		if (hasDigit(words)) {
			return true;
		}
		if (judgeContainsStr(words)) {
			return true;
		}
		return false;
	}

	public static boolean hasDigit(String content) {
		boolean flag = false;
		Pattern p = Pattern.compile(".*\\d+.*");
		Matcher m = p.matcher(content);
		if (m.matches())
			flag = true;
		return flag;
	}

	public static boolean judgeContainsStr(String cardNum) {
		String regex = ".*[a-zA-Z]+.*";
		Matcher m = Pattern.compile(regex).matcher(cardNum);
		return m.matches();
	}
}
