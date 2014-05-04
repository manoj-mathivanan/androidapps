package com.sap.android.sdmscen.test;

import junit.framework.AssertionFailedError;
import junit.framework.TestResult;
import android.test.AndroidTestCase;
import android.util.Log;

import com.sybase.mo.MessagingClientException;
import com.sybase.mo.MessagingClientLib;
import com.sybase.mobile.lib.client.ODPAppSettings;
import com.sybase.mobile.lib.client.ODPClientConnection;
import com.sybase.mobile.lib.client.ODPException;
import com.sybase.mobile.lib.client.ODPUserManager;



public class SSOSyncRegistration extends AndroidTestCase{
	

	
	protected TestResult result;
	private String ERRORMSG = "";
	protected Boolean verdict = null;
	private Object lockObject = new Object();
	
	public SSOSyncRegistration(){
	}
	
		
	@Override
	protected void setUp() throws Exception {
		
			
		try {
			SSOSyncRegistration msr = new SSOSyncRegistration();
			
			
			ODPUserManager.initInstance(getContext(), Helper.APP_NAME);

			ODPUserManager lum = null;
			
			lum = ODPUserManager.getInstance();	
		
			
								
			//i071686
			
			MessagingClientLib.getInstance().clearServerVerificationKey();			
			
			lum.setConnectionProfile(Helper.SEVERIP, Helper.SERVERPORT,Helper.COMPANYID);
			
		
			
		} catch (MessagingClientException e) {
			Log.i("Auto", "Automation message: " +e.getErrorCode() +e.getMessage());
		} catch (Exception e) {
			Log.i("Auto", "Automation message: " +e.getMessage());
		}
		
	}
	
	@Override
	protected void tearDown() throws Exception {
		try {
			ODPUserManager lum = ODPUserManager.getInstance();
			
			if (lum.isUserRegistered()) {
				
			lum.deleteUser();
		
				
			}			
			
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	@Override
	public void run(TestResult result) {
		try{
			setUp();
			this.result = result;
			this.result.startTest(this);
			SSOSyncRegistration();
		
		/*	if(verdict){
				RequestResponse rr = new RequestResponse();
				rr.setContext(getContext());
				rr.isCertRegistration = false;
				
				rr.run(result);
				
			}
			
			tearDown();*/
			this.result.endTest(this);
			}
			catch(Exception e){
			result.addFailure(this, new AssertionFailedError(e.getMessage()));
			result.endTest(this);
			return;
			}
	}
	
	public void SSOSyncRegistration(){
		
		
		try {
			
			ODPUserManager lum = ODPUserManager.getInstance();
			
			if(!lum.isUserRegistered())
			lum.registerUser(Helper.USERNAME, Helper.SECURITY_CONFIG, Helper.PASSWORD, true);	
		    
			else
			{
				ODPClientConnection.initInstance(mContext, Helper.APP_NAME);
				ODPClientConnection.getInstance().startClient();
			}	
			ODPAppSettings las = new ODPAppSettings();
			
			Log.i("Test", "After Registration: "+ODPAppSettings.IsServerKeyProvisioned());
			
			Helper.serviceDocUrl = ODPAppSettings.getApplicationEndPoint();	
			Helper.PUSH_ENDPOINT = ODPAppSettings.getPushEndPoint();
			
			verdict = true;
		} catch (ODPException e) {
			// TODO Auto-generated catch block
			verdict = false;
			result.addError(this, e);
			//ERRORMSG = e.getErrorCode() + e.getMessage();		
			ERRORMSG = e.toString();
			
		}
		
			
		if(!verdict){
			result.addFailure(this, new AssertionFailedError(ERRORMSG));
		}
	}
	


}
