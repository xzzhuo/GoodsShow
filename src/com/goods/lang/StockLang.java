package com.goods.lang;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

class StockLang {

	private Map<LangType, LangData> mLangMap = new HashMap<LangType, LangData>();
	
	static private StockLang mInstance = new StockLang();
	
	private StockLang()
	{
		loadStockData();
	}
	
	public static StockLang getInstance()
	{
		return mInstance;
	}
	
	private void loadStockData()
	{
		addNewSource("English", 				"Englist",				"英文");
		addNewSource("Chinese", 				"Chinese",				"中文");
		
		addNewSource("lang_app_name", 			"Goods",				"商品展示");
		addNewSource("lang_title", 				"Goods",				"商品展示");
		addNewSource("lang_system_manage", 		"System Manage",		"系统管理");
		addNewSource("lang_pls_sign_in", 		"Please sign in",		"请登录");
		addNewSource("lang_sign_in", 			"Sign In",				"登录");
		addNewSource("lang_sign_out", 			"Sign Out",				"退出");		
		addNewSource("lang_account", 			"Account",				"账号");
		addNewSource("lang_password", 			"Password",				"密码");
		addNewSource("lang_new_password", 		"New Password",			"新密码");		
		addNewSource("lang_password_confirm", 	"Password Confirm",		"密码确认");		
		addNewSource("lang_remember_me", 		"Rember Me",			"记住账号");
		addNewSource("lang_account_manage", 	"Account Manage",		"账号管理");
		addNewSource("lang_system_info", 		"System Infomation",	"系统信息");
		
		addNewSource("lang_account_name", 		"Account",				"账号名");
		addNewSource("lang_user_name", 			"User Name",			"用户名");
		addNewSource("lang_user_sex", 			"Sex",					"姓别");
		addNewSource("lang_user_birthday",		"Birthday",				"生日");
		addNewSource("lang_phone_number", 		"Phone",				"手机号");
		addNewSource("lang_address", 			"Address",				"住址");
		addNewSource("lang_profile_photo",		"Profile Photo",		"个人头像");
		addNewSource("lang_account_type", 		"Account Type",			"账号类型");
		addNewSource("lang_account_list", 		"Account List",			"账号列表");
		addNewSource("lang_account_add", 		"New Account",			"添加账号");		
		addNewSource("lang_create_date", 		"Crate Date",			"创建日期");
		addNewSource("lang_account_last_login", "Last Login",			"最后登录时间");
		addNewSource("lang_index", 				"Index",				"序号");
		addNewSource("lang_id", 				"ID",					"ID");		
		addNewSource("lang_app_version", 		"App Version",			"系统版本");
		addNewSource("lang_database_version",	"Database Version",		"数据库版本");
		addNewSource("lang_device_info",		"Device Information",	"设备信息");
		addNewSource("lang_operation",			"OP",					"操作");		
		addNewSource("lang_submit",				"Submit",				"提交");
		addNewSource("lang_system_information", "System Infomation",	"系统信息");
		addNewSource("lang_all_fields_required","All fields required",	"所有表单内容必填");		// All form fields are required.
		addNewSource("lang_account_change_password","Change Password",	"修改密码");
		addNewSource("lang_delete",				"Delete",				"删除");
		addNewSource("lang_has_delete",			"Has Delete",			"已经删除");
		addNewSource("lang_user_profile",		"Profile",				"个人信息");
		addNewSource("lang_update_success",		"Update Success",		"更新成功");
		addNewSource("lang_view",				"View",					"查看");
		addNewSource("lang_date_update",		"Update Date",			"更新日期");
		addNewSource("lang_user_setting",		"Settings",				"设置");	
		addNewSource("lang_user_language",		"Language",				"语言");	
		addNewSource("lang_select_all",			"Select All",			"全选");	
		addNewSource("lang_all",				"All",					"所有");
		addNewSource("lang_unknown",			"Unknown",				"未知");
		addNewSource("lang_delete",				"Delete",				"删除");
		addNewSource("lang_manage",				"Manage",				"管理");
		addNewSource("lang_complete",			"Completed",			"完成");
		addNewSource("lang_not_complete",		"Not Completed",		"未完成");
		addNewSource("lang_db_engine",			"Database Engine Version",	"数据库引擎版本");
		addNewSource("lang_Dashboard",			"Dashboard",			"商品展示");
		addNewSource("lang_detail",				"Detail",				"详细");
		addNewSource("lang_show",				"Show",					"显示");
		addNewSource("lang_edit",				"Edit",					"编辑");
		addNewSource("lang_state",				"State",				"状态");

		addNewSource("lang_sign_up", 			"Sign up", 				"创建新账号");
		addNewSource("lang_return_to_signin", 	"Return to sign in", 	"返回登录");
		addNewSource("lang_description", 		"Description", 			"描述");
		addNewSource("lang_creater", 			"Creater", 				"创建者");
		addNewSource("lang_visible", 			"Visible", 				"可见类型");
		addNewSource("lang_access", 			"Access", 				"访问类型");
		addNewSource("lang_remark",				"Remark", 				"备注");		
		
		////////////////////////////////////////////////////////////////////////////////
		// for jquery-ui data
		
		addNewSource("lang_prev_day", 			"Prev",					"前一月");
		addNewSource("lang_next_day", 			"Next",					"后一月");
		addNewSource("lang_week_header", 		"Week",					"星期");		
		addNewSource("lang_today", 				"Today",				"今天");
		addNewSource("lang_done", 				"done",					"完成");
		addNewSource("lang_month_names_short",
				"[\"1 Mon\",\"2 Mon\",\"3 Mon\",\"4 Mon\",\"5 Mon\",\"6 Mon\",\"7 Mon\",\"8 Mon\",\"9 Mon\",\"10 Mon\",\"11 Mon\",\"12 Mon\"]",
				"[\"1 月\",\"2 月\",\"3 月\",\"4 月\",\"5 月\",\"6 月\",\"7 月\",\"8 月\",\"9 月\",\"10 月\",\"11 月\",\"12 月\"]");
		addNewSource("lang_day_names_short",
				"[\"Sun\",\"Mon\",\"Tue\",\"Wed\",\"Thu\",\"Fri\",\"Sat\"]",
				"[\"日\",\"一\",\"二\",\"三\",\"四\",\"五\",\"六\"]");
		addNewSource("lang_day_names_min",
				"[\"Sun\",\"Mon\",\"Tue\",\"Wed\",\"Thu\",\"Fri\",\"Sat\"]",
				"[\"日\",\"一\",\"二\",\"三\",\"四\",\"五\",\"六\"]");

		
		////////////////////////////////////////////////////////////////////////////////
		// special
		
		addNewSource("lang_account_example",	"eg:xxx@yyy.com",	"如：xxx@yyy.com");
		addNewSource("SUPER_ADMIN",				"Super Admin",		"超级管理员");
		addNewSource("ADMIN",					"Admin",			"管理员");
		addNewSource("NORMAL",					"Normal",			"普通人员");

		addNewSource("LANG_ENG",				"English",			"英语");
		addNewSource("LANG_CHS",				"Chinese",			"中文 ");
		
		addNewSource("lang_sex_group",			"[\"Male\",\"Female\",\"Unknown\"]",	"[\"男\",\"女\",\"无\"]");
		
		addNewSource("PUBLIC",					"Public",			"公开");
		addNewSource("PRIVATE",					"Private",			"私有");

		addNewSource("ACTIVE",					"Active",			"活动");
		addNewSource("ARCHIVED",				"Archived",			"归档");
		addNewSource("DELETED",					"Deleted",			"删除");
		
		
		// page title
		addNewSource("lang_main_page",			"Main Page",	"商品展示");
		
		addNewSource("lang_change_user",		"Change User",	"切换用户");
		
		
		////////////////////////////////////////////////////////////////////////////////
		// error information
		
		addNewSource("lang_err_success", 			"Success",					"成功");
		addNewSource("lang_err_account_not_exist", 	"Account is not exist",		"账号不存在");
		addNewSource("lang_err_invalid_password", 	"Password invalid",			"密码错误");
		addNewSource("lang_err_system", 			"System error",				"系统错误");
		addNewSource("lang_err_not_access",			"No access",				"权限限制");
		addNewSource("lang_err_request_data",		"Request error",			"请求数据错误");
		addNewSource("lang_err_add_new_account",	"Add new account failed",	"添加新账号失败");
		addNewSource("lang_err_account_has_exist",	"Account has exist",		"账号已存在");
		addNewSource("lang_err_account_not_exist",	"Account is not exist",		"账号不存在");
		addNewSource("lang_err_pssword_not_allow",	"Password requement",		"密码只允许： a-z 0-9");	// Password field only allow : a-z 0-9
		addNewSource("lang_err_password_confirm",	"Password confirm error",	"密码不一致");			// Confirm password must the same with password
		addNewSource("lang_err_length_must",		"Length Must:",				"长度必须为： ");
		addNewSource("lang_err_not_empty",			"Must Input Value", 		"不能为空");
		addNewSource("lang_err_account_verify",		"Account verify failed",	"账号验证失败，账号或密码不正确");
		addNewSource("lang_err_update_password",	"Password update failed",	"密码更新失败");	
		addNewSource("lang_err_update_data",		"Date update failed",		"数据更新失败");			
		addNewSource("lang_err_delete_account",		"Account delete failed",	"删除账号失败");	
		addNewSource("lang_err_phone_number",		"Phone muber error",		"手机号错误");	
		addNewSource("lang_err_account_code",		"Account code verify failed","账号代码验证失败");	
		addNewSource("lang_err_add_data",			"Add data failed",			"添加数据失败");
		addNewSource("lang_err_update_data",		"Update data failed",		"更新数据失败");
		addNewSource("lang_err_account_expire", 	"Account expire", 			"账号已过期");
		addNewSource("lang_err_date_not_reached", 	"Date is not reached", 		"日期未到");
		
		addNewSource("lang_err_sign_up_success", 	"Create new account success", "创建新账号成功");
		addNewSource("lang_err_update_team_success","Update team success", 		"更新团队成功");
		
		addNewSource("lang_err_item_not_exist",		"Item is not exist",		"该项目不存在");
		addNewSource("lang_err_delete_item",		"Item delete failed",		"项目删除失败");
		addNewSource("lang_err_update_item",		"Item update failed",		"项目更新失败");
		addNewSource("lang_err_not_admin_request",	"Not administrator request","非法的管理员请求，请重新登录");
		addNewSource("lang_err_cmd_not_support", 	"Command is not support", 	"命令不支持");
		addNewSource("lang_err_exception", 			"Exception error", 			"异常错误");
		addNewSource("lang_error_file_size", 		"file size is get to limit","文件大小到达最大值");
		addNewSource("lang_err_number_format", 		"member format error",		"数字格式错误");
	}
	
	public void addNewSource(String key, String eng, String chs)
	{
		LangData mapEng = null;
		if (mLangMap.containsKey(LangType.LANG_ENG))
		{
			mapEng = mLangMap.get(LangType.LANG_ENG);
		}
		else
		{
			mapEng = new LangData(LangType.LANG_ENG.name());
			mLangMap.put(LangType.LANG_ENG, mapEng);
		}
		
		LangData mapChs = null;
		if (mLangMap.containsKey(LangType.LANG_CHS))
		{
			mapChs = mLangMap.get(LangType.LANG_CHS);
		}
		else
		{
			mapChs = new LangData(LangType.LANG_CHS.name());
			mLangMap.put(LangType.LANG_CHS, mapChs);
		}
		
		mapEng.getLangMap().put(key, eng);
		mapChs.getLangMap().put(key, chs);
	}

	public Collection<LangData> getLangList()
	{
		return this.mLangMap.values();
	}
}
