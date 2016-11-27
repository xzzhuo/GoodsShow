package com.goods.process;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.goods.application.GoodsConfig;
import com.goods.constant.MyConstant;
import com.goods.data.AccountData;
import com.goods.data.AccountType;
import com.goods.data.SystemData;
import com.goods.table.AccountTable;

import exhi.net.log.NetLog;
import exhi.net.netty.NetFile;

public class GoodsProcess extends BaseProcess {

	protected final static String ACCOUNT_UUID 	= "account_uuid";
	protected final static String ITEM_ID 		= "item_id";
	
	protected final static String FAILED 		= "failed";
	protected final static String SUCCESS 		= "OK";
	
	protected final static String COOKIE_ACCOUNT = MyConstant.COOKIE_ACCOUNT;
	protected final static String COOKIE_CODE = MyConstant.COOKIE_CODE;
	// protected final static String COOKIE_LANGUAGE = MyConstant.COOKIE_LANGUAGE;
	
	protected final static String FORMAT_RETURN = "{\"result\":\"%s\",\"message\":\"%s\",\"uri\":\"%s\"}";
	
	protected AccountData mCurrentAccount = null;

	@Override
	public void onProcess(String client, String path, Map<String, String> request) {

		try
		{
			File templateFile = new File(path);
			String act = "";
			
			super.initProcess(client, path, request);
			this.initDatabase();

			/*
			String lan = this.getCookie(COOKIE_LANGUAGE);
			if (lan != null)
			{
				super.setCurrentLanguage(lan);
			}
			*/
			
			String newAct = this.checkAccount(client, request);
			if (!newAct.isEmpty())
			{
				act = newAct; 
			}
			else
			{
				if (request.containsKey("act"))
				{
					act = request.get("act");
					request.remove("act");
				}
				
				if (act.isEmpty())
				{
					act = "main";
				}
			}
			
			NetLog.debug(client, "act=" + act);
			
			if (mCurrentAccount != null)
			{
				String name = mCurrentAccount.getName();
				name = (name==null?null:(name.isEmpty()?null:name));
				
				// this.setCookie(COOKIE_LANGUAGE, this.mCurrentAccount.getLanguage());
				super.setCurrentLanguage(this.mCurrentAccount.getLanguage());
				mWebUtil.assign("isManager", mCurrentAccount.isManager());
				mWebUtil.assign("current_account", mCurrentAccount.getAccount());
				mWebUtil.assign("current_name", name);
			}
			
			this.doProcess(client, path, templateFile, act, request);
		}
		catch(ShowErrorException ex)
		{
			NetLog.error(client, ex.getMessage());
			this.die(ex.getMessage());
		}
		catch(NullPointerException ex)
		{
			if (ex.getStackTrace().length > 0)
			{
				StackTraceElement stack = ex.getStackTrace()[0];
				NetLog.error(client, "Null pointer: " +  stack.toString());
			}
			else
			{
				NetLog.error(client, ex.getMessage());
			}
			this.die(ex.getMessage());
		}
		catch(Exception ex)
		{
			NetLog.error(client, ex.getMessage());
			this.die(ex.getMessage());
		}
		
	}

