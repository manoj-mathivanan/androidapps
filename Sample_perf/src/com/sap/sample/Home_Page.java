package com.sap.sample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.view.View.OnClickListener;

public class Home_Page extends Activity {
	
	public void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);
        setTitle("Travel Management");
     
		ImageView btn_trvlAgency = (ImageView) findViewById(R.id.travelAgencies);
		ImageView btn_flghtDet = (ImageView) findViewById(R.id.flightDetails);
		Button btnImageView = (Button)findViewById(R.id.ImageView);
		
		btn_trvlAgency.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
			Intent travelAgency = new Intent(Home_Page.this, TravelAgencyList.class);
			startActivity(travelAgency);
			}
		});
		
		btn_flghtDet.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
			Intent booking_list = new Intent(Home_Page.this, all_steps.class);
			startActivity(booking_list);	
	//			Intent crAgency = new Intent(Home_Page.this, CreateAgency.class);
	//			startActivity(crAgency);
			}
		});
	
			
		btnImageView.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent ImageUpDn = new Intent(Home_Page.this, ImageUploadDownload.class);
				startActivity(ImageUpDn);
			}
		});
	}
}
