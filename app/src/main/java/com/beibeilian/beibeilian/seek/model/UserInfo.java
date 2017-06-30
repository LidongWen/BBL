package com.beibeilian.beibeilian.seek.model;


public class UserInfo {
	public String username;
	public String birthday;
	public String photo;
	public String heartdubai;
	public String lives;
	public String nickname;
	public String state;
	public String time;
	public String distance;
	public int heartduibaistate;
	public String maritalstatus;
	public String auth;


	public String getAuth() {
		return auth;
	}
	public void setAuth(String auth) {
		this.auth = auth;
	}
	public String getMaritalstatus() {
		return maritalstatus;
	}
	public void setMaritalstatus(String maritalstatus) {
		this.maritalstatus = maritalstatus;
	}
	public int getHeartduibaistate() {
		return heartduibaistate;
	}
	public void setHeartduibaistate(int heartduibaistate) {
		this.heartduibaistate = heartduibaistate;
	}
	public String sex;
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getDistance() {
		return distance;
	}
	public void setDistance(String distance) {
		this.distance = distance;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getHeartdubai() {
		return heartdubai;
	}
	public void setHeartdubai(String heartdubai) {
		this.heartdubai = heartdubai;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public String getLives() {
		return lives;
	}
	public void setLives(String lives) {
		this.lives = lives;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	@Override
	public boolean equals(Object obj) {

		// TODO Auto-generated method stub
		//如果是自己
		if(this==obj){
			return true ;

		}
		//如果是空
		if(obj==null ){
			return false;
		}
		//比较两个People的名字是否相同
		if(obj!=null && obj instanceof UserInfo){
			if((((UserInfo)obj).username.equals(this.username)))
				return  true ;

		}
		return false;

	}
	@Override
	public int hashCode() {

		return username.hashCode();

	}
}
