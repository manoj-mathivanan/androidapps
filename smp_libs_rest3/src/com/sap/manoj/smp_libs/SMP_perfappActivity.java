package com.sap.manoj.smp_libs;

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

import com.sap.mobile.lib.sdmconfiguration.ISDMPreferences;
import com.sap.mobile.lib.sdmconfiguration.SDMConstants;
import com.sap.mobile.lib.sdmconfiguration.SDMPreferences;
import com.sap.mobile.lib.sdmconfiguration.SDMPreferencesException;
import com.sap.mobile.lib.sdmconnectivity.SDMConnectivityParameters;
import com.sap.mobile.lib.sdmconnectivity.SDMRequestManager;
import com.sap.mobile.lib.supportability.ISDMLogger;
import com.sap.mobile.lib.supportability.SDMLogger;

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
	    		helper.smpurl=IP_e.getText().toString();
	    		//helper.PORT=PORT_e.getText().toString();
	    		helper.appId=APPNAME_e.getText().toString();
	    		//helper.AppconnID=APPID_e.getText().toString();
	    		//helper.CAPTCHA=CAPTCHA_e.getText().toString();
	    		//helper.urlrewrite = urlre_write.isChecked();
	    		//helper.basic_auth = auth.getText().toString();
	    		//helper.smpurl = helper.IP;
	    		//helper.appId = helper.APPNAME;
	    		helper.secCon = secconfig_e.getText().toString();
	    		onboard_user();
	    		onboard_user();
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
		SDMLogger logger = new SDMLogger();
        ISDMPreferences pref = new SDMPreferences(getBaseContext(), logger);
        try {
			pref.setStringPreference(ISDMPreferences.SDM_CONNECTIVITY_HANDLER_CLASS_NAME, SDMConstants.SDM_HTTP_HANDLER_CLASS);
	        pref.setBooleanPreference(ISDMPreferences.SDM_PERSISTENCE_SECUREMODE, false);       
		} catch (SDMPreferencesException e2) {
			e2.printStackTrace();
		}
        SDMConnectivityParameters param = new SDMConnectivityParameters();                        
        param.setUserName(helper.userName);
        param.setUserPassword(helper.password);  
        SDMRequestManager requestManager = new SDMRequestManager(logger, pref, param, 1);
        ClientConnection cc = new ClientConnection(getBaseContext(),helper.appId,helper.domain,helper.secCon,requestManager);
		cc.setConnectionProfile(helper.smpurl);
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
					AppSettings appset = new AppSettings(cc);
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
