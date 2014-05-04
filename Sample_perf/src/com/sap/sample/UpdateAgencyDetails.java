package com.sap.sample;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import com.sap.mobile.lib.sdmconfiguration.ISDMPreferences;
import com.sap.mobile.lib.sdmconfiguration.SDMPreferences;
import com.sap.mobile.lib.sdmconfiguration.SDMPreferencesException;
import com.sap.mobile.lib.sdmconnectivity.ISDMNetListener;
import com.sap.mobile.lib.sdmconnectivity.ISDMRequest;
import com.sap.mobile.lib.sdmconnectivity.ISDMRequestStateElement;
import com.sap.mobile.lib.sdmconnectivity.ISDMResponse;
import com.sap.mobile.lib.sdmconnectivity.SDMBaseRequest;
import com.sap.mobile.lib.sdmconnectivity.SDMConnectivityParameters;
import com.sap.mobile.lib.sdmconnectivity.SDMRequestManager;
import com.sap.mobile.lib.sdmparser.ISDMODataServiceDocument;
import com.sybase.mobile.lib.log.ODPLogger;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ParseException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.view.View.OnClickListener;

public class UpdateAgencyDetails extends Activity implements ISDMNetListener {

	private static final String TAG = "ListElements";
	private String serviceDocPath =null;// "http://vmw3815.wdf.sap.corp:50009/sap/opu/sdata/iwfnd/RMTSAMPLEFLIGHT/TravelagencyCollection";//"http://usciu3e.wdf.sap.corp:50026/sap/opu/sdata/iwcnt/SalesOrderLookUp/";
	public static ISDMODataServiceDocument serviceDocument = null;
	public static String serviceDocument1 = null;
	public SDMPreferences mPreferences;
	public SDMConnectivityParameters mParameters;
	public SDMRequestManager sDMRequestManager; 
	private SDMRequestManager mRequestManager = null;
	public MyLogger mLogger = new MyLogger();
	public Context contex;
	private String URL;
	private static Object lockObject = new Object();
	public EditText AgencyNumber;
	public EditText telePhone;
	public EditText Name;
	public EditText Street;
	public EditText City;
	public EditText Country;
	public String telePhone_text;
	public EditText url_Name;
	public String url_Name_text;
	public String agencyNum;
	public ODPLogger log = new ODPLogger();
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.update_agency);
		setTitle("Travel Agency Details");
		AgencyNumber = (EditText) findViewById(R.id.numerText);
		AgencyNumber.setText((String) getIntent().getExtras().get("AgencyNum"));
		telePhone = (EditText) findViewById(R.id.phoneText);
		telePhone.setText((String) getIntent().getExtras().get("Telephone"));
		url_Name = (EditText) findViewById(R.id.urlText);
		url_Name.setText((String) getIntent().getExtras().get("URL"));
		Name = (EditText) findViewById(R.id.nameText);
		Name.setText((String) getIntent().getExtras().get("Name"));
		Street= (EditText) findViewById(R.id.streetText);
		Street.setText((String) getIntent().getExtras().get("Street"));
		City = (EditText) findViewById(R.id.cityText);
		City.setText((String) getIntent().getExtras().get("City"));
		Country= (EditText) findViewById(R.id.countryText);
		Country.setText((String) getIntent().getExtras().get("Country"));

		Button button = (Button) findViewById(R.id.login);
		
		button.setOnClickListener(new OnClickListener() {
			
//			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				 agencyNum = AgencyNumber.getText().toString();
	    		 telePhone_text = telePhone.getText().toString();
	             url_Name_text = url_Name.getText().toString();
	             String URL="https://ldcig8p.wdf.sap.corp:44318/sap/opu/odata/iwfnd/RMTSAMPLEFLIGHT/TravelagencyCollection('"+agencyNum+"')";
	     		log.p("Started making Request for PUT Document","Request Sent for PUT");
	    		
	             sendPutData(URL, getApplicationContext(),telePhone_text,url_Name_text,agencyNum);
	             MessageBox("Update request submitted..!");
	             finish();
			}
		});
		
	    Button btnCncl = (Button) findViewById(R.id.cancel);
	    btnCncl.setOnClickListener(new OnClickListener() {
			
//			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	    
	    Button delbtn = (Button) findViewById(R.id.delete);
	    delbtn.setOnClickListener(new OnClickListener() {
			
//			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				agencyNum = AgencyNumber.getText().toString();
				String URL="https://ldcig8p.wdf.sap.corp:44318/sap/opu/odata/iwfnd/RMTSAMPLEFLIGHT/TravelagencyCollection('"+agencyNum+"')";
				sendDeleteData(URL, getApplicationContext());
			}
		});
	}
		private void MessageBox(String message) {
			// TODO Auto-generated method stub
			Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
		}
		
		public void updateentry()
		{
			agencyNum = AgencyNumber.getText().toString();
   		 telePhone_text = telePhone.getText().toString();
            url_Name_text = url_Name.getText().toString();
            String URL="https://ldcig8p.wdf.sap.corp:44318/sap/opu/odata/iwfnd/RMTSAMPLEFLIGHT/TravelagencyCollection('"+agencyNum+"')";
    		log.p("Started making Request for PUT Document","Request Sent for PUT");
   		
            sendPutData(URL, getApplicationContext(),telePhone_text,url_Name_text,agencyNum);
            MessageBox("Update request submitted..!");
            finish();
		}
		
		public  ISDMODataServiceDocument sendDeleteData(String url,Context contex){
			Log.d(TAG, "Get Service Document");
			/**
			 * create request
			 */
	//added code
			mLogger = new MyLogger();
			mParameters = new SDMConnectivityParameters();
			// Username and password is mandatory
			mParameters.setUserName("perfandroid");
			mParameters.setUserPassword("perfandroid");
			// SDMPreferences needs the context and a logger implementation
			this.contex=contex;
			mPreferences = new SDMPreferences(contex, mLogger);
			try {
				// Set/override default preferences (optional)
				mPreferences.setStringPreference(ISDMPreferences.SAP_APPLICATIONID_HEADER_VALUE,"NEWFLIGHT");
				mPreferences.setIntPreference(ISDMPreferences.SDM_CONNECTIVITY_CONNTIMEOUT,25000);
				mPreferences.setIntPreference(ISDMPreferences.SDM_CONNECTIVITY_HTTPS_PORT,443);
				mPreferences.setIntPreference(ISDMPreferences.SDM_CONNECTIVITY_HTTP_PORT,80);
				//mPreferences.setStringPreference(ISDMPreferences.SDM_CONNECTIVITY_HANDLER_CLASS_NAME,"com.sybase.mobile.lib.client.IMOConnectionHandler");
			} catch (SDMPreferencesException e) {
				
				mLogger.e(getClass().getName(), "Error setting preferences.", e);
		//		alert("Error setting preferences: "+e.getMessage());
			}
			// SDMRequestManager needs them all :)
			// Note: it is no longer a singleton!
			mRequestManager = new SDMRequestManager(mLogger, mPreferences,
					mParameters, SDMRequestManager.MAX_THREADS);

			//end of added code
			ISDMRequest request = new SDMBaseRequest();
			// Set the listener to ourselves; we're implementing ISDMNetListener
			request.setListener(this);
			// Set the request URL
			//url = "http://vmw3815.wdf.sap.corp:50009/sap/opu/sdata/iwfnd/RMTSAMPLEFLIGHT/BookingCollection(carrid='JL',connid='0408',fldate='20111008',bookid='00004235')?sap-client=100";
			url = url+"?sap-client=100";
			request.setRequestUrl(url);
			// Set the HTTP request method (optional)
			
			Map<String, String> curChildMap = new HashMap<String, String>();
	        curChildMap.put("X-Requested-With","XMLHttpRequest");
	        ((SDMBaseRequest) request).setHeaders(curChildMap);
	      
	        Map<String, String> curChildMap1 = new HashMap<String, String>();
	        curChildMap1.put("X-Requested-Test","XMLTestRequest");
	        ((SDMBaseRequest) request).setHeaders(curChildMap1);
	        
	        //int name = 0;
			request.setRequestMethod(ISDMRequest.REQUEST_METHOD_DELETE);//(ISDMRequest.REQUEST_METHOD_DELETE);
			// Set the priority of the request; high priority requests processed
			// first
			request.setPriority(ISDMRequest.PRIORITY_HIGH);
			// Post the request to the request manager - non-blocking call
			mRequestManager.makeRequest(request);
			synchronized (lockObject) {
				try {
					lockObject.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					Log.i("App msg", "request failed: " + e.getMessage());
				}
			}
		    System.out.println(curChildMap);
		    Map<String, String> headers = ((SDMBaseRequest) request).getHeaders();
		    System.out.println(headers);
		    request.setListener(this);
	        return null;
		}
		
		
		public  ISDMODataServiceDocument sendPutData(String url,Context contex,String telePhone,String urlText,String agencyCode){
			Log.d(TAG, "Get Service Document");
		
	//added code
			
//			String data = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
//	            +"<atom:entry xml:base=\"http://ldcig8p.wdf.sap.corp:50018/sap/opu/odata/iwfnd/RMTSAMPLEFLIGHT/\" xmlns:atom=\"http://www.w3.org/2005/Atom\" xmlns:d=\"http://schemas.microsoft.com/ado/2007/08/dataservices\" xmlns:m=\"http://schemas.microsoft.com/ado/2007/08/dataservices/metadata\" xmlns:sap=\"http://www.sap.com/Protocols/SAPData\">\n"
//	            +"<atom:author />\n"
//	            +"<atom:content type=\"text/html\" src=\"http://www.sunshine-travel.sap\" />\n"
//	            +"<m:properties>\n"
//	            +"<d:agencynum>"+agencyCode+"</d:agencynum>\n"
//	            +"<d:NAME>"+Name.getText().toString()+"</d:NAME>\n"
//	            +"<d:STREET>"+Street.getText().toString()+"</d:STREET>\n"	            
//	            +"<d:CITY>"+City.getText().toString()+"</d:CITY>\n"
//	            +"<d:COUNTRY>"+Country.getText().toString()+"</d:COUNTRY>\n"
//	            +"<d:TELEPHONE>"+telePhone+"</d:TELEPHONE>\n"
//	            +"<d:URL>"+ urlText +"</d:URL>\n"
//	            +"</m:properties>\n"
//	            +"<atom:id>http://ldcig8p.wdf.sap.corp:50018/sap/opu/odata/iwfnd/RMTSAMPLEFLIGHT/TravelagencyCollection("+ agencyCode +")</atom:id>\n"
//	            +"<atom:link href=\"TravelagencyCollection("+ agencyCode +")\" rel=\"edit\" type=\"application/atom+xml;type=entry\" />\n"
//	            +"<atom:title>Travelagency</atom:title>\n"
//	            +"<atom:updated>2011-07-27T07:13:49Z</atom:updated>\n"
//	            +"</atom:entry>\n";
			
			 String data ="<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
					+"<entry\n"
					+"xml:base=\"http://ldcig8p.wdf.sap.corp:50018/sap/opu/odata/iwfnd/RMTSAMPLEFLIGHT/\"\n"
					+"xmlns=\"http://www.w3.org/2005/Atom\" xmlns:m=\"http://schemas.microsoft.com/ado/2007/08/dataservices/metadata\"\n"
					+"xmlns:d=\"http://schemas.microsoft.com/ado/2007/08/dataservices\">\n"
					+"<id>http://ldcig8p.wdf.sap.corp:50018/sap/opu/odata/iwfnd/RMTSAMPLEFLIGHT/TravelagencyCollection('"+agencyCode+"')</id>\n"
					+"<title type=\"text\">TravelagencyCollection('"+agencyCode+"')</title>\n"
					+"<category term=\"RMTSAMPLEFLIGHT.Travelagency\"\n"
						+"scheme=\"http://schemas.microsoft.com/ado/2007/08/dataservices/scheme\" />\n"
						+"<link href=\"TravelagencyCollection('"+agencyCode+"')\" rel=\"edit\" title=\"Travelagency\"/>\n"
					+"<content type=\"application/atom+xml\">\n"
						+"<m:properties>\n"
							+"<d:agencynum>"+agencyCode+"</d:agencynum>\n"
							+"<d:NAME>"+Name.getText().toString()+"</d:NAME>\n"
							+"<d:STREET>"+Street.getText().toString()+"</d:STREET>\n"
							+"<d:POSTBOX />\n"
							+"<d:POSTCODE>28779</d:POSTCODE>\n"
							+"<d:CITY>"+City.getText().toString()+"</d:CITY>\n"
							+"<d:COUNTRY>"+Country.getText().toString()+"</d:COUNTRY>\n"
							+"<d:REGION>04</d:REGION>\n"
							+"<d:TELEPHONE>"+telePhone+"</d:TELEPHONE>\n"
							+"<d:URL>http://www.happy</d:URL>\n"
							+"<d:LANGU>D</d:LANGU>\n"
							+"<d:CURRENCY>EUR</d:CURRENCY>\n"
							+"<d:mimeType>text/html</d:mimeType>\n"
						+"</m:properties>\n"
					+"</content>\n"
				+"</entry>\n";

			//negative test
			//data = null;
			
			mLogger = new MyLogger();
			mParameters = new SDMConnectivityParameters();
			mParameters.enableXsrf(true);
			// Username and password is mandatory
			mParameters.setUserName("perfandroid");
			mParameters.setUserPassword("perfandroid");
			// SDMPreferences needs the context and a logger implementation
			this.contex=contex;
			mPreferences = new SDMPreferences(contex, mLogger);
			try {
				// Set/override default preferences (optional)
				mPreferences.setStringPreference(ISDMPreferences.SAP_APPLICATIONID_HEADER_VALUE,"NEWFLIGHT");
				mPreferences.setIntPreference(ISDMPreferences.SDM_CONNECTIVITY_CONNTIMEOUT,25000);
				mPreferences.setIntPreference(ISDMPreferences.SDM_CONNECTIVITY_HTTPS_PORT,443);
				mPreferences.setIntPreference(ISDMPreferences.SDM_CONNECTIVITY_HTTP_PORT,80);
				//mPreferences.setStringPreference(ISDMPreferences.SDM_CONNECTIVITY_HANDLER_CLASS_NAME,"com.sybase.mobile.lib.client.IMOConnectionHandler");
			} catch (SDMPreferencesException e) {
				mLogger.e(getClass().getName(), "Error setting preferences.", e);
			//	alert(e.getMessage());
			}
			// SDMRequestManager needs them all :)
			// Note: it is no longer a singleton!
			mRequestManager = new SDMRequestManager(mLogger, mPreferences,
					mParameters, SDMRequestManager.MAX_THREADS);

			
			//end of added code
			ISDMRequest request = new SDMBaseRequest();
			// Set the listener to ourselves; we're implementing ISDMNetListener
						    
			request.setListener(this);
			// Set the request URL
			url = url;//+"?sap-client=100";
			request.setRequestUrl(url);
			// Set the HTTP request method (optional)
			
			Map<String, String> curChildMap = new HashMap<String, String>();
			Map<String, String> curChildMap1 = new HashMap<String, String>();
	        curChildMap.put("X-Requested-With","XMLHttpRequest");
	        ((SDMBaseRequest) request).setHeaders(curChildMap);
			
	        
	        curChildMap1.put("Content-Type","application/atom+xml");
	        ((SDMBaseRequest) request).setHeaders(curChildMap1);
			
	        byte[] theByteArray = data.getBytes();
	        
	        ((SDMBaseRequest) request).setData(theByteArray);
	       
	        request.setRequestMethod(ISDMRequest.REQUEST_METHOD_PUT);
			// Set the priority of the request; high priority requests processed
			
			request.setPriority(ISDMRequest.PRIORITY_HIGH);
			// Post the request to the request manager - non-blocking call
			mRequestManager.makeRequest(request);		
		    //System.out.println(curChildMap);
		    //Map<String, String> headers = ((SDMBaseRequest) request).getHeaders();
		    //System.out.println(headers);
			synchronized (lockObject) {
				try {
					lockObject.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					Log.i("App msg", "request failed: " + e.getMessage());
				}
			}
		    System.out.println(theByteArray.length);
	        String value = new String(theByteArray);
	        System.out.println(value);
		    request.setListener(this);	 
	        return null;
		}
		