	private String checkAccount(String client, Map<String, String> request) {

		String act = "";
		if (request.containsKey("act"))
		{
			act = request.get("act");
		}
		
		List<String> mIgnoreAct = new ArrayList<String>();
		mIgnoreAct.add(MyConstant.COMMAND_MENU_LOGIN);			// show login UI
		mIgnoreAct.add(MyConstant.COMMAND_MENU_SIGN_OUT);		// quit
		mIgnoreAct.add(MyConstant.COMMAND_MENU_SIGN_UP);
		mIgnoreAct.add(MyConstant.COMMAND_ACT_SIGN_UP);

		if (!mIgnoreAct.contains(act))
		{
			boolean checkResult = false;
			
			String account = "";

			AccountTable accountTable = new AccountTable();
			if (act.equalsIgnoreCase(MyConstant.COMMAND_ACT_SIGN_IN))
			{
				String password = "";
				
				if (request.containsKey("account"))
				{
					account = request.get("account");
				}
				
				if (request.containsKey("password"))
				{
					password = request.get("password");
				}
				
				String errorJson = "";
				checkResult = accountTable.isExist(account);
				if (checkResult)
				{
					checkResult = accountTable.checkAccount(account, password);
					
					if (checkResult)
					{
						NetLog.info(client, "Check account password success");
						
						accountTable.updateRandCode(account, password);
						mCurrentAccount = accountTable.queryAccount(account);
						
						this.setCookie(COOKIE_ACCOUNT, mCurrentAccount.getAccount());
						this.setCookie(COOKIE_CODE, mCurrentAccount.getCode(), 36000);
						errorJson = this.MakeJsonReturn("OK", this.getCurLangMap().get("lang_err_success"));
					}
					else
					{
						NetLog.warning(client, "Check account password failed");
						errorJson = this.MakeJsonReturn("failed_password", this.getCurLangMap().get("lang_err_invalid_password"));
					}
				}
				else
				{
					NetLog.warning(client, "Account is not exist");
					errorJson = this.MakeJsonReturn("failed_account", this.getCurLangMap().get("lang_err_account_not_exist"));
				}
				this.print(errorJson);
			}
			else
			{
				String code = "";
				
				if (this.getCookie(COOKIE_ACCOUNT) != null)
				{
					account = this.getCookie(COOKIE_ACCOUNT);
				}
				
				if (this.getCookie(COOKIE_CODE) != null)
				{
					code = this.getCookie(COOKIE_CODE);
				}
				
				checkResult = accountTable.checkCode(account, code);
				if (checkResult)
				{
					mCurrentAccount = accountTable.queryAccount(account);
					NetLog.info(client, "Check account code success");
				}
				else
				{
					NetLog.warning(client, "Check account code failed");
					act = MyConstant.COMMAND_ACT_LOGIN;
				}
			}
		}
		
		return act;
	}

	private void initDatabase() throws ShowErrorException
	{
		// check system version

	}

