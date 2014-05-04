package com.sap.android.sdmscen.test;

/*import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;*/
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import junit.framework.AssertionFailedError;
import junit.framework.TestResult;

import org.apache.http.HttpResponse;

import android.app.NotificationManager;
import android.test.AndroidTestCase;
import android.util.Log;

import com.sap.mobile.lib.sdmcache.SDMCache;
import com.sap.mobile.lib.sdmconfiguration.ISDMPreferences;
import com.sap.mobile.lib.sdmconfiguration.SDMPreferences;
import com.sap.mobile.lib.sdmconnectivity.ISDMNetListener;
import com.sap.mobile.lib.sdmconnectivity.ISDMRequest;
import com.sap.mobile.lib.sdmconnectivity.ISDMRequestStateElement;
import com.sap.mobile.lib.sdmconnectivity.ISDMResponse;
import com.sap.mobile.lib.sdmconnectivity.SDMBaseRequest;
import com.sap.mobile.lib.sdmconnectivity.SDMConnectivityParameters;
import com.sap.mobile.lib.sdmconnectivity.SDMRequestManager;
import com.sap.mobile.lib.sdmparser.ISDMODataEntry;
import com.sap.mobile.lib.sdmparser.SDMParser;
import com.sap.mobile.lib.supportability.ISDMLogger;
import com.sap.mobile.lib.supportability.SDMLogger;
import com.sybase.mobile.lib.client.ODPClientConnection;
import com.sybase.mobile.lib.client.ODPException;
import com.sybase.mobile.lib.client.ODPUserManager;
import com.sybase.mobile.lib.client.ODPClientListeners.IODPPushNotificationListener;




public class OnlinePush extends AndroidTestCase implements ISDMNetListener,IODPPushNotificationListener {
	
	protected SDMLogger logger;
	protected SDMPreferences pref;
	protected SDMConnectivityParameters param;
	protected SDMRequestManager reqMan;
	public TestResult result;
	protected SDMParser parser;
	public Boolean verdict = null;
	private Object lockObject = new Object();
	public SDMBaseRequest br = new SDMBaseRequest();	
	protected SDMCache cache;
	private HttpResponse mResponse;
	public SDMBaseRequest getrequest = new SDMBaseRequest();	
	public SDMBaseRequest getSchema = new SDMBaseRequest();
	public SDMBaseRequest search = new SDMBaseRequest();
	public ISDMRequest deleterequest = new SDMBaseRequest();
	public ISDMRequest postrequest = new SDMBaseRequest();
	public ISDMRequest putrequest = new SDMBaseRequest();
	public ISDMODataEntry entry;
	
	private String ERRORMSG = "";
	protected String PUT_ID = "firstname ne AND";
	public String subscription_id = "";
	public int post_flag = 0;
	public OnlinePush(){
	}
	
		
	@Override
	protected void setUp() throws Exception {
		
	
		final ISDMNetListener listener;
		logger = new SDMLogger();
		pref = new SDMPreferences(mContext, logger);		
		pref.setStringPreference("CUSTOM_PREF_USERNAME", Helper.USERNAME);
		logger.setLogLevel(ISDMLogger.DEBUG);		
		param = new SDMConnectivityParameters();		
		param.setUserName(pref.getStringPreference("CUSTOM_PREF_USERNAME"));
		param.setUserPassword(Helper.PASSWORD);
		reqMan = new SDMRequestManager(logger, pref, param, 1);	
		parser = new SDMParser(pref, logger);
		pref.setIntPreference(ISDMPreferences.SDM_CONNECTIVITY_CONNTIMEOUT, 7000);
		cache = new SDMCache(pref, logger);
		cache.clear();	
			
		
		try {
			
			OnlinePush msr = new OnlinePush();
			
			
			ODPUserManager.initInstance(getContext(), Helper.APP_NAME);
			
			ODPUserManager lum = null;
			
			lum = ODPUserManager.getInstance();	
								
		    ODPClientConnection.clearServerVerificationKey();
			
			
			lum.setConnectionProfile(Helper.SEVERIP, Helper.SERVERPORT,Helper.COMPANYID);
			
			
		} catch (Exception e) {
			verdict = false;
			result.addFailure(this, new AssertionFailedError(e.getMessage()));
			result.endTest(this);
		}
		
	}
	
	@Override
	protected void tearDown() throws Exception {
		try {
			ODPUserManager lum = ODPUserManager.getInstance();
			//lum.deleteUser();
			
			pref.resetPreference(ISDMPreferences.SDM_CONNECTIVITY_HANDLER_CLASS_NAME);
			pref.removePreference("CUSTOM_PREF_USERNAME");
			cache.clear();
			reqMan.terminate();
		} catch (Exception e) {
			verdict = false;
			result.addFailure(this, new AssertionFailedError(e.getMessage()));
			result.endTest(this);
		}
	}
	
