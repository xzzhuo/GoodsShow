package com.goods.utils;

import java.io.File;

import exhi.net.netty.NetFile;

public class AttachmentModule {

	private String mWorkPath = "";
	private String mUpload = "";
	
	/**
	 * Initialize AttachmentModule object
	 * 
	 * @param workPath Current work path
	 * @param uploadFolder Upload folder
	 */
	public AttachmentModule(String workPath, String uploadFolder)
	{
		this.mWorkPath = workPath;
		this.mUpload = uploadFolder;
	}
	
	/**
	 * update upload file
	 * 
	 * @param account current account
	 * @param oldFileName old file name
	 * @param newFileName new file name
	 * 
	 * @return Return true if update upload file success, otherwise return false
	 */
	public boolean updateFile(String account, String oldFileName, NetFile newFileName)
	{
		if (account == null || account.isEmpty())
		{
			return false;
		}
		
		if (oldFileName != null && !oldFileName.isEmpty())
		{
			String root = String.format("%s%s%s%s%s", this.mWorkPath, File.separator, this.mUpload, File.separator, account);
			File delFile = new File(root, oldFileName);
			delFile.delete();
		}
		
		return uploadFile(account, newFileName);
	}
	
	/**
	 * upload file
	 * 
	 * @param account current account
	 * @param fileName new file name
	 * 
	 * @return Return true if upload file success, otherwise return false
	 */
	public boolean uploadFile(String account, NetFile fileName)
	{
		if (account == null || account.isEmpty())
		{
			return false;
		}
		
		boolean result = false;
		if (fileName != null && fileName.size > 0)
		{
			File origin = new File(fileName.tmp_name);
			
			String root = String.format("%s%s%s", this.mWorkPath, File.separator, this.mUpload);
			File dir = new File(root, account);
			File target = new File(dir.getPath(), origin.getName());

			if (!dir.exists())
			{
				dir.mkdirs();
			}
			
			result = origin.renameTo(target);
		}

		return result;
	}
	
	/**
	 * Get attachment file
	 * 
	 * @param account current account
	 * @param fileName new file name
	 * 
	 * @return Return the attachment file
	 */
	public String getFile(String account, String fileName)
	{
		// upload/account/name
		// image/name

		String root = String.format("%s%s%s%s%s", this.mWorkPath, File.separator, this.mUpload, File.separator, account);
		
		File file = new File(root, fileName);
		
		String value = null;
		if (file.exists())
		{
			value = String.format("%s%s%s%s%s", this.mUpload, File.separator, account, File.separator, fileName);
		}
		return value;
	}
}
