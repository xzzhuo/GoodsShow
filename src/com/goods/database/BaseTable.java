package com.goods.database;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import com.goods.utils.MyUtils;

import exhi.net.database.DatabaseParam;
import exhi.net.database.NetTable;
import exhi.net.utils.DbUtils;

public abstract class BaseTable extends NetTable {

	public BaseTable(DatabaseParam param, String name, int oldVersion, int version) {
		super(param, name, oldVersion, version);
	}

	@Override
	public void onAfterCreateTable(boolean isCreate, int upgrade) {

	}

	@Override
	public void onBeforeCreateTable() {

	}

	public long getCount()
	{
		long count  = 0;
		
		String sql = String.format("SELECT COUNT(*) FROM %s", this.getTableName());
		List<Map<String, Object>> listMap = this.query(sql);
		
		if (listMap != null && listMap.size() == 1)
		{
			count = Long.parseLong(listMap.get(0).get("COUNT(*)").toString());
		}
		
		return count;
	}
	
	/**
	 * Insert new data and return current ID
	 * @param map insert data
	 * @param keyPrimary Primary key field (INT), eg: id
	 * @return return ID
	 */
	public long insert(Map<String, Object> map, String keyPrimary)
	{
		long index = -1;
		
		map = MyUtils.mysql_escape_string(map);
		String sql = DbUtils.getInsertSql(this.getTableName(), map);
		if (this.insert(sql))
		{
			sql = String.format("SELECT id FROM %s ORDER BY id DESC LIMIT 1", this.getTableName());
			List<Map<String, Object>> listMap = this.query(sql);
			
			if (listMap != null && listMap.size() == 1)
			{
				index = Long.parseLong(listMap.get(0).get(keyPrimary).toString());
			}
		}
		
		return index;
	}
	
	@Override
	public List<Map<String, Object>> query(String sql)
	{
		List<Map<String, Object>> list = super.query(sql);
		if (list == null)
		{
			return null;
		}

		return list;
	}

	protected String encode(String value)
	{
		try {
			value = java.net.URLEncoder.encode(value, "utf-8");
		} catch (UnsupportedEncodingException e) {
			value = "";
		}
		
		return value;
	}
	
	protected String decode(String value)
	{
		try {
			value = java.net.URLDecoder.decode(value, "utf-8");
		} catch (UnsupportedEncodingException e) {
			value = "";
		}
		
		return value;
	}
}