	@Override
	protected boolean doProcess(String client, String path, File tempFile, String act, Map<String, String> request) throws ShowErrorException
	{
		if (act.equals("main"))
		{
			mWebUtil.display(tempFile.getName());
		}
		else if (act.equals(MyConstant.COMMAND_MENU_LOGIN))
		{
			// Show the login UI
			mWebUtil.assign("title", super.getCurLangMap().get("lang_sign_in"));
			mWebUtil.display(tempFile.getName());
		}
		// user login
		else if (act.equals(MyConstant.COMMAND_ACT_LOGIN))
		{
			this.location(String.format("login.html?act=%s", MyConstant.COMMAND_MENU_LOGIN));
		}
		// show main page
		else if (act.equals(MyConstant.COMMAND_ACT_SIGN_IN))
		{
			// do noting
			
			// login success and show first page
			// mWebUtil.assign("title", super.getCurLangMap().get("lang_main_guid"));
			// mWebUtil.display(tempFile.getName());
		}
		else if (act.equals(MyConstant.COMMAND_MENU_SIGN_OUT))
		{
			this.deleteCookie(COOKIE_CODE);
			this.location(String.format("login.html?act=%s", MyConstant.COMMAND_MENU_LOGIN));
		}
		else if (act.equals(MyConstant.COMMAND_MENU_SHOW_SYSTEM_INFO))
		{
			SystemData systemData = new SystemData();
			mWebUtil.assign("title", super.getCurLangMap().get("lang_system_information"));
			mWebUtil.assign("data", systemData);
			mWebUtil.display(tempFile.getName());
		}
		else if (act.equals(MyConstant.COMMAND_MENU_MAIN_PAGE))
		{
			mWebUtil.assign("title", super.getCurLangMap().get("lang_main_page"));
			mWebUtil.display(tempFile.getName());
		}
		else if (act.equals(MyConstant.COMMAND_MENU_SIGN_UP))
		{
			this.mWebUtil.assign("account_type_normal", AccountType.NORMAL.name());
			this.mWebUtil.assign("title", this.getCurLangMap().get("lang_sign_up"));
			this.mWebUtil.display(tempFile.getName());
		}
		else if (act.equals(MyConstant.COMMAND_ACT_SIGN_UP))
		{
			CheckRequestResult checkValue = super.checkRequestMap(new ICheckRequest(){
				@Override
				public CheckRequestResult checkRequest(BaseProcess process, Map<String, String> request, String client) {
					
					CheckRequestResult result = new CheckRequestResult();
					result.result = false;
					
					if (!request.containsKey("type"))
					{
						NetLog.error(client, "request data error");
						result.json_return = MakeJsonReturn(FAILED, process.getCurLangMap().get("lang_err_request_data"));
						
						return result;
					}
					
					String type = request.get("type");
					
					if (!type.equals(AccountType.NORMAL.name()))
					{
						NetLog.error(client, "request data error");
						result.json_return = MakeJsonReturn(FAILED, process.getCurLangMap().get("lang_err_request_data"));
						
						return result;
					}
					
					List<String> keys = new ArrayList<String>();
					keys.add("account");
					keys.add("password");

					if (!BaseProcess.checkRequestMap(request, keys))
					{
						NetLog.error(client, "request data error");
						result.json_return = MakeJsonReturn(FAILED, process.getCurLangMap().get("lang_err_request_data"));
						
						return result;
					}
					
					result.result = true;
					return result;
				}
			}, this, request, client);
			
			
			String retValue = "";
			if (checkValue.result)
			{
				String account = request.get("account");
				String password = request.get("password");
				String type = request.get("type");
				
				AccountType accountType = AccountType.NORMAL;
				try
				{
					accountType = AccountType.valueOf(type);
				}
				catch(Exception ex)
				{
					// skip it
				}
				
				AccountTable accountTable = new AccountTable();
				
				if (!accountTable.isExist(account))
				{
					AccountData newAccount = null;
					newAccount = accountTable.insertNewAccount(account, password, accountType);
					
					if (newAccount != null)
					{
						retValue = MakeJsonReturn(SUCCESS, super.getCurLangMap().get("lang_err_success"));
					}
					else
					{
						NetLog.error(client, "add new account failed");
						retValue = MakeJsonReturn(FAILED, super.getCurLangMap().get("lang_err_add_new_account"));
					}
				}
				else
				{
					NetLog.error(client, "account has exist");
					retValue = MakeJsonReturn(FAILED, super.getCurLangMap().get("lang_err_account_has_exist"));
				}
			}
			else
			{
				retValue = checkValue.json_return;
			}
			
			this.print(retValue);
		}
		
		////////////////////////////////////////////////////////////////////////////////
		// Account manage
		
		else if (act.equals(MyConstant.COMMAND_MENU_SHOW_ACCOUNT_LIST))
		{
			if (this.mCurrentAccount.isManager())
			{
				AccountTable accountTable = new AccountTable();
				
				List<Map<String, Object>> accountList = accountTable.query(new String[]{"id","account","name","type","signdate"});
				mWebUtil.assign("list", accountList);			
			}
			else
			{
				throw new ShowErrorException(super.getCurLangMap().get("lang_err_not_access"));
			}

			mWebUtil.assign("title", super.getCurLangMap().get("lang_account_manage"));
			mWebUtil.display(tempFile.getName());
		}
		else if (act.equals(MyConstant.COMMAND_MENU_ADD_ACCOUNT))
		{
			// just only can add 'ADMIN' and 'NORMAL' user
			mWebUtil.assign("title", super.getCurLangMap().get("lang_account_add"));
			AccountType[] typeList = null;
			
			if (this.mCurrentAccount.getType().equalsIgnoreCase(AccountType.SUPER_ADMIN.name()))
			{
				typeList = new AccountType[]{AccountType.ADMIN, AccountType.NORMAL};
			}
			else if (this.mCurrentAccount.getType().equalsIgnoreCase(AccountType.ADMIN.name()))
			{
				typeList = new AccountType[]{AccountType.NORMAL};
			}
			else
			{
				throw new ShowErrorException(super.getCurLangMap().get("lang_err_not_access"));
			}
			mWebUtil.assign("list", typeList);
			mWebUtil.display(tempFile.getName());
		}
		else if (act.equals(MyConstant.COMMAND_ACT_ADD_ACCOUNT))
		{
			String retValue = "";
			if (this.mCurrentAccount.isManager())
			{
				if (request.containsKey("account") &&
						request.containsKey("password") &&
						request.containsKey("type"))
				{
					String account = request.get("account");
					String password = request.get("password");
					
					AccountType type = AccountType.NORMAL;
					try
					{
						type = AccountType.valueOf(request.get("type"));
					}
					catch(Exception e)
					{
						NetLog.error(client, e.getMessage());
					}
					
					AccountTable accountTable = new AccountTable();
					
					if (!accountTable.isExist(account))
					{
						AccountData newAccount = accountTable.insertNewAccount(
								account, password, type, GoodsConfig.instance().getLangTypeName());
						
						if (newAccount != null)
						{
							retValue = MakeJsonReturn(SUCCESS, super.getCurLangMap().get("lang_err_success"));
						}
						else
						{
							NetLog.error(client, "add new account failed");
							retValue = MakeJsonReturn(FAILED, super.getCurLangMap().get("lang_err_add_new_account"));
						}
					}
					else
					{
						NetLog.error(client, "account has exist");
						retValue = MakeJsonReturn(FAILED, super.getCurLangMap().get("lang_err_account_has_exist"));
					}
				}
				else
				{
					NetLog.error(client, "request data error");
					retValue = MakeJsonReturn(FAILED, super.getCurLangMap().get("lang_err_request_data"));
				}
			}
			else
			{
				NetLog.error(client, "access error");
				retValue = MakeJsonReturn(FAILED, super.getCurLangMap().get("lang_err_not_access"));
			}
			
			this.print(retValue);
		}
		else if (act.equals(MyConstant.COMMAND_MENU_CHANGE_PASSWORD))
		{
			mWebUtil.assign("title", super.getCurLangMap().get("lang_account_change_password"));
			mWebUtil.assign("current_account", this.mCurrentAccount.getAccount());
			mWebUtil.display(tempFile.getName());
		}
		else if (act.equals(MyConstant.COMMAND_ACT_CHANGE_PASSWORD))
		{
			String retValue = "";
			if (request.containsKey("account") &&
					request.containsKey("password") &&
					request.containsKey("new_password"))
			{
				String account = request.get("account");
				String password = request.get("password");
				String newPassword = request.get("new_password");
				
				if (account.equals(this.mCurrentAccount.getAccount()))
				{
					AccountTable accountTable = new AccountTable();
	
					if (accountTable.isExist(account))
					{
						if (accountTable.checkAccount(account, password))
						{
							AccountData newAccount = accountTable.updatePassword(account, password, newPassword);
							
							if (newAccount != null)
							{
								retValue = MakeJsonReturn(SUCCESS, super.getCurLangMap().get("lang_err_success"));
							}
							else
							{
								NetLog.error(client, "update password failed");
								retValue = MakeJsonReturn(FAILED, super.getCurLangMap().get("lang_err_update_password"));
							}
						}
						else
						{
							NetLog.error(client, "account verify failed");
							retValue = MakeJsonReturn(FAILED, super.getCurLangMap().get("lang_err_account_verify"));
						}
					}
					else
					{
						NetLog.error(client, "account not exist");
						retValue = MakeJsonReturn(FAILED, super.getCurLangMap().get("lang_err_account_not_exist"));
					}
				}
				else
				{
					NetLog.error(client, "account expire");
					retValue = MakeJsonReturn(FAILED, super.getCurLangMap().get("lang_err_account_expire"));
				}
			}
			else
			{
				NetLog.error(client, "request data error");
				retValue = MakeJsonReturn(FAILED, super.getCurLangMap().get("lang_err_request_data"));
			}
			
			this.print(retValue);
		}
		else if (act.equals(MyConstant.COMMAND_ACT_ACCOUNT_DELETE))
		{
			String retValue = "";
			if (this.mCurrentAccount.isManager())
			{
				AccountTable accountTable = new AccountTable();
				
				String account = "";
				if (request.containsKey("account"))
				{
					account = request.get("account");
				}
				
				AccountData accountData = accountTable.queryAccount(account);
				
				if (accountData==null)
				{
					NetLog.error(client, "account not exist");
					retValue = MakeJsonReturn(FAILED, super.getCurLangMap().get("lang_err_account_not_exist"));
					this.print(retValue);
				}
				else if (accountData.getAccount().equals(this.mCurrentAccount.getAccount()) 
						|| accountData.getType().equals(AccountType.SUPER_ADMIN.name())
						|| accountData.getType().equals(this.mCurrentAccount.getType()))
				{
					NetLog.error(client, "access error");
					retValue = MakeJsonReturn(FAILED, super.getCurLangMap().get("lang_err_not_access"));
					this.print(retValue);
				}
				else
				{
					if (accountTable.deleteAccount(account))
					{
						String location = String.format("account_list.html?act=menu_show_account_list&rand=%s", (new Random()).nextInt(1000000));
						this.location(location);
					}
					else
					{
						NetLog.error(client, "delete account failed");
						retValue = MakeJsonReturn(FAILED, super.getCurLangMap().get("lang_err_delete_account"));
						this.print(retValue);
					}
				}
			}
			else
			{
				NetLog.error(client, "access error");
				retValue = MakeJsonReturn(FAILED, super.getCurLangMap().get("lang_err_not_access"));
				this.print(retValue);
			}
		}
		
		else if (act.equals("act_upload_file"))
		{
			// file_name=user_photo
			String fileName = request.get("file_name");
			NetLog.error("File1", fileName);
			
			if (fileName == null || fileName.isEmpty())
			{
				NetLog.error("File2", "Upload error");
			}
			else
			{
				NetLog.error("File3", fileName);
				NetFile netfile = this.getFile(fileName);
				
				if (netfile == null)
				{
					NetLog.error("File4", "Upload error");
				}
				else
				{
					NetLog.error("File5", netfile.tmp_name);
				}
			}
			
			this.print("act_upload_file");
		}
		// test jquery form
		else if (act.equals("act_upload_file1"))
		{
			// file_name=user_photo
			String fileName = request.get("file_name");
			NetLog.error("File1", fileName);
			
			if (fileName == null || fileName.isEmpty())
			{
				NetLog.error("File2", "Upload error");
			}
			else
			{
				NetLog.error("File3", fileName);
				NetFile netfile = this.getFile(fileName);
				
				if (netfile == null)
				{
					NetLog.error("File4", "Upload error");
				}
				else
				{
					NetLog.error("File5", netfile.tmp_name);
				}
			}
			
			this.print("act_upload_file");
		}

		////////////////////////////////////////////////////////////////////////////////
		
		else
		{
			NetLog.error(client, "Not iplement: act = " + act);
			this.print("Not implement");
		}
		
		return true;
	}
	
	//////////////////////////////////////////////////////////////////////////////

	protected String makeJsonReturn(String result, String message, String location)
	{
		// FORMAT_RETURN = "{\"result\":\"%s\",\"message\":\"%s\",\"uri\":\"%s\"}";
		return String.format(FORMAT_RETURN, result, message, location);
	}
	
	protected String MakeJsonReturn(String result, String message)
	{
		return makeJsonReturn(result, message, "");
	}
}
