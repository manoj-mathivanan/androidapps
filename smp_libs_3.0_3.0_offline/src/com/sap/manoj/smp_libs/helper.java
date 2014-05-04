package com.sap.manoj.smp_libs;

import java.util.List;

import org.apache.http.Header;

import com.sap.mobile.lib.cache.Cache;
import com.sap.mobile.lib.parser.IODataEntry;
import com.sap.mobile.lib.parser.IODataSchema;
import com.sap.mobile.lib.parser.IODataServiceDocument;

public class helper {
	
	//public static String IP = "https://smpapr04sapperf2.prod.jpaas.sapbydesign.com";
	//public static String PORT = "8001";
	//public static String APPNAME = "ldci";
	///public static String APPID = "";
	//public static String CAPTCHA="";
	//public static String csrf;
	////public static String cookie;
	protected static Cache ofcache;
	protected static Cache lcache;
	public static String AGENCY="";
	public static IODataServiceDocument serviceDoc;
	public static IODataSchema schema=null;
	public static Integer row=1;
	public static long start;
	public static long stop;
	public static long end;

	public static List<IODataEntry> reads;
	public static String deltalink;
	//public static String serverIP = "10.66.176.121"/*"10.66.188.221"*/;
	//public static String Port,appId="com.sap.travelapp",connID,secCon = "SSO"/*"SMNetworkEdge"*/,domain="default";// SSO CERT
	//g8p
	public static String Port,appId="ldci2",connID,secCon = "ldci2_SC"/*"SMNetworkEdge"*/,domain="default";
	public static String userName = "perfbb"; 
	public static String password = "perfbb";
	public static String AppconnID;
	public static String serviceDocUrl;
	public static String collectionId="TravelAgencies_DQ";
	public static Header[] headers;
    public static String pushendpt;

    public static String smpurl = "http://10.66.141.166:8080";
    //public static String smpurl = "http://10.68.139.106:5001/ias_relay_server/client/rs_client.dll/pwdf3177";

}