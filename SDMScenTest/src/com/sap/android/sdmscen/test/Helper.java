package com.sap.android.sdmscen.test;

import android.content.Context;

import com.sybase.mobile.lib.client.ODPCertificateManager;
import com.sybase.persistence.PrivateDataVault;

public class Helper {
	
	public static  String SEVERIP; 
	public static  int SERVERPORT; 
	public static  String COMPANYID; 
	protected static final String USERNAME ="perfbb";//"supuser";//"sybase101";//"try@sap.com";//
	protected static final String PASSWORD ="perfbb";//"sybase123";////"password";//
	
	//Details for Manual Synchronization 
	protected static final String USERNAME_MANUAL_SYNC ="and_sync";
	protected static final String USERNAME_MANUAL_ASYNC ="and_async";
	protected static final String ACT_CODE = "123";
	
	protected static final String APP_NAME ="com.sap.travelapp";// "com.sap.travelapp";// "APITEST_BB_RBS";//
	protected static String SECURITY_CONFIG ="SSO";//"nnesso2";//"nonnetwork";//";// 
	protected static String SECURITY_CONFIG_HttpAuth ="HttpAuth";//"NESSO";//
	protected static String SECURITY_CONFIG_CERT ="CERT";
	
	protected static String serviceDocUrl = "";	
	public static String PUSH_ENDPOINT = "";
	
	protected static final String USERNAME_CERT = "SUPUSER";
	protected static String PASSWORD_CERT ;//=  ODPCertificateManager.getSignedCertificateFromFile("/mnt/sdcard/SUPUSER.p12", "mobile");
	
	public static String VAULT_PASSWORD = "VaultPassword";
	public static String OLD_VAULT_PASSWORD = "currentPassword";
	protected static String VAULT_SALT = "VaultSalt";
	protected static String VAULT_ID = "MyVault";
	public PrivateDataVault ldv;
	public static boolean PasswordPolicy = false;
	
	public static Context MyContext;
}
