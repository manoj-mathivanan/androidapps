package com.sap.manoj.smp_libs;

import java.io.BufferedReader;

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
import com.sap.mobile.lib.sdmparser.SDMParserException;
import com.sap.mobile.lib.sdmpersistence.SDMPersistence;
//import com.sap.mobile.lib.sdmpersistence.SDMPersistence;
import com.sap.mobile.lib.supportability.ISDMLogger;
import com.sap.mobile.lib.supportability.SDMLogger;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.sap.mobile.lib.sdmconfiguration.ISDMPreferences;
import com.sap.mobile.lib.sdmconfiguration.SDMConstants;
import com.sap.mobile.lib.sdmconfiguration.SDMPreferences;
import com.sap.mobile.lib.sdmconnectivity.SDMConnectivityParameters;
import com.sap.mobile.lib.supportability.ISDMLogger;
import com.sap.mobile.lib.supportability.SDMLogger;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class listcollection extends ListActivity  implements ISDMNetListener{
	public long start,stop,end;
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
	public SDMBaseRequest getSchema = new SDMBaseRequest();
	protected static SDMBaseRequest getCollec = new SDMBaseRequest();
	public String[] collectionItems;
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display);
        final Button fetch = (Button)findViewById(R.id.fetch);
        fetch.setOnClickListener(new OnClickListener() {           
	    	@Override 
	        public void onClick(View v) { 
	    		fetch_collection();
	        }
	    });  
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
	
	public void fetch_collection(){
		String serviceDocument1 = "";
		ArrayAdapter<CharSequence> adapter;
		try {
				
			logger = new SDMLogger();
        	pref = new SDMPreferences(this.getApplicationContext() , logger);
        	SDMConnectivityParameters param = new SDMConnectivityParameters();
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
			cache.clear();	
       	
        	//get service doc
        	getrequest.setListener(listcollection.this);
        	getrequest.setRequestUrl(helper.serviceDocUrl);
        	getrequest.setRequestMethod(SDMBaseRequest.REQUEST_METHOD_GET);    	
       	Map<String, String> headers = new HashMap<String, String>();
       	headers.put("X-SUP-APPCID",helper.AppconnID);
       	headers.put("Content-Type", "application/atom+xml"); 
       	getrequest.setHeaders(headers);
       	
       	Log.d("Performance","Start executing Service Document");
       	reqMan.makeRequest(getrequest);
    		synchronized (lockObject) {
    			try {
    				lockObject.wait();
    			} catch (InterruptedException e) {
    				//Log.i("tag","in catch of get service doc"+e);
    			}
    		}
    		

    		//get Schema
    		String schemaUrl = helper.serviceDocUrl+"$metadata";
    		getSchema.setListener(listcollection.this);
    		getSchema.setRequestUrl(schemaUrl);
    		getSchema.setRequestMethod(SDMBaseRequest.REQUEST_METHOD_GET); 		
    		Map<String, String> headers1 = new HashMap<String, String>();
            	headers1.put("X-SUP-APPCID",helper.AppconnID);
            	headers1.put("Content-Type", "application/atom+xml"); 
            	
           	getSchema.setHeaders(headers1);
           	Log.d("Performance","Start executing Metadata");
    		reqMan.makeRequest(getSchema);   				
    		synchronized (lockObject) {
    			try {
    				lockObject.wait();
    			} catch (InterruptedException e) {
    				//Log.i("tag","in catch of get metadata"+e);
    			}
    		}
    		
    		//getcollection   		
    		String collecUrl = helper.serviceDocUrl + helper.collectionId;
    		getCollec.setListener(listcollection.this);
    		getCollec.setRequestUrl(collecUrl);

  		Map<String, String> headers11 = new HashMap<String, String>();
            	headers11.put("X-SUP-APPCID",helper.AppconnID);
            	headers11.put("Accept-Encoding","gzip");
            	((SDMBaseRequest) getCollec).setHeaders(headers11);
        		getCollec.setRequestMethod(SDMBaseRequest.REQUEST_METHOD_GET);
        		Log.d("Performance","Start executing GET Collection Set");
        		start = System.currentTimeMillis();
    		reqMan.makeRequest(getCollec);
    		synchronized (lockObject) {
    			try {
    				lockObject.wait();
    			} catch (InterruptedException e) {
    				//Log.i("tag", "get request failed: " + e.getMessage());
    			}

    		}
    		
    		final List<ISDMODataEntry> list = cache.getSDMODataEntries(collectionId);
    		////Log.i("", "Collection items:"+list);
    		
    		if (list != null && list.size() > 0) {
    			String[] res= new String[list.size()];
    			// final int n = list.size();
    			updated = new ArrayList<CharSequence>();
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
    				//Log.i("", "Display Collec is working fine \n " + updated);
    				res[i]=sb.toString();
    			} 
    		}
    			//Log.i("", "Collection list:" +updated);
			
	                    //serviceDocument1 = new String(decompress(inst));
	                    //Log.d("Performance","End executing GET Collection Set");
	                    
	                    //Log.d("Performance",st);
	                    //Log.d("Performance String",st);
	                    //collectionItems = ParseXML(serviceDocument1);
	                    //collectionItems = ParseXML(st);
    			
	            		adapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_list_item_1,Collections);
	            		setListAdapter(adapter);
	            		ListView lv = getListView();
	            		lv.setVisibility(View.VISIBLE);
	            		lv.setTextFilterEnabled(true);
	            		
	        } catch (Exception e) {
	            e.printStackTrace();
	      }
		Log.d("Performance","End executing parsing and response and storing GET Collection Set");
		 
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

	public String[] ParseXML(String xmlRecords) {
		Log.d("Performance","Start Parsing Collection Set");
		int i=0;
		String[] coll;
	    try {
	        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	        DocumentBuilder dbb = dbf.newDocumentBuilder();
			Document doc = dbb.parse(new ByteArrayInputStream(xmlRecords.getBytes()));
	        NodeList nodes = doc.getElementsByTagName("d:agencynum");
	        NodeList nodes1 = doc.getElementsByTagName("d:NAME");
	        coll = new String[nodes.getLength()];
	        coll[0]="Click on value for more options";
	        for (i = 1; i < nodes.getLength(); i++) {
	        	Node value=nodes.item(i).getChildNodes().item(0);
	        	Node vName=nodes1.item(i).getChildNodes().item(0);
	           String abb =value.getNodeValue();
	           String abbName =vName.getNodeValue();
	           coll[i]= abb+"-"+abbName ;
	        }	        
	        Log.d("Performance","End Parsing Collection Set");
	        return coll;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String getCharacterDataFromElement(Element e) {
		Node child = e.getFirstChild();
		if (child instanceof CharacterData) {
			CharacterData cd = (CharacterData) child;
			return cd.getData();
		}
		return "?";
	}
	
	 public void onListItemClick(ListView parent, View v, int position, long id)
	 {
		 String val = Collections[position];
			String delimiter = "-";
			String[] temp;
			temp = val.split(delimiter);
			val = temp[0];
			helper.AGENCY=val;
			Intent myIntent = new Intent(this, singledata.class);
			startActivity(myIntent);
	 }
	public void MessageBox(String message) {
		Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
	}
	@Override
	public void onError(ISDMRequest arg0, ISDMResponse arg1,
			ISDMRequestStateElement arg2) {
		// TODO Auto-generated method stub
		MessageBox(arg1.getStatusLine().toString());
		synchronized (lockObject) {
			lockObject.notify();
		}
		
	}
	@Override
	public void onSuccess(ISDMRequest arg0, ISDMResponse arg1) {
		
		HttpEntity responseEntity = arg1.getEntity();
		//MessageBox(arg1.getStatusLine().toString());
		//Log.i("tag","For successful GET"+responseEntity.toString());
		if( arg0.equals(getrequest)){
			try {
				Log.d("Performance","End request for service document");
				
				//Log.i("tag","For successful GET" + arg1.getStatusLine());
				
				Log.d("Performance","Start parsing for service document");
				ISDMODataServiceDocument serviceDoc = parser.parseSDMODataServiceDocumentXML(EntityUtils.toString(responseEntity));
				Log.d("Performance","End parsing for service document");
				cache.setSDMODataServiceDocument(serviceDoc);
				
				//Log.i("tag","For successful GET");
				Log.d("Performance","End executing parsing and response and storing service document");
				synchronized (lockObject) {
					lockObject.notify();
				}
			} catch (Exception e){
				synchronized (lockObject) {
					lockObject.notify();
				}
			}
		}		
		
		if( arg0.equals(getSchema)){
			try{
				String xmlString = EntityUtils.toString(responseEntity);
				Log.d("Performance","End request for metadata");
				Log.d("Performance","Start parsing for metadata");
				ISDMODataSchema schema = parser.parseSDMODataSchemaXML(xmlString, cache.getSDMODataServiceDocument());
				Log.d("Performance","End parsing for metadata");
				cache.setSDMODataSchema(schema);
				Log.d("Performance","End executing parsing and response and storing metadata");
				//Log.i("tag","For successful GET metadata" +arg1.getStatusLine());
				synchronized (lockObject) {
					lockObject.notify();
				}
				
			}catch(Exception e){
				synchronized (lockObject) {
					lockObject.notify();
				}
			}
		}
		if (arg0.equals(getCollec)) {
			try {
				Log.i("tag","For successful GET" + arg1.getStatusLine());
				////Log.i("tag",responseEntity.getContentEncoding().getValue());
				//Log.i("tag","in success of get collec");
				//String xmlString = EntityUtils.toString(responseEntity);
				InputStream inst = responseEntity.getContent();
				//String xmlst = readString(inst);
				//Log.i("string",xmlString);

				byte[] a = decompress(inst);
				//byte[] a= SDMUtility.decompress(responseEntity.getContent(),SDMUtility.CONTENT_GZIP);
				Log.d("Performance","End request for GET Collection Set");
				 stop = System.currentTimeMillis();
				Log.d("Performance","Start parsing for GET Collection Set");
				entries = parser.parseSDMODataEntriesXML(new String(a),
						collectionId, cache.getSDMODataSchema());
				
				cache.setSDMODataEntries(entries);
				Log.d("Performance","End parsing of GET Collection Set");
				 end = System.currentTimeMillis();
				 Log.d("libs timing", "Collection Req-Resp = "+(stop-start));
                 Log.d("libs timing", "Collection Parser = "+(end-stop));
                 Log.d("libs timing", "Collection E2E = "+(end-start));
                 //excel.saveExcelFile(getApplicationContext(),"/out.xls","collection",(stop-start),(end-stop),(end-start));
				perst = new SDMPersistence(pref, logger);
				
				perst.storeSDMCache(cache);
				
				cache.clear();
				
				perst.loadSDMCache(cache);
				
				Log.d("Performance","End executing parsing and response and storing GET Collection Set");
				
				//Log.i("tag","For successful GET Collection" + arg1.getStatusLine());
						
				synchronized (lockObject) {
			       //Log.i("tag","in try sync of get collec");
					lockObject.notify();
				}

			} catch (Exception e) {
				e.printStackTrace();
			
				synchronized (lockObject) {
					lockObject.notify();
				}
			}
		}

		
	}
	

}
