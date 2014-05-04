package com.sap.android.activity;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.test.AndroidTestCase;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.sap.android.sdmscen.test.Helper;

public class TestScenarios extends ListActivity {
	
	public ListView scenariosList = null;
	private String[] scenarios = {
								  "SSOSyncRegistration",
								   "OnlinePush",
								   						   
								 };
	
	Button btnRun;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Helper.MyContext = this;
		setListAdapter(new ArrayAdapter<String>(TestScenarios.this, android.R.layout.simple_list_item_multiple_choice,scenarios));
		this.scenariosList = getListView();
		
		scenariosList.setItemsCanFocus(false);
		scenariosList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		
		setContentView(com.sap.android.sdmscen.test.R.layout.testscenarios);
		btnRun = (Button) findViewById(com.sap.android.sdmscen.test.R.id.btnRun);
	
		btnRun.setOnClickListener(new OnClickListener() {
					@Override
			public void onClick(View arg0) {
						
				// TODO Auto-generated method stub
				String savedItems = "";
				scenariosList = getListView();
				for(int i=0; i<scenariosList.getAdapter().getCount(); i++)
				{
					
					if(scenariosList.isItemChecked(i))
					{
						
						try {
							Class<?> clas = Class.forName("com.sap.android.sdmscen.test."+scenariosList.getItemAtPosition(i).toString());
							AndroidTestCase tc = (AndroidTestCase) clas.newInstance();
							TestActivity.runTest(tc);
						} catch (ClassNotFoundException e) {
													
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							
							e.printStackTrace();
						} catch (InstantiationException e) {
							
							e.printStackTrace();
						}
					}
				}
				
			    AlertDialog ad = new AlertDialog.Builder(TestScenarios.this).create();  
			    ad.setCancelable(false); // This blocks the 'BACK' button  
			    ad.setMessage("Execution completed");  
			    ad.setButton("OK", new DialogInterface.OnClickListener() {  
			        @Override  
			        public void onClick(DialogInterface dialog, int which) {  
			            dialog.dismiss();                      
			        }  
			    });  
			    ad.show();  
			}

		
		});
				
	}
	
	public void GetNewPassword(View v)
	{
		// TODO Auto-generated method stub
		Log.i("Deepika","Inside update password");
		final EditText txtVaultPassword = new EditText(Helper.MyContext);
		Helper.PasswordPolicy = true;
		AlertDialog ad = new AlertDialog.Builder(Helper.MyContext).create();  
		ad.setTitle("Enter Vault Password"); 
		ad.setCancelable(false);
		ad.setView(txtVaultPassword);
		ad.setButton("Save", new DialogInterface.OnClickListener() 
		{  
			@Override  
			public void onClick(DialogInterface dialog, int which)
			{  
				String value = txtVaultPassword.getText().toString();
				Helper.OLD_VAULT_PASSWORD = Helper.VAULT_PASSWORD;
				Helper.VAULT_PASSWORD = value;
				Log.i("Deepika","Value is "+value);
				dialog.dismiss(); 
				
			}  
		}); 
		ad.show();  
	}
	
}