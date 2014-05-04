package com.sap.manoj.smp_libs;

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

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.sap.mobile.lib.cache.Cache;
import com.sap.mobile.lib.cache.CacheException;
import com.sap.mobile.lib.configuration.Constants;
import com.sap.mobile.lib.configuration.IPreferences;
import com.sap.mobile.lib.configuration.Preferences;
import com.sap.mobile.lib.parser.IODataCollection;
import com.sap.mobile.lib.parser.IODataEntry;
import com.sap.mobile.lib.parser.IODataFeed;
import com.sap.mobile.lib.parser.IODataSchema;
import com.sap.mobile.lib.parser.IODataServiceDocument;
import com.sap.mobile.lib.parser.Parser;
import com.sap.mobile.lib.persistence.EncryptionKeyManager;
import com.sap.mobile.lib.request.BaseRequest;
import com.sap.mobile.lib.request.ConnectivityParameters;
import com.sap.mobile.lib.request.INetListener;
import com.sap.mobile.lib.request.IRequest;
import com.sap.mobile.lib.request.IRequestStateElement;
import com.sap.mobile.lib.request.IResponse;
import com.sap.mobile.lib.request.RequestManager;
import com.sap.mobile.lib.supportability.ILogger;
import com.sap.mobile.lib.supportability.Logger;
import com.sybase.persistence.PrivateDataVault;

public class listcollection extends ListActivity  implements INetListener{
	public long start,stop,end;
	public	String responseText = null;
	public String UN,PWD;
	protected  String[] Collections;
	
	//protected SDMPersistence perst;
	public String collections=null;
	public static IODataSchema schema1;
	protected Parser parser;
	protected Cache cache;
	public static String key = null;
	public static String vaultKey = null;
	public ConnectivityParameters param;
	
