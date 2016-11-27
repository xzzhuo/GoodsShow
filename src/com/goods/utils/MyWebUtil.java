package com.goods.utils;

import exhi.net.netty.NetProcess;
import exhi.net.netty.WebUtil;

public class MyWebUtil extends WebUtil {

	public MyWebUtil(NetProcess process) {
		super(process);
	}
	
	@Override
	public void assign(String key, Object value)
	{
		super.assign(key, MyUtils.html_escape_string(value));
	}

	public void assign(String key, Object value, boolean escape)
	{
		if (escape)
		{
			this.assign(key, value);
		}
		else
		{
			super.assign(key, value);
		}
	}
}
