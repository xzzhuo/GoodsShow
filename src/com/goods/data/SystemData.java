package com.goods.data;

public class SystemData extends BaseData {

	private int id = 0;
	private String title = "";
	private int appver = 1;
	private int dbver = 1;
	private String dbengine = "";
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getAppver() {
		return appver;
	}

	public void setAppver(int appver) {
		this.appver = appver;
	}

	public int getDbver() {
		return dbver;
	}

	public void setDbver(int dbver) {
		this.dbver = dbver;
	}

	public String getDbengine() {
		return dbengine;
	}

	public void setDbengine(String dbengine) {
		this.dbengine = dbengine;
	}
}