	IODataFeed entries;
	public static List<IODataEntry> reads;
	public static List<IODataCollection> collectionsList;
	String collectionId = "TravelAgencies_DQ";
	public static ArrayList<CharSequence> updated;
	protected ArrayAdapter<CharSequence> adapter;
	protected ILogger logger;
	protected IPreferences pref;
	protected RequestManager reqMan;
	private Object lockObject = new Object();
	public IRequest getrequest = new BaseRequest();
	public BaseRequest getSchema = new BaseRequest();
	protected static BaseRequest getCollec = new BaseRequest();
	protected static BaseRequest getdelta = new BaseRequest();
	public String[] collectionItems;
	public String cacheURL=helper.serviceDocUrl + helper.collectionId;
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display);
        setkey();
        ILogger logger1 = new Logger();
        helper.ofcache = new Cache(this.getApplicationContext(), logger1);
		helper.ofcache.initializeCache();
		helper.lcache = new Cache(this.getApplicationContext(), logger1);
		helper.lcache.initializeCache();
        final Button fetch = (Button)findViewById(R.id.fetch);
        fetch.setOnClickListener(new OnClickListener() {          
	    	@Override 
	        public void onClick(View v) { 
	    		fetch_collection();
	        }
	    });  
        final Button store = (Button)findViewById(R.id.buttonstore);
        store.setOnClickListener(new OnClickListener() {           
	    	@Override 
	        public void onClick(View v) { 
	    		store_collection();
	        }
	    });
        final Button retrieve = (Button)findViewById(R.id.buttonretrieve);
        retrieve.setOnClickListener(new OnClickListener() {           
	    	@Override 
	        public void onClick(View v) { 
	    		retrieve_collection();
	        }
	    });
        final Button clear = (Button)findViewById(R.id.buttonclear);
        clear.setOnClickListener(new OnClickListener() {           
	    	@Override 
	        public void onClick(View v) { 
	    		clear_cache();
	        }
	    });
        final Button changes = (Button)findViewById(R.id.buttonchange);
        changes.setOnClickListener(new OnClickListener() {           
	    	@Override 
	        public void onClick(View v) { 
	    		server_cache();
	        }
	    });
        final Button delta = (Button)findViewById(R.id.buttondelta);
        delta.setOnClickListener(new OnClickListener() {           
	    	@Override 
	        public void onClick(View v) { 
	    		fetch_delta();
	        }
	    });
	}
	
	public void server_cache()
	{
		//update ten records
		for(int i=0;i<10;i++)
		{
			store_collection();
			retrieve_collection();
			clear_cache();
		}
	}
	
	public void fetch_delta()
	{
		//do request with deltalink
		try{
			String collecUrl = helper.deltalink;
			getdelta.setListener(listcollection.this);
			getdelta.setRequestUrl(collecUrl);
		logger = new Logger();
    	pref = new Preferences(this.getApplicationContext() , logger);
    	//ConnectivityParameters param = new ConnectivityParameters();
    	pref.setIntPreference(IPreferences.CONNECTIVITY_CONNTIMEOUT, 75000);
    	pref.setIntPreference(IPreferences.CONNECTIVITY_SCONNTIMEOUT, 75000);
    	pref.setStringPreference(IPreferences.CONNECTIVITY_HANDLER_CLASS_NAME, Constants.HTTP_HANDLER_CLASS);
    	pref.setBooleanPreference(IPreferences.PERSISTENCE_SECUREMODE, false);
    	logger.setLogLevel(ILogger.DEBUG);
    	param = new ConnectivityParameters(); 
    	param.setUserName(helper.userName);                    // username for all other onboardings except CERT.
    	UN= helper.userName;
        param.setUserPassword(helper.password);                // password for all other onboardings except CERT.	
        PWD = helper.password;
		
        param.enableXsrf(true);
    	reqMan = new RequestManager(logger, pref, param, 1); 
    	parser = new Parser(pref, logger);
		

		Map<String, String> headers11 = new HashMap<String, String>();
        	headers11.put("X-SUP-APPCID",helper.AppconnID);
        	//headers11.put("Accept-Encoding","gzip");
        	((BaseRequest) getdelta).setHeaders(headers11);
    		getdelta.setRequestMethod(BaseRequest.REQUEST_METHOD_GET);
    		Log.d("Performance","Start executing GET Collection Set");
    		start = System.currentTimeMillis();
		reqMan.makeRequest(getdelta);
		synchronized (lockObject) {
			try {
				lockObject.wait();
			} catch (InterruptedException e) {
				//Log.i("tag", "get request failed: " + e.getMessage());
			}
		}
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void store_collection(){
		start = System.currentTimeMillis();
		try {
			Log.d("Performance", "storing starting");
			helper.ofcache.mergeEntries(entries, cacheURL);
			Log.d("Performance", "storing done");
			helper.deltalink = helper.ofcache.getDeltaLink(cacheURL);
			Log.d("Performance", "delta link fetched "+helper.deltalink);
		} catch (CacheException e) {
			e.printStackTrace();
		}
		stop = System.currentTimeMillis();
		Log.d("Performance","Time taken to store cache "+ entries.getEntries().size() +"= " + (stop-start));
	}
	
	public void clear_cache()
	{
		start = System.currentTimeMillis();
		try {
			//helper.ofcache = null;
			//ILogger logger1 = new Logger();
	        //helper.ofcache = new Cache(this.getApplicationContext(), logger1);
			//helper.ofcache.initializeCache();
			helper.ofcache.clearCache(cacheURL);
		} catch (Exception e) {
			e.printStackTrace();
		}
		stop = System.currentTimeMillis();
		Log.d("Performance","Time taken to clear cache = " + (stop-start));
	}
	
	
	private void setkey() {
		vaultKey = "12345";

		key = null;

		try {

			PrivateDataVault.init(getApplicationContext());

			if (PrivateDataVault.vaultExists("VaultForPersistenceKey")) {

				PrivateDataVault ldv = PrivateDataVault
						.getVault("VaultForPersistenceKey");

				ldv.unlock(vaultKey, "salt");

				key = ldv.getString("cipherkey");

				ldv.lock();

			} else {

				PrivateDataVault ldv = PrivateDataVault.createVault(
						"VaultForPersistenceKey", vaultKey, "salt");

				key = EncryptionKeyManager
						.getEncryptionKey(getApplicationContext());
				//Log.i("Offline", "Key Generated:" + key);
				ldv.setString("cipherkey", key);

				ldv.lock();

			}
			// // EncryptionKeyManager encKeyManager = new
			// EncryptionKeyManager();
			// // encKeyManager.resetEncryptionKey(getApplicationContext());

//			EncryptionKeyManager.setEncryptionKey(key, getApplicationContext());

		} catch (Exception e) {
			Log.i("Offline", "Inside exception in Encryption:" + e.getMessage());
		}

	}
   
	public void retrieve_collection(){
		start = System.currentTimeMillis();
		try {
			reads = helper.ofcache.readEntriesServer(cacheURL);
			//Log.i("Offline", "Read Entries Done");
		} catch (CacheException e) {
			e.printStackTrace();
		}
		stop = System.currentTimeMillis();
		Log.d("Performance","Time taken to retrieve cache "+ reads.size() +"= " + (stop-start));
		if (reads != null && reads.size() > 0) {
	            for (int i = 0; i < reads.size(); ++i) {

	                IODataEntry entry1 = reads.get(i);

	                StringBuffer sb = new StringBuffer();

	                sb.append(entry1.getPropertyValue("agencynum")+"-"+entry1.getPropertyValue("NAME"));

	                //Log.i("Offline", "Value Server : " + sb.toString());

	        }

	        }
		try {
			helper.reads = helper.ofcache.readEntriesServer(helper.serviceDocUrl + helper.collectionId);
		} catch (CacheException e2) {
			e2.printStackTrace();
		}
		
	}
	
	public static byte[] decompress(InputStream in) throws IOException {
		 ByteArrayOutputStream baos = new ByteArrayOutputStream();
	        //ByteArrayInputStream bis = (ByteArrayInputStream) in;
	        InputStream is = null;
	        try {

	              is = new GZIPInputStream((InputStream) in);

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
		ArrayAdapter<CharSequence> adapter;
		try {
				
			logger = new Logger();
        	pref = new Preferences(this.getApplicationContext() , logger);
        	//ConnectivityParameters param = new ConnectivityParameters();
        	pref.setIntPreference(IPreferences.CONNECTIVITY_CONNTIMEOUT, 75000);
        	pref.setIntPreference(IPreferences.CONNECTIVITY_SCONNTIMEOUT, 75000);
        	pref.setStringPreference(IPreferences.CONNECTIVITY_HANDLER_CLASS_NAME, Constants.HTTP_HANDLER_CLASS);
        	pref.setBooleanPreference(IPreferences.PERSISTENCE_SECUREMODE, false);
        	logger.setLogLevel(ILogger.DEBUG);
        	param = new ConnectivityParameters(); 
        	param.setUserName(helper.userName);                    // username for all other onboardings except CERT.
        	UN= helper.userName;
            param.setUserPassword(helper.password);                // password for all other onboardings except CERT.	
            PWD = helper.password;
			
            param.enableXsrf(true);
        	reqMan = new RequestManager(logger, pref, param, 1); 
        	parser = new Parser(pref, logger);
			//cache = new Cache(pref, logger);
				
       	
        	//get service doc
        	getrequest.setListener(listcollection.this);
        	getrequest.setRequestUrl(helper.serviceDocUrl);
        	getrequest.setRequestMethod(BaseRequest.REQUEST_METHOD_GET);    	
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
    		getSchema.setRequestMethod(BaseRequest.REQUEST_METHOD_GET); 		
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
            	headers11.put("Content-Type", "application/atom+xml"); 
            	((BaseRequest) getCollec).setHeaders(headers11);
        		getCollec.setRequestMethod(BaseRequest.REQUEST_METHOD_GET);
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
    		
    		final List<IODataEntry> list = entries.getEntries();
    		Collections = new String[list.size()];
    		if (list!= null && list.size()> 0) {
				for (int i = 0; i < list.size(); ++i) {

					IODataEntry entry1 = list.get(i);
					StringBuffer sb = new StringBuffer();
					sb.append(entry1.getPropertyValue("agencynum") + "-" + entry1.getPropertyValue("NAME"));
					Collections[i] = sb.toString();
					//Log.i("Offline", "Value Server : " + sb.toString());
				}
    		}
    		adapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_list_item_1,Collections);
    		setListAdapter(adapter);
    		ListView lv = getListView();
    		lv.setVisibility(View.VISIBLE);
    		lv.setTextFilterEnabled(true);
    		
	            		
	        } catch (Exception e) {
	            e.printStackTrace();
	      }
		//Log.d("Performance","End executing parsing and response and storing GET Collection Set");
		 
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
	public void onError(IRequest arg0, IResponse arg1,
			IRequestStateElement arg2) {
		Log.d("Performance","on Error"+arg1.getStatusLine());
		//MessageBox(arg1.getStatusLine().toString());
		synchronized (lockObject) {
			lockObject.notify();
		}
		
	}
	@Override
	public void onSuccess(IRequest arg0, IResponse arg1) {
		
		HttpEntity responseEntity = arg1.getEntity();
		//MessageBox(arg1.getStatusLine().toString());
		//Log.i("tag","For successful GET"+responseEntity.toString());
		if( arg0.equals(getrequest)){
			try {
				Log.d("Performance","End request for service document");
				
				//Log.i("tag","For successful GET" + arg1.getStatusLine());
				
				Log.d("Performance","Start parsing for service document");
				helper.serviceDoc = parser.parseODataServiceDocument(EntityUtils.toString(responseEntity));
				Log.d("Performance","End parsing for service document");
				//cache.setODataServiceDocument(serviceDoc);
				
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
				helper.schema = parser.parseODataSchema(xmlString, helper.serviceDoc);
				Log.d("Performance","End parsing for metadata");
				
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
				//byte[] a= SDMUtility.decompress(responseEntity.getContent(),bUtility.CONTENT_GZIP);
				Log.d("Performance","End request for GET Collection Set");
				//String st = new String(a);
				//Log.d("Performance",st);
				 stop = System.currentTimeMillis();
				Log.d("Performance","Start parsing for GET Collection Set");
				entries = parser.parseODataFeed(new String(a),collectionId,helper.schema);
				Log.d("Performance","End parsing of GET Collection Set"+entries.getEntries().size());
				 end = System.currentTimeMillis();
				 Log.d("libs timing", "Collection Req-Resp = "+(stop-start));
                 Log.d("libs timing", "Collection Parser = "+(end-stop));
                 Log.d("libs timing", "Collection E2E = "+(end-start));
				
                 //excel.saveExcelFile(getApplicationContext(),"/out.xls","collection",(stop-start),(end-stop),(end-start));
				//perst = new SDMPersistence(pref, logger);
				
				//perst.storeSDMCache(cache);
				
				//cache.clear();
				
				//perst.loadSDMCache(cache);
				
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
		if (arg0.equals(getdelta)) {
			try {
				Log.i("tag","For successful GET" + arg1.getStatusLine());
				////Log.i("tag",responseEntity.getContentEncoding().getValue());
				//Log.i("tag","in success of get collec");
				//String xmlString = EntityUtils.toString(responseEntity);
				InputStream inst = responseEntity.getContent();
				String xmlst = readString(inst);
				//Log.i("string",xmlString);

				//byte[] a = decompress(inst);
				//byte[] a= SDMUtility.decompress(responseEntity.getContent(),bUtility.CONTENT_GZIP);
				Log.d("Performance","End request for GET Collection Set");
				//String st = new String(a);
				//Log.d("Performance",st);
				 stop = System.currentTimeMillis();
				Log.d("Performance","Start parsing for GET Collection Set");
				entries = parser.parseODataFeed(xmlst,collectionId,helper.schema);
				Log.d("Performance","End parsing of GET Collection Set");
				 end = System.currentTimeMillis();
				 Log.d("libs timing", "Collection Req-Resp = "+(stop-start));
                 Log.d("libs timing", "Collection Parser = "+(end-stop));
                 Log.d("libs timing", "Collection E2E = "+(end-start));
				Log.d("data",xmlst);
                 //excel.saveExcelFile(getApplicationContext(),"/out.xls","collection",(stop-start),(end-stop),(end-start));
				//perst = new SDMPersistence(pref, logger);
				
				//perst.storeSDMCache(cache);
				
				//cache.clear();
				
				//perst.loadSDMCache(cache);
				
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