//		@Override
//		public void onError(ISDMRequest rq, HttpResponse rsp,
//				ISDMRequestStateElement rse) {
//			// TODO Auto-generated method stub
//			Log.d(TAG, "Error after request, url: " + rq.getRequestUrl() + ", "
//					+ rq.getRequestMethod());
//			
//			if (rq.getRequestUrl().equals(URL)) {
//				String serviceDocumentXML = getContent(rsp.getEntity());
//			}
//		}
//		@Override
	//	public void onSuccess(ISDMRequest rq, HttpResponse rsp) {}
		
		private String getContent(HttpEntity entity) {
			if (entity == null)
				return null;
			String ret = null;
			try {
				ret = EntityUtils.toString(entity, "UTF-8");
			} catch (ParseException e) {
				Log.w(TAG, "getContent()" + e.getMessage());
			} catch (IOException e) {
				Log.w(TAG, "getContent()" + e.getMessage());
				//alert(e.getMessage());
			}
			return ret;
		}
		public void onError(ISDMRequest rq, ISDMResponse rsp,
				ISDMRequestStateElement arg2) {
			// TODO Auto-generated method stub

			// TODO Auto-generated method stub
			log.p("Stopped making Request for PUT","Response Received for PUT");
			Log.d(TAG, "Error after request, url: " + rq.getRequestUrl() + ", "
					+ rq.getRequestMethod());
			
			if (rq.getRequestUrl().equals(URL)) {
				String serviceDocumentXML = getContent(rsp.getEntity());
			}
			synchronized (lockObject) {
				lockObject.notify();
			}
		
		}
		public void onSuccess(ISDMRequest rq, ISDMResponse rsp) {
			// TODO Auto-generated method stub

			// TODO Auto-generated method stub
			Log.d(TAG, "Success after request, " + rq.getRequestUrl());
			
			log.p("Stopped making Request for PUT","Response Received for PUT");
			/**
			 * Parsing service document
			 */
			if (rq.getRequestUrl().equals(URL)) {
				String serviceDocumentXML = getContent(rsp.getEntity());
			}
			synchronized (lockObject) {
				lockObject.notify();
			}
			//Intent TravelList = new Intent(UpdateAgencyDetails.this, TravelAgencyList.class);
			//startActivity(TravelList);
		  finish();
		}

}
