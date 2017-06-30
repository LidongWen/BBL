package com.beibeilian.beibeilian.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import com.beibeilian.beibeilian.constant.BBLConstant;
import com.beibeilian.beibeilian.me.model.Remind;
import com.beibeilian.beibeilian.me.model.UserInfoEntiy;
import com.beibeilian.beibeilian.me.model.Version;
import com.beibeilian.beibeilian.model.PayRule;
import com.beibeilian.beibeilian.model.VIPOrder;
import com.beibeilian.beibeilian.privateletter.model.HomeMessage;
import com.beibeilian.beibeilian.privateletter.model.MessageList;
import com.beibeilian.beibeilian.seek.model.UserInfo;
import com.beibeilian.beibeilian.util.HelperUtil;
import com.beibeilian.beibeilian.util.MessageConstantUtil;
import com.beibeilian.beibeilian.util.PublicConstant;
import com.beibeilian.beibeilian.util.StringEscapeUtils;

import java.util.ArrayList;
import java.util.List;

public class BBLDao extends SQLiteOpenHelper {
	private static String NAME = "bbl_app_2017.db";
	private static int VERSION = 1;
	private SQLiteDatabase db;

	public BBLDao(Context context, String name, CursorFactory factory, int version) {
		super(context, NAME, factory, VERSION);
	}

	private String userinfosql = "create table if not exists userinfo(id integer primary key autoincrement,nickname varchar(50),username varchar(50),password varchar(50),time varchar(20),state varchar(2),level varchar(20),sex varchar(5))";
	private String imsql = "create table if not exists privateletter(id integer primary key autoincrement,imid varchar(50),userout varchar(50),userin varchar(50),content varchar(200),seestate integer default 0,sendstate integer default 0,time varchar(50),delstate integer default 0)";
	private String userphotosql = "create table if not exists userphoto(id integer primary key autoincrement,userin varchar(50),photourl varchar(100),nickname varchar(50),sex varchar(5))";
	private String version_sql = "create table if not exists version(id integer primary key autoincrement,code varchar(15),size varchar(10),url varchar(200),content varchar(100))";
	private String remind_sql = "create table if not exists remind(id integer primary key autoincrement,voice varchar(10),zhendong varchar(10),username varchar(50))";
	// private String order_sql = "create table if not exists viporder(id
	// integer primary key autoincrement,username varchar(50),membertype integer
	// default 0,stime varchar(20),etime varchar(20),orderno
	// varchar(50),cversion varchar(10),payway integer default 0)";
	private String groupsql = "create table if not exists imgroup(id integer primary key autoincrement,imid varchar(50),userout varchar(50),content varchar(200),seestate integer default 0,sendstate integer default 0,time varchar(50),delstate integer default 0,grouptype varchar(50))";
	private String mygroupsql = "create table if not exists mygroup(id integer primary key autoincrement,username varchar(50),grouptype varchar(50))";
	private String paysql = "create table if not exists price_power(id integer primary key autoincrement,visit varchar(2),anlian varchar(2),relation varchar(2),gift varchar(2),marriage varchar(2),ball varchar(2),chat varchar(2),price_15 varchar(10),price_30 varchar(10),price_180 varchar(10),price_360 varchar(10),modtime varchar(20))";

	private String vipmembersql = "create table if not exists vipmember(id integer primary key autoincrement,username varchar(50))";

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(userinfosql);
		db.execSQL(imsql);
		db.execSQL(userphotosql);
		db.execSQL(version_sql);
		db.execSQL(remind_sql);
		// db.execSQL(order_sql);
		db.execSQL(groupsql);
		db.execSQL(mygroupsql);
		db.execSQL(paysql);
		db.execSQL(vipmembersql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if (oldVersion != newVersion) {
			db.execSQL("drop table if exists userinfo");
			db.execSQL("drop table if exists privateletter");
			db.execSQL("drop table if exists userphoto");
			db.execSQL("drop table if exists version");
			db.execSQL("drop table if exists remind");
			db.execSQL("drop table if exists imgroup");
			db.execSQL("drop table if exists mygroup");
			db.execSQL("drop table if exists price_power");
			db.execSQL("drop table if exists vipmember");
			db.execSQL("drop table if exists viporder");
			onCreate(db);
		}
	}

