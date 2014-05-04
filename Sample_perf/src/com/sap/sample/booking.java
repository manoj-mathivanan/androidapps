package com.sap.sample;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import com.sap.mobile.lib.sdmcache.SDMCache;
import com.sap.mobile.lib.sdmconfiguration.ISDMPreferences;
import com.sap.mobile.lib.sdmconfiguration.SDMPreferences;
import com.sap.mobile.lib.sdmconfiguration.SDMPreferencesException;
import com.sap.mobile.lib.sdmconnectivity.ISDMRequest;
import com.sap.mobile.lib.sdmconnectivity.ISDMRequestStateElement;
import com.sap.mobile.lib.sdmconnectivity.ISDMResponse;
import com.sap.mobile.lib.sdmconnectivity.SDMBaseRequest;
import com.sap.mobile.lib.sdmconnectivity.SDMConnectivityParameters;
import com.sap.mobile.lib.sdmconnectivity.SDMRequestManager;
import com.sap.mobile.lib.sdmparser.ISDMODataEntry;
import com.sap.mobile.lib.sdmparser.ISDMODataSchema;
import com.sap.mobile.lib.sdmparser.ISDMODataServiceDocument;
import com.sap.mobile.lib.sdmparser.SDMParser;
import com.sap.mobile.lib.supportability.ISDMLogger;
import com.sap.mobile.lib.supportability.SDMLogger;

