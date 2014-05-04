package com.sap.manoj.smp_libs;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.util.EntityUtils;

import com.sap.mobile.lib.sdmcache.SDMCache;
import com.sap.mobile.lib.sdmconfiguration.ISDMPreferences;
import com.sap.mobile.lib.sdmconfiguration.SDMConstants;
import com.sap.mobile.lib.sdmconfiguration.SDMPreferences;
import com.sap.mobile.lib.sdmconnectivity.ISDMNetListener;
import com.sap.mobile.lib.sdmconnectivity.ISDMRequest;
import com.sap.mobile.lib.sdmconnectivity.ISDMRequestStateElement;
import com.sap.mobile.lib.sdmconnectivity.ISDMResponse;
import com.sap.mobile.lib.sdmconnectivity.SDMBaseRequest;
import com.sap.mobile.lib.sdmconnectivity.SDMConnectivityParameters;
import com.sap.mobile.lib.sdmconnectivity.SDMRequestManager;
import com.sap.mobile.lib.sdmparser.ISDMODataCollection;
import com.sap.mobile.lib.sdmparser.ISDMODataEntry;
import com.sap.mobile.lib.sdmparser.ISDMODataSchema;
import com.sap.mobile.lib.sdmparser.ISDMODataServiceDocument;
import com.sap.mobile.lib.sdmparser.SDMParser;
import com.sap.mobile.lib.sdmpersistence.SDMPersistence;
import com.sap.mobile.lib.supportability.ISDMLogger;
import com.sap.mobile.lib.supportability.SDMLogger;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class ImageUploadDownload extends Activity implements ISDMNetListener{
	public static byte[] serviceDocument1 = null;
 	public static byte[] serviceDocument2 = null;
 	public long start,stop;
	public	String responseText = null;
	public String UN,PWD;
	protected  String[] Collections;
	public String schema=null;
	protected SDMPersistence perst;
	public String collections=null;
	public static ISDMODataSchema schema1;
	protected SDMParser parser;
	protected SDMCache cache;
	public static ISDMODataServiceDocument serviceDoc;
	public static List<ISDMODataEntry> entries;
	public static List<ISDMODataCollection> collectionsList;
	String collectionId = "TravelagencyCollection";
	public static ArrayList<CharSequence> updated;
	protected ArrayAdapter<CharSequence> adapter;
	protected SDMLogger logger;
	protected SDMPreferences pref;
	protected SDMRequestManager reqMan;
	private Object lockObject = new Object();
	public ISDMRequest getrequest = new SDMBaseRequest();
	public ISDMRequest addrequest = new SDMBaseRequest();
	public ISDMRequest deleterequest = new SDMBaseRequest();
	public ISDMRequest updaterequest = new SDMBaseRequest();
	public SDMBaseRequest getSchema = new SDMBaseRequest();
	protected static SDMBaseRequest getCollec = new SDMBaseRequest();
	public SDMConnectivityParameters param = new SDMConnectivityParameters();
 	
 	@Override
     public void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         setContentView(R.layout.download);
         
         Button btnDownLoad = (Button)findViewById(R.id.download1);
         btnDownLoad.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View v) {
			   	getServiceDocument();
		   		ImageView b=(ImageView)findViewById(R.id.icon);
		   		imageDownload(b);
			}
		});
         
         Button btnupload = (Button)findViewById(R.id.upload);
         btnupload.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				uploadImage("/mnt/sdcard/data/app/picK.jpg");
		   		//MessageBox("Response Code :"+responseCode+" Upload executed");
			}
		});
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
 			e.printStackTrace();
 		}
  }
    
	public void MessageBox(String message) {
		Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
	}
    
	public void getServiceDocument() {
		byte[] imageData;
		try{
		
			logger = new SDMLogger();
	    	pref = new SDMPreferences(this.getApplicationContext() , logger);
	    	pref.setIntPreference(ISDMPreferences.SDM_CONNECTIVITY_CONNTIMEOUT, 75000);
	    	pref.setIntPreference(ISDMPreferences.SDM_CONNECTIVITY_SCONNTIMEOUT, 75000);
	    	pref.setStringPreference(ISDMPreferences.SDM_CONNECTIVITY_HANDLER_CLASS_NAME, SDMConstants.SDM_HTTP_HANDLER_CLASS);
	    	pref.setBooleanPreference(ISDMPreferences.SDM_PERSISTENCE_SECUREMODE, false);
	    	logger.setLogLevel(ISDMLogger.DEBUG);
	    	param = new SDMConnectivityParameters(); 
	    	param.setUserName(helper.userName);                    // username for all other onboardings except CERT.
	    	UN= helper.userName;
	        param.setUserPassword(helper.password);                // password for all other onboardings except CERT.	
	        PWD = helper.password;
			
	        param.enableXsrf(true);
	    	reqMan = new SDMRequestManager(logger, pref, param, 1); 
	    	parser = new SDMParser(pref, logger);
			cache = new SDMCache(pref, logger);
			//cache.clear();	
	   	
	    	//get service doc
	    	getrequest.setListener(ImageUploadDownload.this);
	    	getrequest.setRequestUrl(helper.serviceDocUrl+"CarrierCollection('LH')/$value");
	    	getrequest.setRequestMethod(SDMBaseRequest.REQUEST_METHOD_GET);    	
	   	Map<String, String> headers = new HashMap<String, String>();
	   	headers.put("X-SUP-APPCID",helper.AppconnID);
	   	getrequest.setHeaders(headers);
	   	Log.d("Performance","Start executing download Image");
	   	start = System.currentTimeMillis();
	   	reqMan.makeRequest(getrequest);
			synchronized (lockObject) {
				try {
					lockObject.wait();
				} catch (InterruptedException e) {
					Log.i("tag","in catch of get service doc"+e);
				}
			
		}
		}catch(Exception e){
			Log.d("error",e.getMessage());
		}
		
		//return serviceDocument1;
	}
	
	public void uploadImage(String filePath) {
		int code=0;
		byte[] data =  null;    
	    try {    	
	  	   data = readFile(filePath);
	  	} catch (IOException e) {
	  		e.printStackTrace();
	  	}
	    	    try{
	    	
			logger = new SDMLogger();
	    	pref = new SDMPreferences(this.getApplicationContext() , logger);
	    	pref.setIntPreference(ISDMPreferences.SDM_CONNECTIVITY_CONNTIMEOUT, 75000);
	    	pref.setIntPreference(ISDMPreferences.SDM_CONNECTIVITY_SCONNTIMEOUT, 75000);
	    	pref.setStringPreference(ISDMPreferences.SDM_CONNECTIVITY_HANDLER_CLASS_NAME, SDMConstants.SDM_HTTP_HANDLER_CLASS);
	    	pref.setBooleanPreference(ISDMPreferences.SDM_PERSISTENCE_SECUREMODE, false);
	    	logger.setLogLevel(ISDMLogger.DEBUG);
	    	param = new SDMConnectivityParameters(); 
	    	param.setUserName(helper.userName);                    // username for all other onboardings except CERT.
	    	UN= helper.userName;
	        param.setUserPassword(helper.password);                // password for all other onboardings except CERT.	
	        PWD = helper.password;
			
	        //param.enableXsrf(true);
	    	reqMan = new SDMRequestManager(logger, pref, param, 1); 
	    	parser = new SDMParser(pref, logger);
			cache = new SDMCache(pref, logger);
			cache.clear();	
	   	
	    	//get service doc
	    	updaterequest.setListener(ImageUploadDownload.this);
	    	updaterequest.setRequestUrl(helper.serviceDocUrl+"CarrierCollection('LH')/$value");
	    	updaterequest.setRequestMethod(SDMBaseRequest.REQUEST_METHOD_PUT);    	
	   	Map<String, String> headers = new HashMap<String, String>();
	   	headers.put("X-SUP-APPCID",helper.AppconnID);
	   	headers.put("Content-Type", "image/jpeg");
	   	headers.put("X-Requested-With", "XMLHttpRequest");
	   	updaterequest.setHeaders(headers);
	   	ByteArrayEntity entity=new ByteArrayEntity(data);
	    ((SDMBaseRequest) updaterequest).setData(data);
	    Log.d("Performance","Start executing upload Image");
	    start = System.currentTimeMillis();
	   	reqMan.makeRequest(updaterequest);
			synchronized (lockObject) {
				try {
					lockObject.wait();
				} catch (InterruptedException e) {
					Log.i("tag","in catch of get service doc"+e);
				}
			}
	    	
	    }catch(Exception e){
	    	Log.d("error",e.getMessage());
	    }
	    
			
	}
	
	public static byte[] readFile (String file) throws IOException { 
	       return readFile(new File(file)); 
	   } 

	   public static byte[] readFile (File file) throws IOException { 
	       // Open file 
	       RandomAccessFile f = new RandomAccessFile(file, "r"); 

	       try { 
	           // Get and check length 
	           long longlength = f.length(); 
	           int length = (int) longlength; 
	           if (length != longlength) throw new IOException("File size >= 2 GB"); 

	           // Read file and return data 
	           byte[] data = new byte[length]; 
	           f.readFully(data); 
	           return data; 
	       } 
	       finally { 
	           f.close(); 
	       } 
	   }

	@Override
	public void onError(ISDMRequest arg0, ISDMResponse arg1,
			ISDMRequestStateElement arg2) {
		// TODO Auto-generated method stub
		synchronized (lockObject) {
			lockObject.notify();
		}
		//MessageBox(arg1.getStatusLine().toString());
		Log.i("tag","For unsuccessful" + arg1.getStatusLine());
		synchronized (lockObject) {
			lockObject.notify();
		}
	}

	@Override
	public void onSuccess(ISDMRequest arg0, ISDMResponse arg1) {
		// TODO Auto-generated method stub
		//MessageBox(arg1.getStatusLine().toString());
		HttpEntity responseEntity = arg1.getEntity();
		//Log.i("tag","For successful GET"+responseEntity.toString());
		if( arg0.equals(getrequest)){
			try {
				
				Log.i("tag","For successful GET" + arg1.getStatusLine());
				
				serviceDocument2 = EntityUtils.toByteArray(responseEntity);
				Log.d("Performance","End executing download Image");
				//Log.i("tag","For successful GET");
				stop = System.currentTimeMillis();
				Log.d("libs timing", "Download = "+(stop-start));
				//excel.saveExcelFile(getApplicationContext(),"/out.xls","download",(stop-start),0,0);
				synchronized (lockObject) {
					lockObject.notify();
				}
			} catch (Exception e){
				synchronized (lockObject) {
					lockObject.notify();
				}
			}
		}	
		if( arg0.equals(updaterequest)){
			try {
				Log.i("tag","For successful UPDATE" + arg1.getStatusLine());
				
				Log.d("Performance","End executing upload Image");
	 
				//Log.i("tag","For successful update");
				stop = System.currentTimeMillis();
				Log.d("libs timing", "Upload = "+(stop-start));
				//excel.saveExcelFile(getApplicationContext(),"/out.xls","upload",(stop-start),0,0);
				synchronized (lockObject) {
					lockObject.notify();
				}
			} catch (Exception e){
				synchronized (lockObject) {
					lockObject.notify();
				}
			}
		}
	}
}


