package com.goods.lang;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class LangManage {

	private Map<String, LangData> mLangMap = new HashMap<String, LangData>();
	
	private final LangType mDefaultLangType = LangType.LANG_CHS;

	public LangManage()
	{
		
	}
	
	public List<String> getLangNames()
	{
		List<String> listKey = new ArrayList<String>();
        Iterator<String> it = mLangMap.keySet().iterator();
        while (it.hasNext()) {
            String key = it.next().toString();
            listKey.add(key);
        }
        
        return listKey;
	}

	public LangData getLang(String name)
	{
		LangData langData = null;
		if (mLangMap.containsKey(name))
		{
			langData = mLangMap.get(name);
		}
		
		return langData;
	}
	
	public boolean putLang(String name, LangData data)
	{
		if (this.mLangMap.containsKey(name))
		{
			mLangMap.get(name).append(data.getLangMap());
		}
		else
		{
			this.mLangMap.put(name, data);
		}

		return true;
	}
	
	public LangData getDefaultLang()
	{
		return this.getLang(mDefaultLangType.name());
	}

	public void loadStockLanguage()
	{
		Collection<LangData> mapLandData = StockLang.getInstance().getLangList();
		for (LangData data : mapLandData)
		{
			this.putLang(data.getLangName(), data);
		}
	}

}
