package com.beibeilian.beibeilian.constant;

import com.beibeilian.beibeilian.util.HttpConstantUtil;

public class BBLConstant {

	public static final int MEMBER_STATE_NUMBER_OUT = 0;// 不是会员,数量已超,收费

	public static final int MEMBER_STATE_YES = 1;// 是会员且正常使用

	public static final int MEMBER_STATE_OUT = -1;// 过期

	public static final int MEMBER_STATE_NUMBER_NO_OUT = 2;// 未超过数量

	public static final int ANLIAN_STATE_YES = 1;// 暗恋成功

	public static final int ANLIAN_STATE_OUT = -1;// 已经暗恋了

	public static final int ANLIAN_STATE_NO = 0;// 暗恋失败

	public static final String ACTION_APP_START = "0";// APP启动

	public static final String ACTION_PAY_CONFIRM = "1";// 支付确认

	public static final String ACTION_PAY_CANCEL = "2";// 支付取消

	public static final String ACTION_PAY_FAILE = "3";// 支付失败

	public static final String ACTION_PAY_UNKNOW = "4";// 支付未知

	public static final String ACTION_PAY_SUCCESS = "5";// 支付成功

	public static final String ACTION_PAY_UNKNOW_NO = "6";// 支付未知NO

	/**
	 * 图片地址前缀
	 */
	public static final String PHOTO_BEFORE_URL = HttpConstantUtil.FILE_UPLOAD_PreUrl+"upload/";

	public static final String PANDASDK_ID="c246e69b730e43ec803dab213d30c8a5";
}