	public void insertVipMember(String username) {
		db = this.getWritableDatabase();
		db.execSQL("delete from vipmember");
		db.execSQL("insert into vipmember(username) values('" + username + "')");
	}

	public void deleteVipMember() {
		db = this.getWritableDatabase();
		db.execSQL("delete from vipmember");
	}

	public int findVipMember(String username) {
		db = this.getReadableDatabase();
		Cursor mCursor = db.rawQuery("select * from vipmember where username='" + username + "'", null);
		int result = mCursor.getCount();
		mCursor.close();
		return result;
	}

	public PayRule findPayRule() {
		db = this.getReadableDatabase();
		Cursor mCursor = db.rawQuery(
				"select visit,anlian,relation,gift,marriage,ball,chat,price_15,price_30,price_180,price_360 from price_power order by id limit 1",
				null);
		PayRule model = new PayRule();
		while (mCursor.moveToNext()) {
			model.setVisit(mCursor.getString(0));
			model.setAnlian(mCursor.getString(1));
			model.setRelation(mCursor.getString(2));
			model.setGift(mCursor.getString(3));
			model.setMarriage(mCursor.getString(4));
			model.setBall(mCursor.getString(5));
			model.setChat(mCursor.getString(6));
			model.setPrice_15(mCursor.getString(7));
			model.setPrice_30(mCursor.getString(8));
			model.setPrice_180(mCursor.getString(9));
			model.setPrice_360(mCursor.getString(10));
		}
		mCursor.close();
		return model;
	}

	public void insertPayRule(PayRule model) {
		db = this.getWritableDatabase();
		db.execSQL(
				"insert into price_power(visit,anlian,relation,gift,marriage,ball,chat,price_15,price_30,price_180,price_360)values('"
						+ model.getVisit() + "','" + model.getAnlian() + "','" + model.getRelation() + "','"
						+ model.getGift() + "','" + model.getMarriage() + "','" + model.getBall() + "','"
						+ model.getChat() + "','" + model.getPrice_15() + "','" + model.getPrice_30() + "','"
						+ model.getPrice_180() + "','" + model.getPrice_360() + "')");
	}

	public void deletePayRule() {
		db = this.getWritableDatabase();
		db.execSQL("delete from price_power");
	}

	public void insertMygroup(String username, String grouptype) {
		db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(
				"select grouptype from mygroup where username='" + username + "' and grouptype='" + grouptype + "'",
				null);
		if (cursor.getCount() <= 0) {
			db = this.getWritableDatabase();
			db.execSQL("insert into mygroup(username,grouptype)values('" + username + "','" + grouptype + "')");
		}
		cursor.close();
	}

	public List<String> findmygroup(String username) {
		db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("select grouptype from mygroup where username='" + username + "'", null);
		List<String> list = new ArrayList<String>();
		while (cursor.moveToNext()) {
			list.add(cursor.getString(0));
		}
		cursor.close();
		return list;
	}

	public void insertOrder(String username, int membertype, String stime, String etime, String orderno,
							String cversion, int payway) {
		db = this.getWritableDatabase();
		db.execSQL("insert into viporder(username,membertype,stime,etime,orderno,cversion,payway)values('" + username
				+ "','" + membertype + "','" + stime + "','" + etime + "','" + orderno + "','" + cversion + "','"
				+ payway + "')");
	}

	public List<VIPOrder> findOrder(String username) {
		List<VIPOrder> list = new ArrayList<VIPOrder>();
		db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(
				"select membertype,stime,etime,orderno,cversion,payway from viporder where username='" + username + "'",
				null);
		VIPOrder model = null;
		while (cursor.moveToNext()) {
			model = new VIPOrder();
			model.setMembertype(cursor.getString(0));
			model.setStime(cursor.getString(1));
			model.setEtime(cursor.getString(2));
			model.setOrderno(cursor.getString(3));
			model.setCversion(cursor.getString(4));
			model.setPayway(cursor.getString(5));
			list.add(model);
			model = null;
		}
		cursor.close();
		return list;
	}

	public void delOrder(String username) {
		db = this.getWritableDatabase();
		db.execSQL("delete from viporder where username='" + username + "'");
	}

	public void updateRemind(Remind remind, String username) {
		db = this.getWritableDatabase();
		db.execSQL("update remind set voice='" + remind.getVoice() + "',zhendong='" + remind.getZhendong()
				+ "' where username='" + username + "'");
		db.close();
	}

