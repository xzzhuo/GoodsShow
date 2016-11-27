package com.goods.table;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import com.goods.application.GoodsConfig;
import com.goods.data.AccountData;
import com.goods.data.AccountType;
import com.goods.data.SimpleAccount;
import com.goods.database.BaseTable;
import com.goods.database.TableName;
import com.goods.utils.MyUtils;

import exhi.net.database.NetDatabase;
import exhi.net.utils.DbUtils;

public class AccountTable extends BaseTable {

	static public final String FLAG = "goods";
	static public final String FIELDS_ACCOUNT	= "account";
	static public final String FIELDS_UUID		= "uuid";
	static public final String FIELDS_NAME		= "name";
	
	public AccountTable() {
		super(GoodsConfig.instance().getDatabaseParam(),
				TableName.getAccount(),
				1, 1);
	}

	@Override
	public void onCreateTable(NetDatabase db) {
		
		String sql = String.format("create table %s (", getTableName());
		sql += "id INT NOT NULL AUTO_INCREMENT,";
		sql += "PRIMARY KEY (id),";

		sql += String.format("%s VARCHAR(64) NOT NULL UNIQUE,", FIELDS_UUID);
		sql += String.format("%s VARCHAR(64) NOT NULL UNIQUE,", FIELDS_ACCOUNT);
		sql += String.format("%s VARCHAR(64) NOT NULL,", FIELDS_NAME);
		sql += "device VARCHAR(64),";
		sql += "type VARCHAR(64) NOT NULL DEFAULT 'NORMAL',";
		sql += "password VARCHAR(64) NOT NULL,";
		sql += "code VARCHAR(64) NOT NULL DEFAULT '',";
		sql += "token VARCHAR(64) NOT NULL DEFAULT '',";
		sql += "signdate TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,";

		// personal setting
		sql += "sex VARCHAR(8),";
		sql += "birthday VARCHAR(64),";
		sql += "photo VARCHAR(256),";

		sql += "phone VARCHAR(32),";
		sql += "city VARCHAR(64),";
		sql += "address VARCHAR(256),";
		
		sql += "language VARCHAR(32) DEFAULT 'LANG_CHS',";		
		
		// sql += "encryption_code VARCHAR(256) NOT NULL";
		sql += "reserve INT DEFAULT '0'";
		sql += ") ENGINE=InnoDB DEFAULT CHARSET=UTF8";
		
		db.createTable(sql);
	}

	@Override
	public void onInitTable()
	{
		createAdminAccount();
		createGuestAccount();
	}

	public AccountData insertNewAccount(String account, String password, AccountType type)
	{
		return this.insertNewAccount(account, password, type, null);
	}

	public AccountData insertNewAccount(String account, String password, AccountType type, String lang)
	{
		if (account.isEmpty() || password.isEmpty())
		{
			return null;
		}
		
		AccountData data = new AccountData();
		String code = makeRandCode();
		String token = makeRandCode();
		
		data.setUuid(UUID.randomUUID().toString());
		data.setAccount(account);
		data.setPassword(makeMd5(password+code+token));
		data.setCode(code);
		data.setToken(token);
		data.setType(type.name());
		
		if (lang != null)
		{
			data.setLanguage(lang);
		}

		Map<String, Object> map = MyUtils.transferBean2Map(data);
		map.remove("id");
		map.remove("manager");
		map.remove("signdate");

		if (this.insert(map, "id") == -1)
		{
			data = null;
		}
		
		return data;
	}
	
	private AccountData createAdminAccount()
	{
		AccountData data = this.queryAccount("admin");
		if (data != null)
		{
			return data;
		}
		return insertNewAccount("admin", "admin", AccountType.SUPER_ADMIN);
	}
	
	private AccountData createGuestAccount()
	{
		AccountData data = this.queryAccount("guest");
		if (data != null)
		{
			return data;
		}
		return insertNewAccount("guest", "guest", AccountType.NORMAL);
	}
	
	private String makeRandCode()
	{
		return String.format("%d", (new Random()).nextInt(1000000));
	}

	/**
	* Check account with password
	* @author xiaozhao
	* @param account the user account
	* @param password the user account
	* @return return true if success or return false
	*/
	public boolean checkAccount(String account, String password)
	{
		AccountData data = queryAccount(account);
		
		if (data != null)
		{
			password = makeMd5(password + data.getCode() + data.getToken());
			return (account.equals(data.getAccount()) && password.equals(data.getPassword()));
		}
		else
		{
			return false;
		}
	}

