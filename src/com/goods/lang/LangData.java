package com.goods.lang;

import java.util.HashMap;
import java.util.Map;

public class LangData {

	private String mLangName = "";
	private Map<String, String> mMapLang = new HashMap<String, String>();
	
	public LangData(String name)
	{
		this.mLangName = name;
	}
	
	public String getLangName() {
		return this.mLangName;
	}
	
	public Map<String, String> getLangMap()
	{
		return this.mMapLang;
	}

	public void append(Map<String, String> langMap) {
		this.mMapLang.putAll(langMap);
	}
}
