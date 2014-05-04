package com.sap.manoj.smp_libs;

import java.util.HashMap;

import android.app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;



import com.sap.mobile.lib.configuration.Constants;
import com.sap.mobile.lib.configuration.IPreferences;
import com.sap.mobile.lib.configuration.Preferences;
import com.sap.mobile.lib.configuration.PreferencesException;
import com.sap.mobile.lib.request.ConnectivityParameters;
import com.sap.mobile.lib.request.RequestManager;
import com.sap.mobile.lib.supportability.ILogger;
import com.sap.mobile.lib.supportability.Logger;
import com.sap.smp.rest.*;

public class SMP_perfappActivity extends Activity {
	String UN,PWD;
	public long start,stop;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        final EditText IP_e = (EditText)findViewById(R.id.IP);
    	final EditText PORT_e = (EditText)findViewById(R.id.Port);
    	final EditText APPNAME_e = (EditText)findViewById(R.id.App_Name);
    	final EditText secconfig_e = (EditText)findViewById(R.id.sec_config);
    	final EditText CAPTCHA_e = (EditText)findViewById(R.id.captchaText);
    	final EditText auth = (EditText)findViewById(R.id.authtext);
    	final Button Onboard_b = (Button)findViewById(R.id.Onboard);
    	final Button Use_appid = (Button)findViewById(R.id.useappid);
    	final CheckBox urlre_write = (CheckBox)findViewById(R.id.checkBox1);
    	urlre_write.setChecked(true);
    	
       
        Onboard_b.setOnClickListener(new OnClickListener() {           
	    	@Override 
	        public void onClick(View v) { 
	    		//helper.smpurl=IP_e.getText().toString();
	    		//helper.PORT=PORT_e.getText().toString();
	    		//helper.appId=APPNAME_e.getText().toString();
	    		//helper.AppconnID=APPID_e.getText().toString();
	    		//helper.CAPTCHA=CAPTCHA_e.getText().toString();
	    		//helper.urlrewrite = urlre_write.isChecked();
	    		//helper.basic_auth = auth.getText().toString();
	    		//helper.smpurl = helper.IP;
	    		//helper.appId = helper.APPNAME;
	    		//helper.secCon = secconfig_e.getText().toString();
	    		onboard_user();
	    		//onboard_user();
	    		//APPID_e.setText(helper.AppconnID);	    		
	        }
	    }); 
        Use_appid.setOnClickListener(new OnClickListener() {          
	    	@Override 
	        public void onClick(View v) { 
	    	//	helper.IP=IP_e.getText().toString();
	    	//	helper.PORT=PORT_e.getText().toString();
	    	//	helper.APPNAME=APPNAME_e.getText().toString();
	    	//	helper.APPID=APPID_e.getText().toString();
	    	//	helper.CAPTCHA=CAPTCHA_e.getText().toString();
	    	//	helper.urlrewrite = urlre_write.isChecked();
	    	//	helper.basic_auth = auth.getText().toString();
	    		//helper.smpurl = helper.IP;
	    		//helper.appId = helper.APPNAME;
	    		Intent goToNextActivity = new Intent(SMP_perfappActivity.this,auto.class);
				startActivity(goToNextActivity);
	        }
	    }); 
    }
	public void MessageBox(String message)
	{
		Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
	}

	private void onboard_user() {
		ILogger logger = new Logger();
        IPreferences pref = new Preferences(getBaseContext(), logger);
        try {
			pref.setStringPreference(IPreferences.CONNECTIVITY_HANDLER_CLASS_NAME, Constants.HTTP_HANDLER_CLASS);
	        pref.setBooleanPreference(IPreferences.PERSISTENCE_SECUREMODE, false);       
		} catch (PreferencesException e2) {
			e2.printStackTrace();
		}
        ConnectivityParameters param = new ConnectivityParameters();                        
        param.setUserName(helper.userName);
        param.setUserPassword(helper.password);  
        RequestManager requestManager = new RequestManager(logger, pref, param, 1);
        ClientConnection cc = new ClientConnection(getBaseContext(),helper.appId,helper.domain,helper.secCon,requestManager);
		cc.setConnectionProfile(helper.smpurl);
		//cc.setConnectionProfile(true,"10.68.139.106","5001","ias_relay_server/client/rs_client.dll","pwdf3177");

		//cc.setApplicationConnectionID("PerfAPPCID");
		Log.d("Performance","Start Onboarding");
		start=System.currentTimeMillis();
		UserManager usm = new UserManager(cc);
		if(!usm.isUserRegistered())
		{ 
			try {
				boolean reg = usm.registerUser(true);
				if(reg == true){
					Log.d("Performance","End Onboarding");
					stop=System.currentTimeMillis();
					Log.d("libs timing", "Onboard = "+(stop-start));

					//excel.saveExcelFile(getApplicationContext(),"/out.xls","onboard",(stop-start),0,0);
					Log.i("tag","user registration status"+ reg);
					helper.AppconnID= usm.getApplicationConnectionId();
					//helper.AppconnID="PerfAPPCID";
					Log.d("Performance",helper.AppconnID);
					AppSettings appset = new AppSettings(cc);
					HashMap<String, String> fullConfig1 = new HashMap<String, String>();
					Log.i("tag", "updating AndroidGcmRegistrationId");
					fullConfig1.put("d:AndroidGcmRegistrationId", "APA91bG8oMTlWtn-Yh6fKgyS0lP0piDXw1xWxM2wBLV7B4o9Jfm2gJZux9ao0PkhSCVlrjBHsmA1uXUy-N29VCGzthxFvvFlkuGNg5tI-rnKjZrJwm1orLIhiLkRdZ4CAWNwiFUlARR9ACGZUWi2wSwOY8glHYZr7A");
					appset.setConfigProperty(fullConfig1);
					Log.i("tag", "AndroidGcmRegistrationId " + (String)appset.getConfigProperty("d:AndroidGcmRegistrationId"));
					helper.serviceDocUrl = appset.getApplicationEndPoint();
					helper.pushendpt= appset.getPushEndPoint();
					Intent goToNextActivity = new Intent(SMP_perfappActivity.this,auto.class);
					startActivity(goToNextActivity);
				}
				else
				{
					Log.d("Performance","Error Onboarding");
					Toast.makeText(this,"error while onboarding,check logcat",Toast.LENGTH_SHORT).show();	
				}
			} catch (SMPException e) {
				Log.i("tag","in catch of registration" + e.getErrorCode()+e.getMessage());			
				e.printStackTrace();
			}
		}

	}
		
}
