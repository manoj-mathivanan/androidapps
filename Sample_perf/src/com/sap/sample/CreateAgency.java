package com.sap.sample;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.content.Context;
import android.net.ParseException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

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

public class CreateAgency extends Activity implements ISDMNetListener{

	protected static  EditText AgencyNumber, AgencyName, Street, City, Country, Telephone;
	public SDMPreferences mPreferences;
	public SDMConnectivityParameters mParameters;
	public SDMRequestManager sDMRequestManager; 
	private SDMRequestManager mRequestManager = null;
	public static final String TAG = "ServiceDocProvider";
	public MyLogger mLogger = new MyLogger();
	public Context contex;
	public  String serviceDocument = null;
	private String URL;
	public String AgencyNumber_txt="0000999";
	public String AgencyName_txt;
	public String City_txt;
	public String Telephone_txt;
	public String Country_txt;
	public String Street_txt;
	Button Save, Cancel;
	public static String baseUrl;
	private static Object lockObject = new Object();
	public ODPLogger log = new ODPLogger();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_agency);
        
        AgencyNumber = (EditText) findViewById(R.id.nameText1);
        AgencyName = (EditText) findViewById(R.id.nameText);
        Street = (EditText) findViewById(R.id.streetText);
        City = (EditText) findViewById(R.id.cityText);
        Telephone = (EditText) findViewById(R.id.phoneText);
        Country = (EditText) findViewById(R.id.countryText);
        //createentry();
        Save =(Button)findViewById(R.id.login);
        Save.setOnClickListener(new OnClickListener(){

//			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AgencyNumber_txt = AgencyNumber.getText().toString();
	    		AgencyName_txt = AgencyName.getText().toString();
	    		Street_txt = Street.getText().toString();
	    		City_txt = City.getText().toString();
	    		Telephone_txt = Telephone.getText().toString();
	    		Country_txt = Country.getText().toString();
	    		String URL = "https://ldcig8p.wdf.sap.corp:44318/sap/opu/odata/iwfnd/RMTSAMPLEFLIGHT/TravelagencyCollection";
	    		log.p("Started making Request for POST Data","Request Sent for POST");
	    		
	    		sendPostData(URL, getApplicationContext(), AgencyNumber_txt, AgencyName_txt, Street_txt, City_txt, Telephone_txt, Country_txt);
	    		finish();
			}
        	
        });
        
        Cancel = (Button) findViewById(R.id.cancel);
        Cancel.setOnClickListener(new OnClickListener() {
			
//			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

}
    public void createentry()
    {
    	// TODO Auto-generated method stub
		AgencyNumber_txt = AgencyNumber.getText().toString();
		AgencyName_txt = AgencyName.getText().toString();
		Street_txt = Street.getText().toString();
		City_txt = City.getText().toString();
		Telephone_txt = Telephone.getText().toString();
		Country_txt = Country.getText().toString();
		String URL = "https://ldcig8p.wdf.sap.corp:44318/sap/opu/odata/iwfnd/RMTSAMPLEFLIGHT/TravelagencyCollection";
		log.p("Started making Request for POST Data","Request Sent for POST");
		
		sendPostData(URL, getApplicationContext(), AgencyNumber_txt, AgencyName_txt, Street_txt, City_txt, Telephone_txt, Country_txt);
		finish();
    }
	public  ISDMODataServiceDocument sendPostData(String url, Context contex, String AgencyNumber_txt, String AgencyName_txt, String Street_txt, String City_txt, String Telephone_txt, String Country_txt ){
        Log.d(TAG, "Get Service Document");
        
        String data ="<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
			+"<entry\n"
			+"xml:base=\"http://ldcig8p.wdf.sap.corp:50018/sap/opu/odata/iwfnd/RMTSAMPLEFLIGHT/\"\n"
			+"xmlns=\"http://www.w3.org/2005/Atom\" xmlns:m=\"http://schemas.microsoft.com/ado/2007/08/dataservices/metadata\"\n"
			+"xmlns:d=\"http://schemas.microsoft.com/ado/2007/08/dataservices\">\n"
			+"<id>http://ldcig8p.wdf.sap.corp:50018/sap/opu/odata/iwfnd/RMTSAMPLEFLIGHT/TravelagencyCollection('"+AgencyNumber_txt+"')</id>\n"
			+"<title type=\"text\">TravelagencyCollection('"+AgencyNumber_txt+"')</title>\n"
			+"<category term=\"RMTSAMPLEFLIGHT.Travelagency\"\n"
				+"scheme=\"http://schemas.microsoft.com/ado/2007/08/dataservices/scheme\" />\n"
				+"<link href=\"TravelagencyCollection('"+AgencyNumber_txt+"')\" rel=\"edit\" title=\"Travelagency\"/>\n"
			+"<content type=\"application/atom+xml\">\n"
				+"<m:properties>\n"
					+"<d:agencynum>"+AgencyNumber_txt+"</d:agencynum>\n"
					+"<d:NAME>"+AgencyName_txt+"</d:NAME>\n"
					+"<d:STREET>Rastenburger Str. 12</d:STREET>\n"
					+"<d:POSTBOX />\n"
					+"<d:POSTCODE>28779</d:POSTCODE>\n"
					+"<d:CITY>"+City_txt+"</d:CITY>\n"
					+"<d:COUNTRY>DE</d:COUNTRY>\n"
					+"<d:REGION>04</d:REGION>\n"
					+"<d:TELEPHONE>"+Telephone_txt+"</d:TELEPHONE>\n"
					+"<d:URL>http://www.happy</d:URL>\n"
					+"<d:LANGU>D</d:LANGU>\n"
					+"<d:CURRENCY>EUR</d:CURRENCY>\n"
					+"<d:mimeType>text/html</d:mimeType>\n"
				+"</m:properties>\n"
			+"</content>\n"
		+"</entry>\n";


        //negative test
        //data = null;
     
//added code
        System.out.println(data);
        mLogger = new MyLogger();
        mParameters = new SDMConnectivityParameters();
        // Username and password is mandatory
        mParameters.setUserName("perfandroid");
        mParameters.setUserPassword("perfandroid");
        mParameters.enableXsrf(true);
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
        url = url; //+"?sap-client=100";
        request.setRequestUrl(url);
        // Set the HTTP request method (optional)
        Map<String, String> curChildMap = new HashMap<String, String>();
        Map<String, String> curChildMap1 = new HashMap<String, String>();
        curChildMap.put("X-Requested-With","XMLHttpRequest");
        curChildMap1.put("Content-Type","application/atom+xml");
        ((SDMBaseRequest) request).setHeaders(curChildMap);
        ((SDMBaseRequest) request).setHeaders(curChildMap1);
       // ((SDMBaseRequest) request).setHeaders(null);
        //((SDMBaseRequest) request).setHeaders(null);
        byte[] theByteArray = data.getBytes();
        ((SDMBaseRequest) request).setData(theByteArray);
    
        request.setRequestMethod(ISDMRequest.REQUEST_METHOD_POST);
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
        //System.out.println(curChildMap);
        Map<String, String> headers = ((SDMBaseRequest) request).getHeaders();
        System.out.println(headers);
        System.out.println(theByteArray.length);
        String value = new String(theByteArray);
        System.out.println(value);
        request.setListener(this);
        return null;
  }

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
		/**
		 * Parsing service document
		 */
		if (rq.getRequestUrl().equals(URL)) {
			String serviceDocumentXML = getContent(rsp.getEntity());
		}
		
		log.p("Stopped POST Request ","Response Received for POST");
		synchronized (lockObject) {
			lockObject.notify();
		}
		//Intent back = new Intent(CreateAgency.this, TravelAgencyList.class);
		//startActivity(back);
			
		
	}
	}