	/**
	* Check account with code
	* @author xiaozhao
	* @param account the user account
	* @param code the account code
	* @return return true if success or return false
	*/
	public boolean checkCode(String account, String code)
	{
		if (this.getCount() == 0)
		{
			return false;
		}
		
		AccountData data = queryAccount(account);
		
		if (data == null)
		{
			return false;
		}
		
		return code.equals(data.getCode());
	}
	
	/**
	* Check account with token
	* @author xiaozhao
	* @param account the user account
	* @param code the account code
	* @return return true if success or return false
	*/
	public boolean checkToken(String account, String token)
	{
		if (this.getCount() == 0)
		{
			return false;
		}
		
		AccountData data = queryAccount(account);
		
		if (data == null)
		{
			return false;
		}
		
		return token.equals(data.getToken());
	}
	
	public boolean updateRandCode(String account, String password)
	{	
		return this.updateRandCode(account, password, 0);
	}
	
	public boolean updateRandCode(String account, String password, int device)
	{
		if (this.getCount() == 0)
		{
			return false;
		}
		
		boolean retval = false;
		
		AccountData data = queryAccount(account);
		if (data != null)
		{
			String newPassword = makeMd5(password + data.getCode() + data.getToken());
			if (newPassword.equals(data.getPassword()))
			{
				String code = data.getCode();
				String token = data.getToken();
				
				if (device == 0)
				{
					code = makeRandCode();
				}
				else
				{
					token = makeRandCode();
				}
				
				newPassword = makeMd5(password + code + token);
				String sql = String.format("UPDATE %s SET password='%s', code='%s', token='%s', device=%d WHERE %s LIKE '%s'",
						this.getTableName(), newPassword, code, token, device, FIELDS_ACCOUNT, account);
				retval = this.update(sql);
			}
		}
		
		return retval;
	}
	
	public AccountData queryAccount(String account)
	{
		AccountData data = null;
		
		String sql = String.format("SELECT * FROM %s WHERE %s LIKE '%s' LIMIT 1", this.getTableName(), FIELDS_ACCOUNT, account);
		List<Map<String, Object>> listMap = this.query(sql);
		
		if (listMap != null && listMap.size() == 1)
		{
			Map<String, Object> map = listMap.get(0);
			data = MyUtils.transferMap2Bean(map, AccountData.class);
		}

		return data;
	}
	
	public AccountData queryByUuid(String uuid)
	{
		AccountData data = null;
		
		String sql = String.format("SELECT * FROM %s WHERE %s LIKE '%s' LIMIT 1", this.getTableName(), FIELDS_UUID, uuid);
		List<Map<String, Object>> listMap = this.query(sql);
		
		if (listMap != null && listMap.size() == 1)
		{
			Map<String, Object> map = listMap.get(0);
			data = MyUtils.transferMap2Bean(map, AccountData.class);
		}

		return data;
	}
	
	public List<SimpleAccount> querySimpleAccountByUuidArray(List<String> uuidArray) {

		List<SimpleAccount> accountList = new ArrayList<SimpleAccount>();
		
		String wheres = "";
		
		if (uuidArray.size() > 0)
		{
			for (String uuid : uuidArray)
			{
				wheres += String.format(" OR %s LIKE '%s'", FIELDS_UUID, uuid);
			}
		}
		
		String sql = String.format("SELECT * FROM %s WHERE (1=0%s)", this.getTableName(), wheres);
		List<Map<String, Object>> listMap = this.query(sql);
		
		for(Map<String, Object> map: listMap)
		{
			SimpleAccount data = MyUtils.transferMap2Bean(map, SimpleAccount.class);
			if (data != null &&
					!data.getUuid().isEmpty() &&
					!data.getAccount().isEmpty() &&
					(data.getType() != null && !data.getType().isEmpty()))
			{
				accountList.add(data);
			}
		}

		return accountList;
	}
	
	public List<SimpleAccount> searchSimpleAccount(String value) {

		List<SimpleAccount> accountList = new ArrayList<SimpleAccount>();
		
		String wheres = String.format("%s LIKE '%%%s%%'", FIELDS_ACCOUNT, value);
		
		String sql = String.format("SELECT * FROM %s WHERE %s", this.getTableName(), wheres);
		List<Map<String, Object>> listMap = this.query(sql);
		
		for(Map<String, Object> map: listMap)
		{
			SimpleAccount data = MyUtils.transferMap2Bean(map, SimpleAccount.class);
			if (data != null &&
					!data.getUuid().isEmpty() &&
					!data.getAccount().isEmpty() &&
					(data.getType() != null && !data.getType().isEmpty()))
			{
				accountList.add(data);
			}
		}

		return accountList;
	}
	
