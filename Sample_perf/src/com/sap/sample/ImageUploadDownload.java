package com.sap.sample;
import com.sap.mobile.lib.sdmparser.ISDMODataServiceDocument;
import com.sap.mobile.lib.sdmparser.ISDMParser;
//import com.sybase.mo.MessagingClientLib;
import com.sybase.mobile.lib.client.ODPClientConnection;
import com.sybase.mobile.lib.client.ODPUserManager;
import com.sybase.mobile.lib.log.ODPLogger;

import android.app.Activity;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ImageUploadDownload extends Activity{
	  ListView list;
 	    public static final String TAG = "ExtendedExample";
 		public static boolean persistable = true;
 		public static String flag ="image";// "attach";//
 		
 		public static ISDMODataServiceDocument serviceDocument = null;
 		
 		public static String serviceDocument1 = null;
 		public static byte[] serviceDocument2 = null;
 		public static ISDMParser parser;
 		public String [] mStrings =null;
 		//public ODPLogger log = new ODPLogger();
 		public TextView Selection;
 		private String serviceDocPath = "https://ldcig8p.wdf.sap.corp:44318/sap/opu/odata/iwfnd/RMTSAMPLEFLIGHT/CarrierCollection('LH')/$value" ;
 	
     @SuppressWarnings("static-access")
 	@Override
     public void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         setContentView(R.layout.download);
         final AssetManager assetManager = getResources().getAssets(); 

 	
 		///////image download///////
 		
 		Button btnDown = (Button) findViewById(R.id.download);
 		btnDown.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (flag == "image")
					
		   		{
					//log.p("Started making Request for Image","Request Sent for Image");
					download sr=new download(getApplicationContext());
					
					
			   		serviceDocument2=sr.getServiceDocument(serviceDocPath);
					
					//log.p("Stopped making Request for Image","Response Received for Image");
		   			ImageView b=(ImageView)findViewById(R.id.icon);
		   			imageDownload(b);
		   			
		   		}
				
			}
		});
		
		
    Button btnUp = (Button)findViewById(R.id.upload);
    btnUp.setOnClickListener(new OnClickListener() {
		
		public void onClick(View v) {
			// TODO Auto-generated method stub
			//log.p("Started making Request for Image","Request Sent for Image");
			download sr1 = new download(getApplicationContext());	
	   		int responseCode =sr1.uploadImage(serviceDocPath, "/mnt/sdcard/data/app/100kb.jpg");
	   		MessageBox("Response Code :"+responseCode);
	   		//log.p("Stopped making Request for Image","Response Received for Image");
				Log.i("response", ""+responseCode);
		}
	});
  	
    
    Button btnUp1 = (Button)findViewById(R.id.upload1);
    btnUp1.setOnClickListener(new OnClickListener() {
		
		public void onClick(View v) {
			// TODO Auto-generated method stub
			//log.p("Started making Request for Download Image","Request Sent for Download Image");
			download sr1 = new download(getApplicationContext());	
	   		int responseCode =sr1.uploadImage(serviceDocPath, "/mnt/sdcard/data/app/picK.jpg");
	   		MessageBox("Request Sent :"+responseCode);
			Log.i("response", ""+responseCode);
			
			//log.p("Stopped making Request for Image Download","Response Received for Image Download");
		}
	});
   }
   


 	private void MessageBox(String message) {
		// TODO Auto-generated method stub
		Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
	}
     public static void imageDownload(ImageView b){
   		try
 		{
 			Bitmap image = BitmapFactory.decodeByteArray(serviceDocument2, 0,serviceDocument2.length);
 			b.setImageBitmap(image);
     	    b.setMaxHeight(100);
     	    b.setMaxWidth(100);
 			b.setEnabled(true);
 		}
 		catch(Exception e)
 		{
 			Log.i("AMBIKA", "Image Exception: "+e);
 		}
     
  }

}
