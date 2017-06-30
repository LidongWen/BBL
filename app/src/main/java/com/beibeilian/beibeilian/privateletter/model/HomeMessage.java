package com.beibeilian.beibeilian.privateletter.model;

public class HomeMessage {

	public String id;    //记录id

	public String imid;  //云数据库id

	public String from;  //谁发的

	public String outuser;

	public String touser;

	public String getOutuser() {
		return outuser;
	}

	public void setOutuser(String outuser) {
		this.outuser = outuser;
	}

	public String getTouser() {
		return touser;
	}

	public void setTouser(String touser) {
		this.touser = touser;
	}

	public String content; //内容

	public String seestate; //查看状态

	public String sendstate; //发送状态

	public String time;      //聊天时间




	public String getImid() {
		return imid;
	}

	public void setImid(String imid) {
		this.imid = imid;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getSeestate() {
		return seestate;
	}

	public void setSeestate(String seestate) {
		this.seestate = seestate;
	}

	public String getSendstate() {
		return sendstate;
	}

	public void setSendstate(String sendstate) {
		this.sendstate = sendstate;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}



}
