package com.goods.data;

import java.util.Date;

public class AccountData extends SimpleAccount {

	// Account information
	private String device = "";
	private String password = "";
	private String code = "";
	private String token = "";
	private Date signdate = new Date();
	
	// Profile
	private String sex = "";
	private String birthday = "";
	private String phone = "";
	private String address = "";
	private String photo = "";

	private String city = "";
	
	// Setting
	private String language = "LANG_CHS";
	
	public boolean isManager()
	{
		return (this.getType().equals(AccountType.SUPER_ADMIN.name()) ||
				this.getType().equals(AccountType.ADMIN.name()));
	}
	
	public String getDevice() {
		return device;
	}

	public void setDevice(String device) {
		this.device = device;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Date getSigndate() {
		return signdate;
	}

	public String getSex() {
		return sex;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public void setSigndate(Date signdate) {
		this.signdate = signdate;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}
}
