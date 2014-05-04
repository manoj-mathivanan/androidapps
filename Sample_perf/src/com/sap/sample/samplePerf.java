package com.sap.sample;

import java.net.ServerSocket;
import java.util.ArrayList;
import com.sybase.mobile.lib.client.ODPAppSettings;
import com.sybase.mobile.lib.client.ODPClientConnection;
import com.sybase.mobile.lib.client.ODPException;
import com.sybase.mobile.lib.client.ODPUserManager;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class samplePerf extends Activity {
    /** Called when the activity is first created. */
	Button Register;
	Button DeRegister;
	public static int flag=0;
	protected static  EditText SERVER_IP,Comp_Id,PortNo,Security_config;
	//protected static String Username = "supuser2",Password = "s3puser",secc="SSO",appname="com.sap.NewFlight";
	
	/*values to be changed*/
	protected static String Username = "perfandroid",Password = "perfandroid",secc="SSO",appname="com.sap.travelapp";
	
	
	public static ArrayList<CharSequence> updated;
	protected static String serviceDocUrl = "";	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        SERVER_IP = (EditText) findViewById(R.id.editText1);
        Comp_Id = (EditText) findViewById(R.id.editText2);
        PortNo = (EditText) findViewById(R.id.editText5);
        Security_config = (EditText) findViewById(R.id.editText6);
      
        Register =(Button)findViewById(R.id.button1);
        Register.setOnClickListener(new OnClickListener() {
			
//	@Override
	public void onClick(View arg0) {
	// TODO Auto-generated method stub
			
	    try {
	    	
	    	
			ODPUserManager.initInstance(getApplicationContext(), appname);
			ODPUserManager odpum = null;
			odpum = ODPUserManager.getInstance();
			
			if (odpum.isUserRegistered())
			{
				Log.i("warning", "Already Registered");
			}
			else
			{
				Log.i("warning","Inside registration else");
				odpum.setConnectionProfile("10.68.139.106",5001,"pwdf3177");
				//odpum.setConnectionProfile("10.66.180.153",5001,"0");
				odpum.registerUser(Username, secc, Password, true);
                ODPClientConnection.initInstance(getApplicationContext(),appname);
                ODPClientConnection lm = null;
				lm = ODPClientConnection.getInstance();
				lm.startClient();
			//	odpum.registerUser("abc", "123", true);
				final NotificationManager nNM=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
				PushHandeler NL = new PushHandeler(getApplicationContext(), nNM);
				ODPClientConnection.registerForPayloadPush(NL);
			}
			
			ODPAppSettings settings = new ODPAppSettings();
			serviceDocUrl = settings.getApplicationEndPoint();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.i("warning",e.toString());
		}
		
		Intent home_page = new Intent(samplePerf.this, Home_Page.class);
		startActivity(home_page);
			}
		});
        
        DeRegister = (Button) findViewById(R.id.button2);
        DeRegister.setOnClickListener(new OnClickListener() {
			
//			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					ODPUserManager.initInstance(getApplicationContext(), appname);
					ODPUserManager odpUserMgnr = ODPUserManager.getInstance();
					
					if(odpUserMgnr.isUserRegistered()){
						MessageBox("Existing connection settings will be deregistered");
						odpUserMgnr.deleteUser();
						Log.i("Exception", "Deregister User: ");
					}
				} catch (ODPException e) {
					// TODO Auto-generated catch block
					Log.i("Exception", "Deregister User: "+e.getMessage());
				}
				
			}
		});
    }
	private void MessageBox(String message) {
		// TODO Auto-generated method stub
		Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
	}
}