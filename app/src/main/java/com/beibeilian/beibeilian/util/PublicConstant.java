package com.beibeilian.beibeilian.util;

import android.os.Environment;

public class PublicConstant {

	/**
	 * 连接域名
	 */
	public static final String XMPPDomain = "@wpy";

	public static final String GroupXMPPDomain = "@conference.wpy";

	/**
	 * 省
	 */
	public static final String MeDataDialogProvinceTag = "MeDataDialogProvinceTag";
	/**
	 * 市
	 */
	public static final String MeDataDialogCityTag = "MeDataDialogCityTag";

	/**
	 * 年龄
	 */
	public static final String MeDataDialogYearTag = "MeDataDialogYearTag";

	/**
	 * 条件年龄
	 */
	public static final String MeDataDialogConditionYearTag = "MeDataDialogConditionYearTag";

	/**
	 * 身高
	 */
	public static final String MeDataDialogHeightTag = "MeDataDialogHeightTag";

	/**
	 * 体重
	 */
	public static final String MeDataDialogWeightTag = "MeDataDialogWeightTag";

	/**
	 * 血型
	 */
	public static final String MeDataDialogBloodTag = "MeDataDialogBloodTag";

	/**
	 * 学历
	 */
	public static final String MeDataDialogEducationTag = "MeDataDialogEducationTag";

	/**
	 * 职业
	 */
	public static final String MeDataDialogJobTag = "MeDataDialogJobTag";

	/**
	 * 月薪
	 */
	public static final String MeDataDialogMonthlyTag = "MeDataDialogMonthlyTag";

	/**
	 * 房子
	 */
	public static final String MeDataDialogHouseTag = "MeDataDialogHouseTag";

	/**
	 * 喜欢的异性
	 */
	public static final String MeDataDialogLikeoppositesexTag = "MeDataDialogLikeoppositesexTag";

	/**
	 * 婚姻状态
	 */
	public static final String MeDataDialogMarriagestatusTag = "MeDataDialogMarriagestatusTag";

	/**
	 * 婚前性
	 */
	public static final String MeDataDialogMarriageSexTag = "MeDataDialogMarriageSexTag";

	/**
	 * 异地恋
	 */
	public static final String MeDataDialogPlaceotherloveTag = "MeDataDialogPlaceotherloveTag";

	/**
	 * 想要小孩
	 */
	public static final String MeDataDialogIswantchildTag = "MeDataDialogIswantchildTag";

	/**
	 * 和父母
	 */
	public static final String MeDataDialogANDFMOMTag = "MeDataDialogANDFMOMTag";

	/**
	 * 性别
	 */
	public static final String MeDataDialogSEXTag = "MeDataDialogSEXTag";

	/**
	 * dialog提交
	 */
	public static final String DialogSubmit = "正在请求服务器,请稍候...";

	/**
	 * toast服务器异常
	 */
	public static final String ToastCatch = "请检查网络是否可用或稍候再试";

	/**
	 * size>0
	 */
	public static final int JsonYesUI = 1;

	/**
	 * size==0
	 */
	public static final int JsonNoUI = 0;

	/**
	 * catch
	 */
	public static final int JsonCatch = -1;

	/**
	 * 刷新通知
	 */
	public static final int RefreashUI = 2;

	/**
	 * updateList
	 */
	public static final int UpdateList = 3;

	/**
	 * 分页数
	 */
	public static int PageSize = 15;

	/**
	 * 文件路径
	 */
	public static String FilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/beibeilian/image/";

	public static String VoiceFilePath = Environment.getExternalStorageDirectory().getAbsolutePath()
			+ "/beibeilian/voice/";

	/**
	 * apk
	 */
	public static String APKPATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/beibeilian/apk/";;

	/**
	 * crash
	 */
	public static String CRASHPATH = Environment.getExternalStorageDirectory().getAbsolutePath()
			+ "/beibeilian/crash/";;

	/**
	 * 服务类名
	 */
	public static String CodeServicePackName = "com.beibeilian.service.CoreIMService";

	/**
	 * 消息提醒
	 */
	public static final int MessagePend = 1;

	public static final int VisitPend = 2;

	public static final int NetPend = 3;

	public static final int BADPend = 4;

	public static final int PADPend = 5;

	public static final int VERSIONPend = 6;

	public static final int ZANPend = 7;

	public static final int COMMITPend = 8;

	public static final int ANLIANPend = 9;

	public static final int POINTSPend = 10;

	public static final int STARTCOMMANDSERVICCEPEND = 11;

	public static final String VISITTYPE = "0";

	public static final String TIETYPE = "1";

	public static final String ANLIANTYPE = "2";


	public static final String GROUPTYPE = "3";

	public static final String MYQAQTYPE = "4";

	public static final int GroupMessagePend = 11;
}
