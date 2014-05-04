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

import org.apache.http.HttpResponse;

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

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.net.MailTo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
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
			parser.enableParserPerformanceLog(true,this.getApplicationContext());
			logger.logToAndroid(true);
			//logger.logsToAndroid();
			pref.setIntPreference(ISDMPreferences.SDM_CONNECTIVITY_CONNTIMEOUT,	7000);
			cache = new SDMCache(pref, logger);
			//cache.clear();
		} catch (SDMPreferencesException e) {
			// TODO Auto-generated catch block
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
		//Log.i("", "Collection items:"+list);
		
		if (list != null && list.size() > 0) {
			String[] res= new String[list.size()];
			// final int n = list.size();
			samplePerf.updated = new ArrayList<CharSequence>();
	//		collectionItems = new ArrayList<CharSequence>();
			Collections=new String[list.size()];
		//	Collections1=new String[list.size()][7];
			for (int i = 0; i < list.size(); ++i) {
				final ISDMODataEntry entry1 = list.get(i);
				StringBuffer sb = new StringBuffer();

			   // agencynum = entry1.getPropertyValue("agencynum");
				sb.append(entry1.getPropertyValue("agencynum")+"-"+entry1.getPropertyValue("NAME"));
				 String content = sb.toString();
				//Performance_AppActivity.updated.add(content);
				 Collections[i]=content;
				// Collections1[i][0]=entry1.getPropertyValue("NAME");
			//	 Collections1[i][1]=entry1.getPropertyValue("STREET");
				// Collections1[i][2]=entry1.getPropertyValue("CITY");
				// Collections1[i][3]=entry1.getPropertyValue("COUNTRY");
			//	 Collections1[i][4]=entry1.getPropertyValue("TELEPHONE");
			//	 Collections1[i][5]=entry1.getPropertyValue("URL");
			//	 Collections1[i][6]=entry1.getPropertyValue("agencynum");
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
	
	
	
	public void onListItemClick(ListView parent, View v, int position, long id){
		//getParticularData();
		//final List<ISDMODataEntry> list = cache.getSDMODataEntries(collectionId);
		//int selectedPosition =parent.getSelectedItemPosition();// adapterView.getSelectedItemPosition();
        //MessageBox((String.valueOf(selectedPosition)));

		Intent myIntent = new Intent(this,UpdateAgencyDetails.class);
        //String val= Collections[position];
        String val="00001134";
/*//        MessageBox(val);
//       // String val1= Collections1[position][1];
//       // MessageBox(val1);
//        String delimiter="-";
//        String[] temp;
//        temp=val.split(delimiter);
//        for(int i=0;i<temp.length;i++)
//        {
//                    MessageBox(temp[i]);
//        }
//        			val=temp[0];
//        			MessageBox(val);
*/        			//collectionId=  collectionId+"('"+val+"')";
        collectionId = "TravelagencyCollection('"+val+"')";			
        getDataServiceMetaCollection(collectionId);
        			//getDataServiceMetaCollection(collectionId);
        			final List<ISDMODataEntry> list = cache.getSDMODataEntries(collectionId);
        			Log.i("", "Collection items:"+list);
        			
        			if (list != null && list.size() > 0) {
        				String[] res= new String[list.size()];
        				// final int n = list.size();
        				samplePerf.updated = new ArrayList<CharSequence>();
        		//		collectionItems = new ArrayList<CharSequence>();
        				Collections1=new String[list.size()];
        				//Collections1=new String[list.size()][7];
        				for (int i = 0; i < list.size(); ++i) {
        					final ISDMODataEntry entry1 = list.get(i);
        					StringBuffer sb = new StringBuffer();

        				    //sb.append(entry1.getPropertyValue("agencynum"));
        					sb.append(entry1.getPropertyValue("agencynum")+"-"+entry1.getPropertyValue("NAME"));
        					 String content = sb.toString();
        					//Performance_AppActivity.updated.add(content);
        					// Collections1[i]=content;
//        					 Collections1[0]=entry1.getPropertyValue("NAME");
//        					 Collections1[1]=entry1.getPropertyValue("STREET");
//        					 Collections1[2]=entry1.getPropertyValue("CITY");
//        					 Collections1[3]=entry1.getPropertyValue("COUNTRY");
//        					 Collections1[4]=entry1.getPropertyValue("TELEPHONE");
//        					 Collections1[5]=entry1.getPropertyValue("URL");
//        					 Collections1[6]=entry1.getPropertyValue("agencynum");
//        					Log.i("", "Display Collec is working fine \n " + samplePerf.updated);
        					res[i]=sb.toString();
        			
        		    log.p("Time taken after parse into db","TravelAgencyDetails");
        			myIntent.putExtra("Name",entry1.getPropertyValue("NAME"));
        			myIntent.putExtra("Street",entry1.getPropertyValue("STREET"));
        			myIntent.putExtra("City",entry1.getPropertyValue("CITY"));
        			myIntent.putExtra("Country",entry1.getPropertyValue("COUNTRY"));
        			myIntent.putExtra("Telephone",entry1.getPropertyValue("TELEPHONE"));
        			myIntent.putExtra("URL",entry1.getPropertyValue("URL"));
        			myIntent.putExtra("AgencyNum",entry1.getPropertyValue("agencynum"));
        				} 
        				Log.i("", "Collection list:" +samplePerf.updated);
        			}
        			collectionId="TravelagencyCollection";
                    Toast.makeText(this, val, Toast.LENGTH_SHORT).show();
                    startActivity(myIntent);        
	}
	
	private void MessageBox(String message) {
		// TODO Auto-generated method stub
		Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
	}
	public void getDataServiceMetaDataDoc() 
	{
		// for schema
		String schemaUrl = samplePerf.serviceDocUrl + "$metadata";
	
		getSchema.setRequestUrl(schemaUrl);
		
//		 Map<String, String> curChildMap2 = new HashMap<String, String>();
//        curChildMap2.put("Accept-Encoding", "gzip");
//        ((SDMBaseRequest) getSchema).setHeaders(curChildMap2);
		getSchema.setRequestMethod(SDMBaseRequest.REQUEST_METHOD_GET);
		getSchema.setListener(this);
		log.p("Started making Request for Metadata Document","Request Sent for Metadata Document");
		
		reqmanager.makeRequest(getSchema);
		
		synchronized (lockObject) {
			try {
				lockObject.wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
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
				// TODO Auto-generated catch block
				Log.i("App msg", "request failed: " + e.getMessage());
			}
		}
		log.p("Stopped making Request for Collections Document","Response Received for Collections Document");
	}
	public void getDataServiceDoc() 
	{
		// for service dcocument
		setreq.setRequestUrl(samplePerf.serviceDocUrl);
//		  Map<String, String> curChildMap2 = new HashMap<String, String>();
//	        curChildMap2.put("Accept-Encoding", "gzip");
//	        ((SDMBaseRequest) setreq).setHeaders(curChildMap2);
		setreq.setRequestMethod(SDMBaseRequest.REQUEST_METHOD_GET);
		setreq.setListener(this);
		log.p("Started making Request for Service Document","Request Sent for Service Document");
		reqmanager.makeRequest(setreq);
		
		synchronized (lockObject){
			try {
				lockObject.wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				Log.i("App msg", "request failed: " + e.getMessage());
			}
			
		}
		log.p("Stopped making Request for Service Document","Response Received for Service Document");
	
	
	// for collection


	}
//	@Override
	//public void onError(ISDMRequest aRequest, HttpResponse aResponse,
		//	ISDMRequestStateElement aRequestStateElement) {}

		
//	@Override
	//public void onSuccess(ISDMRequest aRequest, HttpResponse aResponse) {}
	public boolean onCreateOptionsMenu(Menu menu) {

		//	return super.onCreateOptionsMenu(menu);
			
			menu.add(Menu.NONE, 100, Menu.NONE, "Create Agency");
			menu.add(Menu.NONE, 101, Menu.NONE, "Delete Agency");
			return true;
		
//			return false;
		}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

	//	return super.onOptionsItemSelected(item);
		
		switch (item.getItemId()){
		
		case 100:
			Intent crAgency = new Intent(TravelAgencyList.this, CreateAgency.class);
			startActivity(crAgency);
			break;
			
		case 101:
			//Intent back = new Intent(TravelAgencyList.this, samplePerf.class);
			//startActivity(back);
		{
			//agencyNum = AgencyNumber.getText().toString();
			String URL="https://ldcig8p.wdf.sap.corp:44318/sap/opu/odata/iwfnd/RMTSAMPLEFLIGHT/TravelagencyCollection('0000999')";
			log.p("Started making Request for Delete Document","Request Sent for Delete Document");
			
			sendDeleteData(URL, getApplicationContext());
			
			
			
			
		}
		default:
			Log.v("SAPLog", "default");
		}
		
		return false;
	}
	public  ISDMODataServiceDocument sendDeleteData(String url,Context contex){
		//Log.d(TAG, "Get Service Document");
		/**
		 * create request
		 */
//added code
		mLogger = new MyLogger();
		conpar = new SDMConnectivityParameters();
		// Username and password is mandatory
		conpar.setUserName("perfandroid");
		conpar.setUserPassword("perfandroid");
		conpar.enableXsrf(true);
		// SDMPreferences needs the context and a logger implementation
		//this.contex=contex;
		pref = new SDMPreferences(contex, mLogger);
		try {
			// Set/override default preferences (optional)
			pref.setStringPreference(ISDMPreferences.SAP_APPLICATIONID_HEADER_VALUE,"NEWFLIGHT");
			pref.setIntPreference(ISDMPreferences.SDM_CONNECTIVITY_CONNTIMEOUT,25000);
			pref.setIntPreference(ISDMPreferences.SDM_CONNECTIVITY_HTTPS_PORT,443);
			pref.setIntPreference(ISDMPreferences.SDM_CONNECTIVITY_HTTP_PORT,80);
			//mPreferences.setStringPreference(ISDMPreferences.SDM_CONNECTIVITY_HANDLER_CLASS_NAME,"com.sybase.mobile.lib.client.IMOConnectionHandler");
		} catch (SDMPreferencesException e) {
			
			mLogger.e(getClass().getName(), "Error setting preferences.", e);
	//		alert("Error setting preferences: "+e.getMessage());
		}
		// SDMRequestManager needs them all :)
		// Note: it is no longer a singleton!
reqmanager = new SDMRequestManager(mLogger, pref,
		conpar, SDMRequestManager.MAX_THREADS);
        conpar.enableXsrf(true);

		//end of added code
		//ISDMRequest request = new SDMBaseRequest();
		// Set the listener to ourselves; we're implementing ISDMNetListener
		Delreq.setListener(this);
		// Set the request URL
		//url = "http://vmw3815.wdf.sap.corp:50009/sap/opu/sdata/iwfnd/RMTSAMPLEFLIGHT/BookingCollection(carrid='JL',connid='0408',fldate='20111008',bookid='00004235')?sap-client=100";
		url = url;//+"?sap-client=100";
		Delreq.setRequestUrl(url);
		// Set the HTTP request method (optional)
		
		Map<String, String> curChildMap = new HashMap<String, String>();
        curChildMap.put("X-Requested-With","XMLHttpRequest");
        ((SDMBaseRequest) Delreq).setHeaders(curChildMap);
      
        Map<String, String> curChildMap1 = new HashMap<String, String>();
        curChildMap1.put("X-Requested-Test","XMLTestRequest");
        ((SDMBaseRequest) Delreq).setHeaders(curChildMap1);
        
        Map<String, String> curChildMap2 = new HashMap<String, String>();
        curChildMap2.put("Accept-Encoding", "gzip");
        ((SDMBaseRequest) Delreq).setHeaders(curChildMap2);
        
        //int name = 0;
        Delreq.setRequestMethod(ISDMRequest.REQUEST_METHOD_DELETE);//(ISDMRequest.REQUEST_METHOD_DELETE);
		// Set the priority of the request; high priority requests processed
		// first
        Delreq.setPriority(ISDMRequest.PRIORITY_HIGH);
		// Post the request to the request manager - non-blocking call
		reqmanager.makeRequest(Delreq);
	    System.out.println(curChildMap);
	    Map<String, String> headers = ((SDMBaseRequest) Delreq).getHeaders();
	    System.out.println(headers);
	    Delreq.setListener(this);
		
	   
        return null;
      
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
			// TODO: handle exception
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
				//String collString = EntityUtils.toString(responseEntity);
				InputStream inst = responseEntity.getContent();
				byte[] responseB = decompress(inst);
				String collString = new String(responseB);
				log.p("Start of Parsing for Collection",aRequest.getRequestUrl());
				//System.out.println(collString);
				entries = parser.parseSDMODataEntriesXML(collString,collectionId, cache.getSDMODataSchema());
				//log.p("Collection String",collString);
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

	

