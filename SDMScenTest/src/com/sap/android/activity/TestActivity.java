package com.sap.android.activity;

import java.util.Enumeration;

import junit.framework.TestResult;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.test.AndroidTestCase;
import android.test.AndroidTestRunner;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.sap.android.sdmscen.test.Helper;



public class TestActivity extends Activity {
	
	public static TextView tx;
	public static AndroidTestRunner atr = new AndroidTestRunner();
	public EditText Server,Port,CompanyId;
	public Button btnSave;
	public static CheckBox https;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(com.sap.android.sdmscen.test.R.layout.main);
		tx =(TextView) findViewById(com.sap.android.sdmscen.test.R.id.textView1); 
		tx.setText("--------Scenario Tesing Excution----------\n");
		Server = (EditText) (findViewById(com.sap.android.sdmscen.test.R.id.txtServer));
		
		Port = (EditText) (findViewById(com.sap.android.sdmscen.test.R.id.txtPort));
		
		CompanyId = (EditText) (findViewById(com.sap.android.sdmscen.test.R.id.txtCompanyId));
		
		
		atr.setContext(this);
		
	
		
		
	}
	
	public void Save_OnClick(View v)
	{
		
		Helper.SERVERPORT = Integer.parseInt(Port.getText().toString());
		Helper.SEVERIP = Server.getText().toString();
		Helper.COMPANYID = CompanyId.getText().toString();
		
		Log.i("server:",Helper.SEVERIP);
		Intent scenarios = new Intent(TestActivity.this,TestScenarios.class);
		startActivity(scenarios);		
	}
	public static void runTest(AndroidTestCase atc){
		atr.setTest(atc);		
		atr.runTest();
		TestResult tr = atr.getTestResult();
		tx.append("-------------------------------------\n");
		tx.append(atc.getClass().getSimpleName()+" verdict:"+tr.wasSuccessful()+"\n");
		tx.append(atc.getClass().getSimpleName()+" error: "+tr.errorCount()+"\n" );
		tx.append(atc.getClass().getSimpleName()+" fail:"+tr.failureCount()+"\n" );
		
		
		Enumeration e = tr.failures();
		while(e.hasMoreElements()){
			tx.append(atc.getName()+" failers:"+e.nextElement().toString()+"\n");
		}
		tx.append("-------------------------------------\n");
		
	}
			

}