	@Override
	public void run(TestResult result) {
		try{
			setUp();	
			this.result = result;
			this.result.startTest(this);
			verdict = true;
			Onlinepush();
			if(verdict){
//				final NotificationManager nNM=(NotificationManager)mContext.getSystemService("notification");
//				GatewayReceivePush NL = new GatewayReceivePush(mContext, nNM);
				//Post_OData();
			}
			
			tearDown();
			this.result.endTest(this);
			}
			catch(Exception e){
			result.addFailure(this, new AssertionFailedError(e.getMessage()));
			result.endTest(this);
			return;
			}
	}
	
	@SuppressWarnings("static-access")
	public void Onlinepush(){
		
		
		try {
			ODPClientConnection.initInstance(mContext,Helper.APP_NAME);
			ODPClientConnection lm = null;
			lm = ODPClientConnection.getInstance();
						
			ODPUserManager lum = ODPUserManager.getInstance();
//			if(!lum.isUserRegistered())
//			lum.registerUser(Helper.USERNAME, Helper.SECURITY_CONFIG, Helper.PASSWORD, true);		
//			else
//			{
//			
//			lm.startClient();
//			}
			Log.i("Deepika","Push End Point:"+ Helper.PUSH_ENDPOINT);
			lm.registerForNativePush(this);			
			
//			Helper.serviceDocUrl = ODPAppSettings.getApplicationEndPoint();	
//			Helper.PUSH_ENDPOINT = ODPAppSettings.getPushEndPoint();
//			Log.i("Application Message", "Push End Point: "+Helper.PUSH_ENDPOINT);
			
			final NotificationManager nNM=(NotificationManager)mContext.getSystemService("notification");
			GatewayReceivePush NL = new GatewayReceivePush(mContext, nNM);
							
			//lm.doPushRegistration(NL);
			
			ODPClientConnection.registerForPayloadPush(NL);
			
			verdict = true;
		} catch (ODPException e) {
			
			verdict = false;
			result.addError(this, e);
			ERRORMSG = e.getErrorCode() + e.getMessage();			
			
		}
		
			
		if(!verdict){
			result.addFailure(this, new AssertionFailedError(ERRORMSG));
		}
	}
	
	

	public void Post_OData(){
	
		final ISDMNetListener listener = null;
		post_flag = 1;
	
		String PostURL = Helper.PUSH_ENDPOINT;	
		Log.i("Deepika","Push end poing " + Helper.PUSH_ENDPOINT);
		String postdata = "xyz";	
		postrequest.setRequestMethod(ISDMRequest.REQUEST_METHOD_POST);
		postrequest.setRequestUrl(PostURL);
		postrequest.setListener(this);
		
		Map<String, String> headers = new HashMap<String, String>(); 
		//headers.put("X-Requested-With", "XMLHttpRequest");
		headers.put("Authorization","Basic c3VwQWRtaW46czNwQWRtaW4=");
		headers.put("x-sup-gcm-collapsekey", "updates available");
		headers.put("x-sup-gcm-data", "hello siri");
		
		postrequest.setHeaders(headers);
	
	
		final byte[] theByteArray = postdata.getBytes();
		postrequest.setData(theByteArray);
	
		reqMan.makeRequest(postrequest);
	
		synchronized (lockObject) {
			try {
				lockObject.wait();
			} catch (InterruptedException e) {
			result.addError(this, e);
			}
		}
	
	
	}			
	
	public void onError(ISDMRequest aRequest, ISDMResponse aResponse,ISDMRequestStateElement aRequestStateElement) {
	//	HttpEntity entity = aResponse.getEntity();
	//	Log.i("Deepika","Failure POST"+ entity.toString());
	//	Log.i("Deepika","In error: " +aResponse.toString());
		result.addFailure(this, new AssertionFailedError(aRequestStateElement.getException().getMessage()));		
		verdict = false;
		synchronized (lockObject) {
			lockObject.notify();
		}
		Log.i("Deepika","POST failed");
		Log.i("Deepika", aRequestStateElement.getHttpResponse().toString());
	}
	
	
	
	public void onSuccess(ISDMRequest aRequest, ISDMResponse aResponse) {		
		synchronized (lockObject) {
			lockObject.notify();
		}
		Log.i("Deepika","POST successful");
		verdict = true;
	}


	@Override
	public int onGCMNotification(Hashtable hashValues) {
		// TODO Auto-generated method stub
		Log.i("Deepika","Inside GCMNotification");
		try {
			ODPClientConnection.getInstance().startClient();
		} catch (ODPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (!hashValues.isEmpty()) {
            Iterator it = hashValues.entrySet().iterator();

            while (it.hasNext()) {
                           Map.Entry entry = (Map.Entry) it.next();
                           System.out.println("key: " +entry.getKey()+"value: "+entry.getValue());
            }
    }
		return 0;
	}
	
	

}