	public List<AccountData> queryAllAccount()
	{
		List<AccountData> accountList = new ArrayList<AccountData>();
		
		String sql = String.format("SELECT * FROM %s", this.getTableName());
		List<Map<String, Object>> listMap = this.query(sql);
		
		for(Map<String, Object> map: listMap)
		{
			AccountData data = MyUtils.transferMap2Bean(map, AccountData.class);
			if (data != null &&
					!data.getUuid().isEmpty() &&
					!data.getAccount().isEmpty() &&
					(data.getType() != null && !data.getType().isEmpty()))
			{
				accountList.add(data);
			}
		}

		return accountList;
	}

	public String[] queryAllAccountValue() {
		
		String sql = String.format("SELECT %s FROM %s", FIELDS_ACCOUNT, this.getTableName());
		List<Map<String, Object>> listMap = this.query(sql);
		
		String[] val = null;
		if (listMap.size() > 0)
		{
			val = new String[listMap.size()];
			for(int i=0; i<listMap.size(); i++)
			{
				Map<String, Object> map = listMap.get(i);
				val[i] = (String) map.get(FIELDS_ACCOUNT);
			}
		}
		
		return val;
	}
	
	private String makeMd5(String string) {
		byte[] data = (FLAG+string).getBytes();
		MessageDigest md = null;
		boolean result = false;
		try {
			md = MessageDigest.getInstance("MD5");
			md.update(data, 0, data.length);
			data = md.digest();
			result = true;
		} catch (NoSuchAlgorithmException e) {
			
		}

		StringBuilder sb = new StringBuilder();
		if (result == true)
		{
			for (byte b:data)
			{
				sb.append(String.format("%02X", b));
			}
		}

		return sb.toString();
	}

	public boolean isExist(String account) {
		return (this.queryAccount(account) != null);
	}

	public AccountData updatePassword(String account, String password,
			String newPassword) {
		
		AccountData retval = null;
		
		if (!this.checkAccount(account, password))
		{
			return null;
		}
		
		AccountData data = queryAccount(account);
		if (data != null)
		{
			newPassword = makeMd5(newPassword + data.getCode() + data.getToken());
			String sql = String.format("UPDATE %s SET password='%s', code='%s' WHERE %s LIKE '%s'",
					this.getTableName(), newPassword, data.getCode(), FIELDS_ACCOUNT, account);
			if (this.update(sql))
			{
				retval = queryAccount(account);
			}
		}
		
		return retval;
	}

	public boolean deleteAccountById(int id) {
		String sql = String.format("DELETE FROM %s WHERE id=%d", this.getTableName(), id);
		return this.delete(sql);
	}

	public boolean deleteAccount(String account) {
		String sql = String.format("DELETE FROM %s WHERE %s LIKE '%s'", this.getTableName(), FIELDS_ACCOUNT, account);
		return this.delete(sql);
	}

	private boolean updateData(String account, Map<String, Object> value) {

		if (value.containsKey("id"))
		{
			value.remove("id");
		}
		if (value.containsKey(FIELDS_ACCOUNT))
		{
			value.remove(FIELDS_ACCOUNT);
		}		
		if (value.containsKey(FIELDS_UUID))
		{
			value.remove(FIELDS_UUID);
		}
		if (value.containsKey("device"))
		{
			value.remove("device");
		}
		if (value.containsKey("password"))
		{
			value.remove("password");
		}
		if (value.containsKey("type"))
		{
			value.remove("type");
		}
		if (value.containsKey("code"))
		{
			value.remove("code");
		}
		if (value.containsKey("token"))
		{
			value.remove("token");
		}
		if (value.containsKey("signdate"))
		{
			value.remove("signdate");
		}
		
		String sql = DbUtils.getUpdateSql(this.getTableName(), value, String.format("%s LIKE '%s'", FIELDS_ACCOUNT, account));
		return this.update(sql);
	}

	public boolean updateProfile(String account, String name, String sex,
			String birthday, String phone, String address, String photo) {

		Map<String, Object> value = new HashMap<String, Object>();
		
		value.put(FIELDS_NAME, name);
		value.put("sex", sex);
		value.put("birthday", birthday);
		value.put("phone", phone);
		value.put("address", address);
		value.put("photo", photo);
		
		return this.updateData(account, value);
	}

	public boolean updateSetting(String account, String language) {
		
		Map<String, Object> value = new HashMap<String, Object>();
		
		value.put("language", language);
		
		return this.updateData(account, value);
	}
}