import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class booking extends ListActivity implements
com.sap.mobile.lib.sdmconnectivity.ISDMNetListener {
	protected static SDMBaseRequest setreq = new SDMBaseRequest();
	protected static SDMRequestManager reqmanager;
	private static Object lockObject = new Object();
	protected static SDMBaseRequest getSchema = new SDMBaseRequest();
	public static ISDMODataSchema schema;
	protected static SDMBaseRequest getCollec = new SDMBaseRequest();
	boolean status;
	public static ISDMODataServiceDocument serviceDoc;
	protected SDMCache cache;
	protected SDMParser parser;
	boolean flag;
	public static List<ISDMODataEntry> entries;
	String collectionId = "BookingCollection";
	protected SDMConnectivityParameters conpar = new SDMConnectivityParameters();
	protected SDMPreferences pref;
	protected SDMLogger logger;
	protected ArrayList<CharSequence> collectionItems;
	protected ArrayAdapter<CharSequence> adapter;
	
	
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.booking_display);
		
		try {
			logger = new SDMLogger();
			pref = new SDMPreferences(getApplicationContext(), logger);
			pref.setStringPreference("CUSTOM_PREF_USERNAME", samplePerf.Username);
			logger.setLogLevel(ISDMLogger.DEBUG);
			conpar.setUserName(pref.getStringPreference("CUSTOM_PREF_USERNAME"));
			conpar.setUserPassword(samplePerf.Password);
			reqmanager = new SDMRequestManager(logger, pref, conpar, 1);
			parser = new SDMParser(pref, logger);
			pref.setIntPreference(ISDMPreferences.SDM_CONNECTIVITY_CONNTIMEOUT,	7000);
			cache = new SDMCache(pref, logger);
			cache.clear();
		} catch (SDMPreferencesException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e("Connection failed", e.getMessage());
			Log.i("App msg", "Pref error: " + e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		getData();
		
		final List<ISDMODataEntry> list = cache.getSDMODataEntries(collectionId);
		Log.i("", "Collection items:"+list);
		
		if (list != null && list.size() > 0) {
			String[] res= new String[list.size()];
			// final int n = list.size();
			samplePerf.updated = new ArrayList<CharSequence>();
			for (int i = 0; i < list.size(); ++i) {
				final ISDMODataEntry entry1 = list.get(i);
				StringBuffer sb = new StringBuffer();

				// sb.append(entry1.getPropertyValue("agencynum"));
				sb.append(entry1.getPropertyValue("NAME"));
				 String content = sb.toString();
				 samplePerf.updated.add(content);
				
				Log.i("", "Display Collec is working fine \n " + samplePerf.updated );
				res[i]=sb.toString();
				
			}
			Log.i("", "Collection list:" +samplePerf.updated);
			adapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_list_item_2,
					samplePerf.updated);
			
			setListAdapter(adapter);
			ListView lv = getListView();
			lv.setVisibility(View.VISIBLE);
			 lv.setTextFilterEnabled(true);
			
			flag=true;
		}
	}
	
	public void getData() 
	{
		// for service dcocument
		setreq.setRequestUrl(samplePerf.serviceDocUrl);
		setreq.setRequestMethod(SDMBaseRequest.REQUEST_METHOD_GET);
		setreq.setListener(this);
		reqmanager.makeRequest(setreq);
		
		synchronized (lockObject){
			try {
				lockObject.wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				Log.i("App msg", "request failed: " + e.getMessage());
			}
			
		}
	
	// for schema
	String schemaUrl = samplePerf.serviceDocUrl + "$metadata";
	getSchema.setRequestUrl(schemaUrl);
	getSchema.setRequestMethod(SDMBaseRequest.REQUEST_METHOD_GET);
	getSchema.setListener(this);
	reqmanager.makeRequest(getSchema);
	
	synchronized (lockObject) {
		try {
			lockObject.wait();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			Log.i("App msg", "request failed: " + e.getMessage());
		}
	}
	
	// for collection
	String collecUrl = " http://vmw3815.wdf.sap.corp:50009/sap/opu/sdata/iwfnd/RMTSAMPLEFLIGHT/BookingCollection";
	getCollec.setRequestUrl(collecUrl);
	getCollec.setRequestMethod(SDMBaseRequest.REQUEST_METHOD_GET);
	getCollec.setListener(this);
	reqmanager.makeRequest(getCollec);
	
	synchronized (lockObject) {
		try {
			lockObject.wait();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			Log.i("App msg", "request failed: " + e.getMessage());
		}
	}

	}


		


	public void onError(ISDMRequest arg0, ISDMResponse aResponse,
			ISDMRequestStateElement arg2) {
		// TODO Auto-generated method stub

		// TODO Auto-generated method stub
		Log.i("ERROR", aResponse.getStatusLine().toString());
		
		
		status = false;
		synchronized (lockObject) {
			lockObject.notify();
		}
	
	}

	public void onSuccess(ISDMRequest aRequest, ISDMResponse aResponse) {
		// TODO Auto-generated method stub

		// TODO Auto-generated method stub
		HttpEntity responseEntity = aResponse.getEntity();
		
		if (aRequest.equals(setreq)){
			try {
			serviceDoc = parser.parseSDMODataServiceDocumentXML(EntityUtils.toString(responseEntity));
			cache.setSDMODataServiceDocument(serviceDoc);
			
			status = true;
			samplePerf.flag = 1;
			
			synchronized (lockObject) {
				lockObject.notify();				
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			Log.e("Get failed", e.getMessage());
		}
		}
		
		if (aRequest.equals(getSchema)) {
			try {
				String xmlString = EntityUtils.toString(responseEntity);
				schema = parser.parseSDMODataSchemaXML(xmlString, cache
						.getSDMODataServiceDocument());
				cache.setSDMODataSchema(schema);
				status = true;
				synchronized (lockObject) {
					lockObject.notify();
				}

			} catch (Exception e) {

				status = false;
				synchronized (lockObject) {
					lockObject.notify();
				}
			}
		}
		
		if (aRequest.equals(getCollec)) {
			try {
				String collString = EntityUtils.toString(responseEntity);

				entries = parser.parseSDMODataEntriesXML(collString,
						collectionId, cache.getSDMODataSchema());
				cache.setSDMODataEntries(entries);

				status = true;
				synchronized (lockObject) {
					lockObject.notify();
				}

			} catch (Exception e) {

				status = false;
				synchronized (lockObject) {
					lockObject.notify();
				}
			}
		}
	
	}
	}

