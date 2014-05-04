package com.sap.sample;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

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
import com.sybase.mobile.lib.log.ODPLogger;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;

public class TravelAgencyList extends ListActivity implements
com.sap.mobile.lib.sdmconnectivity.ISDMNetListener {
	protected static SDMBaseRequest setreq = new SDMBaseRequest();
	protected static SDMBaseRequest Delreq = new SDMBaseRequest();
	protected static SDMRequestManager reqmanager;
	private static Object lockObject = new Object();
	protected static SDMBaseRequest getSchema = new SDMBaseRequest();
	public static ISDMODataSchema schema;
	protected static SDMBaseRequest getCollec = new SDMBaseRequest();
	boolean status;
	public static ISDMODataServiceDocument serviceDoc;
	protected SDMCache cache;
	protected SDMParser parser;
	public String agencynum="";
	public MyLogger mLogger = new MyLogger();
	boolean flag;
	public static List<ISDMODataEntry> entries;
	String collectionId = "TravelagencyCollection?$top=300";
	protected SDMConnectivityParameters conpar = new SDMConnectivityParameters();
	protected SDMPreferences pref;
	protected SDMLogger logger;
	protected ArrayList<CharSequence> collectionItems;
	protected ArrayList<CharSequence> collectionItems_actual;
	protected  String[] Collections;
	protected  String[] Collections1;
	protected ArrayAdapter<CharSequence> adapter;
	public ODPLogger log = new ODPLogger();
	
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.display);
		
		try {
			logger = new SDMLogger();
			pref = new SDMPreferences(getApplicationContext(), logger);
			pref.setStringPreference("CUSTOM_PREF_USERNAME", samplePerf.Username);
			logger.setLogLevel(ISDMLogger.DEBUG);
			
			conpar.setUserName(pref.getStringPreference("CUSTOM_PREF_USERNAME"));
			conpar.setUserPassword(samplePerf.Password);
			conpar.enableXsrf(true);
			reqmanager = new SDMRequestManager(logger, pref, conpar, 1);
			parser = new SDMParser(pref, logger);
			//parser.enableParserPerformanceLog(true,this.getApplicationContext());
			logger.logToAndroid(true);
			pref.setIntPreference(ISDMPreferences.SDM_CONNECTIVITY_CONNTIMEOUT,	7000);
			cache = new SDMCache(pref, logger);
			
		} catch (SDMPreferencesException e) {
			e.printStackTrace();
			Log.e("Connection failed", e.getMessage());
			Log.i("App msg", "Pref error: " + e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
		getDataServiceDoc();
		getDataServiceMetaDataDoc();
		getDataServiceMetaCollection(collectionId);
		final List<ISDMODataEntry> list = cache.getSDMODataEntries(collectionId);
		if (list != null && list.size() > 0) {
			String[] res= new String[list.size()];
			samplePerf.updated = new ArrayList<CharSequence>();
			Collections=new String[list.size()];
			for (int i = 0; i < list.size(); ++i) {
				final ISDMODataEntry entry1 = list.get(i);
				StringBuffer sb = new StringBuffer();
				sb.append(entry1.getPropertyValue("agencynum")+"-"+entry1.getPropertyValue("NAME"));
				 String content = sb.toString();
				 Collections[i]=content;
				Log.i("", "Display Collec is working fine \n " + samplePerf.updated);
				res[i]=sb.toString();
			} 
			log.p("Time taken after parse into db","TravelAgencyCollection");
			Log.i("", "Collection list:" +samplePerf.updated);
			
			adapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_list_item_1,Collections);
			setListAdapter(adapter);
			ListView lv = getListView();
			lv.setVisibility(View.VISIBLE);
			lv.setTextFilterEnabled(true);
			flag=true;
		}
	}
	
	
	private void MessageBox(String message) {
		Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
	}
	public void getDataServiceMetaDataDoc() 
	{
		String schemaUrl = samplePerf.serviceDocUrl + "$metadata";
	
		getSchema.setRequestUrl(schemaUrl);
		
		getSchema.setRequestMethod(SDMBaseRequest.REQUEST_METHOD_GET);
		getSchema.setListener(this);
		log.p("Started making Request for Metadata Document","Request Sent for Metadata Document");
		
		reqmanager.makeRequest(getSchema);
		
		synchronized (lockObject) {
			try {
				lockObject.wait();
			} catch (InterruptedException e) {
				Log.i("App msg", "request failed: " + e.getMessage());
			}
		}
		log.p("Stopped making Request for Metadata Document","Response Received for Metadata Document");
	}
	public void getDataServiceMetaCollection(String collName) 
	{
		String collecUrl = samplePerf.serviceDocUrl +collName;// "http://vmw3815.wdf.sap.corp:50009/sap/opu/sdata/iwfnd/RMTSAMPLEFLIGHT/"+collName;
		getCollec.setRequestUrl(collecUrl);
		  Map<String, String> curChildMap2 = new HashMap<String, String>();
	        curChildMap2.put("Accept-Encoding", "gzip");
	        ((SDMBaseRequest) getCollec).setHeaders(curChildMap2);
		getCollec.setRequestMethod(SDMBaseRequest.REQUEST_METHOD_GET);
		getCollec.setListener(this);
		log.p("Started making Request for Collections data","Request Sent for  Collections Document");
		
		reqmanager.makeRequest(getCollec);
		
		synchronized (lockObject) {
			try {
				lockObject.wait();
			} catch (InterruptedException e) {
			
				Log.i("App msg", "request failed: " + e.getMessage());
			}
		}
		log.p("Stopped making Request for Collections Document","Response Received for Collections Document");
	}
	public void getDataServiceDoc() 
	{
		setreq.setRequestUrl(samplePerf.serviceDocUrl);
		setreq.setRequestMethod(SDMBaseRequest.REQUEST_METHOD_GET);
		setreq.setListener(this);
		log.p("Started making Request for Service Document","Request Sent for Service Document");
		reqmanager.makeRequest(setreq);
		
		synchronized (lockObject){
			try {
				lockObject.wait();
			} catch (InterruptedException e) {
				
				Log.i("App msg", "request failed: " + e.getMessage());
			}
			
		}
		log.p("Stopped making Request for Service Document","Response Received for Service Document");
	
	}

	public static byte[] decompress(InputStream in) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ByteArrayInputStream bis = (ByteArrayInputStream) in;
        InputStream is = null;
        try {

              is = new GZIPInputStream((InputStream) bis);

              byte[] b = new byte[10512];
              int count = 0;
              count = is.read(b);
              while (count != -1) {
                    baos.write(b, 0, count);
                    count = is.read(b);
              }
        } catch (Exception e) {
              throw new IOException(e.getMessage());
        }
        return baos.toByteArray();
  }



	public void onError(ISDMRequest arg0, ISDMResponse aResponse,
			ISDMRequestStateElement arg2) {

		Log.i("ERROR", aResponse.getStatusLine().toString());
		
		
		status = false;
		synchronized (lockObject) {
			lockObject.notify();
		}
	
		
	}



	public void onSuccess(ISDMRequest aRequest, ISDMResponse aResponse) {

		
		HttpEntity responseEntity = aResponse.getEntity();
		
		if (aRequest.equals(setreq)){
			try {
				log.p("Start Parsing for Servicecocument",aRequest.getRequestUrl());
				InputStream inst = responseEntity.getContent();
				String xmlst = readString(inst);
			serviceDoc = parser.parseSDMODataServiceDocumentXML(xmlst);
			cache.setSDMODataServiceDocument(serviceDoc);
			log.p("End Parsing for Servicecocument",aRequest.getRequestUrl());
			status = true;
			samplePerf.flag = 1;
			
			synchronized (lockObject) {
				lockObject.notify();				
			}
		} catch (Exception e) {
			
			e.printStackTrace();
			Log.e("Get failed", e.getMessage());
		}
		}
		else
		
		if (aRequest.equals(getSchema)) {
			try {
				log.p("Start of Parsing for metadata",aRequest.getRequestUrl());
				String xmlString = EntityUtils.toString(responseEntity);
				schema = parser.parseSDMODataSchemaXML(xmlString, cache
						.getSDMODataServiceDocument());
				cache.setSDMODataSchema(schema);
				log.p("End of Parsing for metadata",aRequest.getRequestUrl());
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
				
				InputStream inst = responseEntity.getContent();
				byte[] responseB = decompress(inst);
				String collString = new String(responseB);
				log.p("Start of Parsing for Collection",aRequest.getRequestUrl());
				
				entries = parser.parseSDMODataEntriesXML(collString,collectionId, cache.getSDMODataSchema());
				
				cache.setSDMODataEntries(entries);
				log.p("End of Parsing for Collection",aRequest.getRequestUrl());
				
				
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
		if (aRequest.equals(Delreq)) {
			log.p("Stopped making Request for Delete Request","Response Received for Delete Request");
			
		}
	
		
	}



		static String readString(InputStream is) throws IOException {
		  char[] buf = new char[2048];
		  Reader r = new InputStreamReader(is, "UTF-8");
		  StringBuilder s = new StringBuilder();
		  while (true) {
		    int n = r.read(buf);
		    if (n < 0)
		      break;
		    s.append(buf, 0, n);
		  }
		  return s.toString();
		}

	}

	

