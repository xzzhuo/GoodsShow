package com.goods.process;

import java.io.File;
import java.util.List;
import java.util.Map;

import com.goods.application.GoodsConfig;
import com.goods.lang.LangData;
import com.goods.lang.LangManage;
import com.goods.utils.MyWebUtil;

import exhi.net.log.NetLog;
import exhi.net.netty.NetProcess;

public abstract class BaseProcess extends NetProcess {

	protected MyWebUtil mWebUtil = null;
	private Map<String, String> mCurLangMap = null;
	
	private static LangManage mLangData = new LangManage();
	
	protected abstract boolean doProcess(String client, String path, File templateFile, String act, Map<String, String> request) throws ShowErrorException;
	
	public BaseProcess()
	{
		mLangData.loadStockLanguage();
		mWebUtil = new MyWebUtil(this);
	}
	
	protected void setCurrentLanguage(String name)
	{
		// Choice system current language
		LangData curLangData = mLangData.getLang(name);
		if (curLangData == null)
		{
			curLangData = mLangData.getDefaultLang();
		}
		
		mCurLangMap = curLangData.getLangMap();
		
		mWebUtil.assign("language", name);
		mWebUtil.assign("LANG", mCurLangMap);
	}

	protected void initProcess(String address, String path, Map<String, String> request) throws ShowErrorException {

		NetLog.debug(address, "Enter BaseProcess - initProcess()");
		NetLog.debug(address, path);

		this.setCurrentLanguage(GoodsConfig.instance().getLangTypeName());
		File tempFile = new File(path);
		
		NetLog.debug(address, "Parent path = " + tempFile.getParent());
		mWebUtil.setTemplatePath(tempFile.getParent());

		mWebUtil.assign("title", mCurLangMap.get("lang_title"));
		
		NetLog.debug(address, "Leave BaseProcess - initProcess()");
	}

	public static boolean checkRequestMap(Map<String, String> request, List<String> keys)
	{
		boolean verify = true;
		
		for (String key : keys)
		{
			if (!request.containsKey(key))
			{
				verify = false;
				break;
			}
		}
		
		return verify;
	}
	
	public static CheckRequestResult checkRequestMap(ICheckRequest callBack, BaseProcess process, Map<String, String> request, String client)
	{
		return callBack.checkRequest(process, request, client);
	}
	
	protected final Map<String, String> getCurLangMap()
	{
		return this.mCurLangMap;
	}
}