	public Remind queryRemind(String username) {
		Remind model = new Remind();
		try {
			String sql = "select voice,zhendong from remind where username='" + username + "'";
			db = this.getReadableDatabase();
			Cursor c = db.rawQuery(sql, null);
			while (c.moveToNext()) {
				model.setVoice(c.getString(0));
				model.setZhendong(c.getString(1));
			}
			c.close();
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return model;
	}

	public Version queryVersion() {
		Version v = new Version();
		try {
			String sql = "select code,size,url,content from version";
			db = this.getReadableDatabase();
			Cursor c = db.rawQuery(sql, null);
			while (c.moveToNext()) {
				v.setCode(c.getString(0));
				v.setSize(c.getString(1));
				v.setUrl(c.getString(2));
				v.setContent(c.getString(3));
			}
			c.close();
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return v;
	}

	public void updateVersion(Version version) {
		String updatesql = "update version set code=?,size=?,url=?,content=?";
		String insertsql = "insert into version(code,size,url,content) values(?,?,?,?)";
		String selectsql = "select code from version";
		db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectsql, null);
		if (cursor.getCount() > 0) {

			db = this.getWritableDatabase();
			db.execSQL(updatesql,
					new Object[] { version.getCode(), version.getSize(), version.getUrl(), version.getContent() });

		} else {
			db = this.getWritableDatabase();
			db.execSQL(insertsql,
					new Object[] { version.getCode(), version.getSize(), version.getUrl(), version.getContent() });
		}
		cursor.close();
		db.close();
	}

	/**
	 * 我的头像和对方的头像
	 *
	 * @param user
	 * @param photo
	 */
	public void updatePhoto(String user, String photo, String nickname, String sex) {
		String selectsql = "select photourl,nickname from userphoto where userin='" + user + "'";
		String insertsql = "insert into userphoto(userin,photourl,nickname,sex)values('" + user + "','" + photo + "','"
				+ StringEscapeUtils.escapeSql(nickname) + "','" + sex + "') ";
		String updatesql = "update userphoto set photourl='" + photo + "',nickname='"
				+ StringEscapeUtils.escapeSql(nickname) + "' where userin='" + user + "'";
		db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectsql, null);
		if (cursor.getCount() > 0) {
			db = this.getWritableDatabase();
			db.execSQL(updatesql);
		} else {
			db = this.getWritableDatabase();
			db.execSQL(insertsql);
		}
		cursor.close();
		db.close();
	}

