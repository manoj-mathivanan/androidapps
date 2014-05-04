package com.sap.android.sdmscen.test;

import java.io.IOException;

import junit.framework.AssertionFailedError;
import junit.framework.Test;
import junit.framework.TestResult;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import android.app.NotificationManager;
import android.content.Context;
import android.net.ParseException;
import android.util.Log;

import com.sap.mobile.lib.sdmconnectivity.ISDMNetListener;
import com.sap.mobile.lib.sdmconnectivity.ISDMRequest;
import com.sap.mobile.lib.sdmconnectivity.ISDMRequestStateElement;
import com.sap.mobile.lib.sdmconnectivity.ISDMResponse;
import com.sybase.mobile.lib.client.ODPClientConnection;
import com.sybase.mobile.lib.client.ODPException;
import com.sybase.mobile.lib.client.ODPUserManager;


public class GatewayReceivePush implements ISDMNetListener, Test {
	
	public Context context;
	OnlinePush op = new OnlinePush();
	public NotificationManager mNM;
	 public  GatewayReceivePush(Context context,NotificationManager nmh){
		 this.context=context;
		 this.mNM=nmh;
		 
	 }
			
	// ISDMNetListener implementation
	
	public void onSuccess(ISDMRequest aRequest, ISDMResponse aResponse) {
		op.verdict = true;
		Log.i("Deepika", "PUSH SUCCESSFULL");
		HttpEntity entity = aResponse.getEntity();
		try {
		Log.i("Deepika","Push response:"+ EntityUtils.toString(entity));
			try {
				ODPClientConnection.getInstance().stopClient();
			} catch (ODPException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		

			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		try {
			op.tearDown();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		op.result.endTest(this);
	}

	// ISDMNetListener implementation
	
	public void onError(ISDMRequest aRequest, ISDMResponse aResponse,ISDMRequestStateElement aRequestStateElement) {
		op.verdict = false;
		op.result.addFailure((Test) this, new AssertionFailedError("PUSH Failed"));
		Log.i("Application Message", "PUSH ERROR");
		try {
			op.tearDown();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		op.result.endTest(this);
	}

	@Override
	public int countTestCases() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void run(TestResult result) {
		// TODO Auto-generated method stub
		
	}

	
}
