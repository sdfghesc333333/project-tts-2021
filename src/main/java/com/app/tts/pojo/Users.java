package com.app.tts.pojo;
// Generated Sep 25, 2020 2:22:03 PM by Hibernate Tools 4.3.1

import java.util.Map;

import javax.persistence.Entity;

import com.app.tts.util.AppParams;
import com.app.tts.util.ParamUtil;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Users {

	private String id;
	private String email;
	private String password;
	private String avatar;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public static Users fromMap(Map input) {
		Users obj = new Users();
		obj.setId(ParamUtil.getString(input, AppParams.S_ID));
		obj.setPassword(ParamUtil.getString(input, AppParams.S_PASSWORD));
		obj.setEmail(ParamUtil.getString(input, AppParams.S_EMAIL));
		obj.setAvatar(ParamUtil.getString(input, AppParams.S_AVATAR));
		return obj;
	}

	@Override
	public String toString() {
		return "UserObj [id = " + id + ",email = " + email + ",avatar = " + avatar + "]";
	}

}