	/**
	 * 查询头像
	 *
	 * @param user
	 * @return
	 */
	public UserInfo queryPhoto(String user) {
		String selectsql = "select photourl,nickname,sex from userphoto where userin='" + user + "'";
		db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectsql, null);
		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				UserInfo model = new UserInfo();
				String photo = cursor.getString(0);
				if (HelperUtil.flagISNoNull(photo)) {
					if (photo.contains("http")) {
						photo = photo.substring(photo.lastIndexOf("/") + 1);
					}
					model.setPhoto(BBLConstant.PHOTO_BEFORE_URL + photo);
				}
				model.setNickname(cursor.getString(1));
				model.setSex(cursor.getString(2));
				return model;
			}
		}
		cursor.close();
		db.close();
		return null;
	}

	/**
	 * 保存用户登录信息
	 *
	 * @param
	 */
	public void initUser(String username, String nickname, String password, String level, String sex) {
		try {
			String insertsql = "insert into userinfo(username,nickname,password,state,time,level,sex) values('"
					+ username + "','" + StringEscapeUtils.escapeSql(nickname) + "','"
					+ StringEscapeUtils.escapeSql(password) + "','1','" + HelperUtil.DateTime() + "','" + level + "','"
					+ sex + "')";
			String selectsql = "select username from userinfo where username=?";
			String updatesql = "update userinfo set state='1',time='" + HelperUtil.DateTime() + "',password='"
					+ StringEscapeUtils.escapeSql(password) + "',level='" + level + "',sex='" + sex + "',nickname='"
					+ StringEscapeUtils.escapeSql(nickname) + "' where username='" + username + "'";
			db = this.getReadableDatabase();
			Cursor cursor = db.rawQuery(selectsql, new String[] { username });
			if (cursor.getCount() > 0) {
				db = this.getWritableDatabase();
				db.execSQL(updatesql);
			} else {
				db = this.getWritableDatabase();
				db.execSQL(insertsql);
				db.execSQL("insert into remind(voice,zhendong,username)values('1','1','" + username + "')");
			}
			db.execSQL("delete from privateletter where userout!='" + username + "' and userin!='" + username + "'");
			cursor.close();
		} catch (Exception e) {
			e.printStackTrace();
			// System.out.println("登录报错" + e);
		}
	}

	public void delUserinfo() {
		db = this.getWritableDatabase();
		db.execSQL("delete from userinfo");
		db.close();
	}

	public void updateNickname(String username, String nickname) {
		String updatesql = "update userinfo set nickname='" + StringEscapeUtils.escapeSql(nickname)
				+ "' where username='" + username + "'";
		db = this.getWritableDatabase();
		db.execSQL(updatesql);
		db.close();
	}

	public void updateLoginState(String username) {
		String updatesql = "update userinfo set state='0' where username='" + username + "'";
		db = this.getWritableDatabase();
		db.execSQL(updatesql);
		db.close();
	}

	public UserInfoEntiy queryUserByNewTime() {
		try {

			UserInfoEntiy user = new UserInfoEntiy();
			String sql = "select username,password,state,level,sex,nickname from userinfo  order by time desc limit 1";
			db = this.getReadableDatabase();
			Cursor cursor = db.rawQuery(sql, null);
			if (cursor.moveToFirst()) {
				user.setUsername(cursor.getString(0));
				user.setPassword(cursor.getString(1));
				user.setState(cursor.getString(2));
				user.setLevel(cursor.getString(3));
				user.setSex(cursor.getString(4));
				user.setNickname(cursor.getString(5));
			}
			cursor.close();
			db.close();
			return user;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void updateRefreashImGroup(String userout, String content, String time, String sendstate, String imid,
									  String seestate, String grouptype) {
		String selectsql = "select content from imgroup where imid='" + imid + "' and grouptype='" + grouptype + "'";
		db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectsql, null);
		if (cursor.getCount() == 0) {
			String insertsql = "insert into imgroup(grouptype,userout,content,time,sendstate,imid,seestate)values('"
					+ grouptype + "','" + userout + "','" + StringEscapeUtils.escapeSql(content) + "','" + time + "','"
					+ sendstate + "','" + imid + "','" + seestate + "')";
			db = this.getWritableDatabase();
			db.execSQL(insertsql);
		}
		cursor.close();
		// db.close();
	}

	public void updateRefreashIm(String userout, String userin, String content, String time, String sendstate,
								 String imid, String seestate) {
		String selectsql = "select content from privateletter where imid='" + imid + "'";
		db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectsql, null);
		if (cursor.getCount() == 0) {
			String insertsql = "insert into privateletter(userout,userin,content,time,sendstate,imid,seestate)values('"
					+ userout + "','" + userin + "','" + StringEscapeUtils.escapeSql(content) + "','" + time + "','"
					+ sendstate + "','" + imid + "','" + seestate + "')";
			db = this.getWritableDatabase();
			db.execSQL(insertsql);
		}
		cursor.close();
		// db.close();
	}

	public void insertIm(String userout, String userin, String content, String time, String sendstate, String id,
						 String seestate) {
		String insertsql = "insert into privateletter(userout,userin,content,time,sendstate,imid,seestate)values('"
				+ userout + "','" + userin + "','" + StringEscapeUtils.escapeSql(content) + "','" + time + "','"
				+ sendstate + "','" + id + "','" + seestate + "')";
		db = this.getWritableDatabase();
		db.execSQL(insertsql);
	}

	public void insertImGroup(String userout, String grouptype, String content, String time, String sendstate,
							  String id, String seestate) {
		String insertsql = "insert into imgroup(userout,grouptype,content,time,sendstate,imid,seestate)values('"
				+ userout + "','" + grouptype + "','" + StringEscapeUtils.escapeSql(content) + "','" + time + "','"
				+ sendstate + "','" + id + "','" + seestate + "')";
		db = this.getWritableDatabase();
		db.execSQL(insertsql);
	}

	public void updateImGroup(String id, String sendstate, String grouptype) {
		String updatesql = "update imgroup set sendstate='" + sendstate + "' where imid='" + id + "' and grouptype='"
				+ grouptype + "'";
		db = this.getWritableDatabase();
		db.execSQL(updatesql);
	}

	public void updateIm(String id, String sendstate) {
		String updatesql = "update privateletter set sendstate='" + sendstate + "' where imid='" + id + "'";
		db = this.getWritableDatabase();
		db.execSQL(updatesql);
	}

	public void updateImSeeState(String userin) {
		try {
			String updatesql = "update privateletter set seestate='" + MessageConstantUtil.SEE_STATE_YES
					+ "'  where userin='" + userin + "' or userout='" + userin + "'";
			db = this.getWritableDatabase();
			db.execSQL(updatesql);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void updateImSeeStateGroup(String grouptype) {
		try {
			String updatesql = "update imgroup set seestate='" + MessageConstantUtil.SEE_STATE_YES
					+ "'  where grouptype='" + grouptype + "'";
			db = this.getWritableDatabase();
			db.execSQL(updatesql);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public int queryCountByUser(String userin) {
		String sql = "select id  from privateletter where seestate='" + MessageConstantUtil.SEE_STATE_NO
				+ "' and (userin='" + userin + "' or userout='" + userin + "')";
		db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(sql, null);
		int count = cursor.getCount();
		cursor.close();
		return count;
	}

	public int queryCountByUserGroup(String grouptype) {
		String sql = "select id  from imgroup where seestate='" + MessageConstantUtil.SEE_STATE_NO + "' and grouptype='"
				+ grouptype + "'";
		db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(sql, null);
		int count = cursor.getCount();
		cursor.close();
		return count;
	}

	public int queryCountByUserNewState(String userin) {
		String sql = "select id  from privateletter where seestate='" + MessageConstantUtil.SEE_STATE_NO
				+ "' and delstate='0'  and (userin='" + userin + "' or userout='" + userin + "')";
		db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(sql, null);
		int count = cursor.getCount();
		cursor.close();
		return count;
	}

	public int queryCountByUserNewStateGroup(String grouptype) {
		String sql = "select id  from imgroup where seestate='" + MessageConstantUtil.SEE_STATE_NO
				+ "' and delstate='0'  and grouptype='" + grouptype + "'";
		db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(sql, null);
		int count = cursor.getCount();
		cursor.close();
		return count;
	}

	public void delIm(String userin) {
		String delsql = "update  privateletter set delstate='1' where userin='" + userin + "' or userout='" + userin
				+ "'";
		db = this.getWritableDatabase();
		db.execSQL(delsql);
	}

	public void delIMOne(String imid) {
		String delsql = "delete from privateletter where imid='" + imid + "'";
		db = this.getWritableDatabase();
		db.execSQL(delsql);
	}

	public void delIMOneGroup(String imid, String grouptype) {
		String delsql = "delete from imgroup where imid='" + imid + "' and grouptype='" + grouptype + "'";
		db = this.getWritableDatabase();
		db.execSQL(delsql);
	}

	public List<MessageList> queryMessageList(String pagenumber, String username) {
		if (pagenumber.equals("0")) {
		} else {
			pagenumber = String.valueOf(Integer.parseInt(pagenumber) *PublicConstant.PageSize);
		}
		List<MessageList> list = new ArrayList<MessageList>();
		String sql = "select userout,userin,content,time from privateletter W where time=(select MAX(time) from privateletter where userout=W.userout and userin=W.userin or userout=w.userin and userin=W.userout) and delstate='0' order by time desc  limit "
				+ pagenumber + "," +PublicConstant.PageSize + "";
		// String sqlnew="select userout,userin,content,time from privateletter
		// where userout in( select userout from privateletter where userout =
		// '"+username+"' or userin = '"+username+"' group by userout) and time
		// in (select max(time) from privateletter where userout =
		// '"+username+"' or userin = '"+username+"' group by
		// privateletter)order by time desc";
		// String sql="select userout,userin,content,time from privateletter
		// where userout in( select userout from privateletter where userout =
		// '220' or userin = '220' group by userout) and time in (select
		// max(time) from privateletter where userout = '220' or userin = '220'
		// group by userout)order by time desc";
		db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(sql, null);
		while (cursor.moveToNext()) {
			MessageList messageList = new MessageList();
			messageList.setOutuser(cursor.getString(0));
			messageList.setFromuser(cursor.getString(1));
			messageList.setFromcontent(cursor.getString(2));
			messageList.setFromtime(cursor.getString(3));
			list.add(messageList);
		}

		cursor.close();
		return list;
	}

	public List<HomeMessage> queryChatMessageList(String fromuser, String touser, String pagenumber) {
		try {
			if (pagenumber.equals("0")) {
			} else {
				pagenumber = String.valueOf(Integer.parseInt(pagenumber) *PublicConstant.PageSize);
			}
			List<HomeMessage> list = new ArrayList<HomeMessage>();
			String sql = "select userout,content,time,sendstate,imid from privateletter where (userout='" + fromuser
					+ "' and userin='" + touser + "') or (userout='" + touser + "' and userin='" + fromuser
					+ "') order by time desc limit " + pagenumber + "," +PublicConstant.PageSize
					+ "";
			db = this.getReadableDatabase();
			Cursor cursor = db.rawQuery(sql, null);
			// System.out.println(cursor.getCount()+"查询");
			while (cursor.moveToNext()) {
				HomeMessage model = new HomeMessage();
				String outuser = cursor.getString(0);
				if (outuser.equals(fromuser))
					model.setFrom("OUT");
				else
					model.setFrom("IN");
				model.setContent(cursor.getString(1));
				model.setTime(cursor.getString(2));
				model.setSendstate(cursor.getString(3));
				model.setImid(cursor.getString(4));
				list.add(model);
			}
			cursor.close();
			return list;
		} catch (Exception e) {
		}
		return null;
	}

	public boolean queryChatISSend(String fromuser, String touser) {
		try {
			List<HomeMessage> list = new ArrayList<HomeMessage>();
			String sql = "select userout,content,time,sendstate,imid from privateletter where (userout='" + fromuser
					+ "' and userin='" + touser + "') or (userout='" + touser + "' and userin='" + fromuser
					+ "') order by time desc limit 0,10";
			db = this.getReadableDatabase();
			Cursor cursor = db.rawQuery(sql, null);
			while (cursor.moveToNext()) {
				HomeMessage model = new HomeMessage();
				String outuser = cursor.getString(0);
				if (outuser.equals(fromuser)) {
					model.setFrom("OUT");
				} else {
					model.setFrom("IN");
					return true;
				}
				model.setContent(cursor.getString(1));
				model.setTime(cursor.getString(2));
				model.setSendstate(cursor.getString(3));
				model.setImid(cursor.getString(4));
				list.add(model);
			}
			cursor.close();
		} catch (Exception e) {
		}
		return false;
	}

	public List<HomeMessage> queryChatMessageListGroup(String username, String grouptype, String pagenumber) {
		try {
			if (pagenumber.equals("0")) {
			} else {
				pagenumber = String.valueOf(Integer.parseInt(pagenumber) *PublicConstant.PageSize);
			}
			List<HomeMessage> list = new ArrayList<HomeMessage>();
			String sql = "select userout,content,time,sendstate,imid from imgroup where grouptype='" + grouptype
					+ "' order by time desc limit " + pagenumber + "," +PublicConstant.PageSize
					+ "";
			db = this.getReadableDatabase();
			Cursor cursor = db.rawQuery(sql, null);
			// System.out.println(cursor.getCount()+"查询");
			while (cursor.moveToNext()) {
				HomeMessage model = new HomeMessage();
				String outuser = cursor.getString(0);
				if (outuser.equals(username))
					model.setFrom("OUT");
				else
					model.setFrom("IN");
				model.setOutuser(outuser);
				;
				model.setContent(cursor.getString(1));
				model.setTime(cursor.getString(2));
				model.setSendstate(cursor.getString(3));
				model.setImid(cursor.getString(4));
				list.add(model);
			}
			cursor.close();
			return list;
		} catch (Exception e) {
		}
		return null;
	}

	public void delImNoMine(String userin) {
		String delsql = "delete from privateletter where userin!='" + userin + "' and userout!='" + userin + "'";
		db = this.getWritableDatabase();
		db.execSQL(delsql);
	}

	public int queryMessageCount(String username) {
		String sql = "select userout,userin from privateletter where userout='" + username + "' or userin='" + username
				+ "' limit 1";
		db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(sql, null);
		int count = cursor.getCount();
		cursor.close();
		return count;
	}

}
