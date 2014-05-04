package com.sap.manoj.smp_libs;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

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
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class singledata extends Activity  implements ISDMNetListener{
	public long start,stop,end;
	public String anumber;
	public String aname;
	public String astreet;
	public String acity;
	public String acountry;
	public String aphone;
	public String aurl;
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
	public String[] collectionItems;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.singledata);
		final EditText number_e = (EditText)findViewById(R.id.numberText);
		final EditText name_e = (EditText)findViewById(R.id.nameText);
		final EditText street_e = (EditText)findViewById(R.id.streetText);
		final EditText city_e = (EditText)findViewById(R.id.cityText);
		final EditText country_e = (EditText)findViewById(R.id.countryText);
		final EditText phone_e = (EditText)findViewById(R.id.phoneText);
		final EditText url_e = (EditText)findViewById(R.id.urlText);
		final Button update_b = (Button)findViewById(R.id.update);
		final Button delete_b = (Button)findViewById(R.id.delete);
		final Button add_b = (Button)findViewById(R.id.add);
		load_data();
		number_e.setText(anumber);
		name_e.setText(aname);
		street_e.setText(astreet);
		city_e.setText(acity);
		country_e.setText(acountry);
		phone_e.setText(aphone);
		url_e.setText(aurl);
		add_b.setOnClickListener(new OnClickListener() {           
	    	@Override 
	        public void onClick(View v) { 
	    		anumber = number_e.getText().toString();
	    		aname = name_e.getText().toString();
	    		astreet = street_e.getText().toString();
	    		acity = city_e.getText().toString();
	    		acountry = country_e.getText().toString();
	    		aphone = phone_e.getText().toString();
	    		aurl = url_e.getText().toString();
	    		add_record();
	        }
	    });
		update_b.setOnClickListener(new OnClickListener() {           
	    	@Override 
	        public void onClick(View v) {
	    		anumber = number_e.getText().toString();
	    		aname = name_e.getText().toString();
	    		astreet = street_e.getText().toString();
	    		acity = city_e.getText().toString();
	    		acountry = country_e.getText().toString();
	    		aphone = phone_e.getText().toString();
	    		aurl = url_e.getText().toString();
	    		update_record();
	        }
	    }); 
		delete_b.setOnClickListener(new OnClickListener() {           
	    	@Override 
	        public void onClick(View v) { 
	    		anumber = number_e.getText().toString();
	    		delete_record();
	        }
	    }); 
	}
	public void ParseXML(String xmlRecords)
	{
		Log.d("Performance","Start Parsing of GET single record");
		int i=0;
	    try {
	        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	        DocumentBuilder db = dbf.newDocumentBuilder();
	        InputSource is = new InputSource();
	        is.setCharacterStream(new StringReader(xmlRecords));
	        Document doc = db.parse(is);
	        NodeList nodes = doc.getElementsByTagName("m:properties");
	        for (i = 0; i < nodes.getLength(); i++) {
                  Node node = nodes.item(i);
              if (node.getNodeType() == Node.ELEMENT_NODE)
              {
            	  Element element =(Element) node;
            	  NodeList nodelist = element.getElementsByTagName("d:agencynum");
            	  Element element1= (Element) nodelist.item(0);
            	  NodeList fstname = element1.getChildNodes();
            	  anumber = (fstname.item(0)).getNodeValue();
            	  
            	  NodeList nodelist1 = element.getElementsByTagName("d:TELEPHONE");
            	  Element element2= (Element) nodelist1.item(0);
            	  NodeList fstname1 = element2.getChildNodes();
            	  aphone = (fstname1.item(0).getNodeValue());
            	  
            	  NodeList nodelist2 = element.getElementsByTagName("d:URL");
            	  Element element3= (Element) nodelist2.item(0);
            	  NodeList fstname2 = element3.getChildNodes();
            	  aurl = (fstname2.item(0).getNodeValue());

            	  NodeList nodelistA = element.getElementsByTagName("d:NAME");
            	  Element element1A= (Element) nodelistA.item(0);
            	  NodeList fstnameA = element1A.getChildNodes();
            	  aname = (fstnameA.item(0).getNodeValue());
            	  
            	  NodeList nodelistB = element.getElementsByTagName("d:STREET");
            	  Element element2B= (Element) nodelistB.item(0);
            	  NodeList fstname1B = element2B.getChildNodes();
            	  astreet = (fstname1B.item(0).getNodeValue());
            	 
            	  NodeList nodelistC = element.getElementsByTagName("d:CITY");
            	  Element element2C= (Element) nodelistC.item(0);
            	  NodeList fstname1C = element2C.getChildNodes();
            	  acity = (fstname1C.item(0).getNodeValue());
            	  
            	  NodeList nodelist2D = element.getElementsByTagName("d:COUNTRY");
            	  Element element3D= (Element) nodelist2D.item(0);
            	  NodeList fstname2D = element3D.getChildNodes();
            	  acountry = (fstname2D.item(0).getNodeValue());
            	  Log.d("Performance","End Parsing of Single Record");
              }	          
	        }
	    }
	    catch (Exception e) {
	        e.printStackTrace();
	    } 
	  }

	public static String getCharacterDataFromElement(Element e) {
	    Node child = e.getFirstChild();
	    if (child instanceof CharacterData) {
	       CharacterData cd = (CharacterData) child;
	       return cd.getData();
	    }
	    return "?";
	  }
	
	public void MessageBox(String message)
	{
		Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
	}
	public static byte[] decompress(InputStream in) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
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
	public void load_data() {
			try
		{
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
    	getrequest.setListener(singledata.this);
    	getrequest.setRequestUrl(helper.serviceDocUrl+"TravelagencyCollection('" + helper.AGENCY +"')");
    	getrequest.setRequestMethod(SDMBaseRequest.REQUEST_METHOD_GET);    	
   	Map<String, String> headers = new HashMap<String, String>();
   	headers.put("X-SUP-APPCID",helper.AppconnID);
   	getrequest.setHeaders(headers);
   	Log.d("Performance","Start executing GET single record");
   	start = System.currentTimeMillis();
   	reqMan.makeRequest(getrequest);
		synchronized (lockObject) {
			try {
				lockObject.wait();
			} catch (InterruptedException e) {
				//Log.i("tag","in catch of get service doc"+e);
			}
		}
		
		 } catch (Exception e) {
	            e.printStackTrace();
	      }
  }
	
	public void add_record()
	{
		//anumber = "11000042";
	//	String URL = "https://" + helper.IP +"/smp/online/"+helper.APPNAME +"TravelagencyCollection";
		//String URL = "http://smpper12sapperf2.prod.jpaas.sapbydesign.com/smp/online/supa/TravelagencyCollection";
		String data ="<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
				+"<entry\n"
				+"xml:base=\"http://ldcig8p.wdf.sap.corp:50018/sap/opu/odata/iwfnd/RMTSAMPLEFLIGHT/\"\n"
				+"xmlns=\"http://www.w3.org/2005/Atom\" xmlns:m=\"http://schemas.microsoft.com/ado/2007/08/dataservices/metadata\"\n"
				+"xmlns:d=\"http://schemas.microsoft.com/ado/2007/08/dataservices\">\n"
				+"<id>http://ldcig8p.wdf.sap.corp:50018/sap/opu/odata/iwfnd/RMTSAMPLEFLIGHT/TravelagencyCollection('"+anumber+"')</id>\n"
				+"<title type=\"text\">TravelagencyCollection('"+anumber+"')</title>\n"
				+"<category term=\"RMTSAMPLEFLIGHT.Travelagency\"\n"
					+"scheme=\"http://schemas.microsoft.com/ado/2007/08/dataservices/scheme\" />\n"
					+"<link href=\"TravelagencyCollection('"+anumber+"')\" rel=\"edit\" title=\"Travelagency\"/>\n"
				+"<content type=\"application/atom+xml\">\n"
					+"<m:properties>\n"
						+"<d:agencynum>"+anumber+"</d:agencynum>\n"
						+"<d:NAME>"+aname+"</d:NAME>\n"
						+"<d:STREET>"+astreet+"</d:STREET>\n"
						+"<d:POSTBOX />\n"
						+"<d:POSTCODE>28779</d:POSTCODE>\n"
						+"<d:CITY>"+acity+"</d:CITY>\n"
						+"<d:COUNTRY>"+acountry+"</d:COUNTRY>\n"
						+"<d:REGION>04</d:REGION>\n"
						+"<d:TELEPHONE>"+aphone+"</d:TELEPHONE>\n"
						+"<d:URL>"+aurl+"</d:URL>\n"
						+"<d:LANGU>D</d:LANGU>\n"
						+"<d:CURRENCY>EUR</d:CURRENCY>\n"
						+"<d:mimeType>text/html</d:mimeType>\n"
					+"</m:properties>\n"
				+"</content>\n"
			+"</entry>\n";
	 
		try
		{
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
    	addrequest.setListener(singledata.this);
    	addrequest.setRequestUrl(helper.serviceDocUrl+"TravelagencyCollection");
    	addrequest.setRequestMethod(SDMBaseRequest.REQUEST_METHOD_POST);    	
   	Map<String, String> headers = new HashMap<String, String>();
   	headers.put("X-SUP-APPCID",helper.AppconnID);
   	headers.put("Content-Type", "application/atom+xml");
   	headers.put("X-Requested-With", "XMLHttpRequest");
   	addrequest.setHeaders(headers);
   	byte[] theByteArray = data.getBytes();
    ((SDMBaseRequest) addrequest).setData(theByteArray);
    Log.d("Performance","Start executing POST(add) single record");
    start = System.currentTimeMillis();
   	reqMan.makeRequest(addrequest);
		synchronized (lockObject) {
			try {
				lockObject.wait();
			} catch (InterruptedException e) {
				//Log.i("tag","in catch of get service doc"+e);
			}
		}
		
		 } catch (Exception e) {
	            e.printStackTrace();
	      }
		
	}
	
	public void update_record()
	{
	//	String URL = "https://" + helper.IP +"/smp/online/"+helper.APPNAME +"/TravelagencyCollection('"+anumber+"')";
		//String URL = "http://smpper12sapperf2.prod.jpaas.sapbydesign.com/smp/online/supa/TravelagencyCollection('"+anumber+"')";
		String data ="<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
				+"<entry\n"
				+"xml:base=\"http://ldcig8p.wdf.sap.corp:50018/sap/opu/odata/iwfnd/RMTSAMPLEFLIGHT/\"\n"
				+"xmlns=\"http://www.w3.org/2005/Atom\" xmlns:m=\"http://schemas.microsoft.com/ado/2007/08/dataservices/metadata\"\n"
				+"xmlns:d=\"http://schemas.microsoft.com/ado/2007/08/dataservices\">\n"
				+"<id>http://ldcig8p.wdf.sap.corp:50018/sap/opu/odata/iwfnd/RMTSAMPLEFLIGHT/TravelagencyCollection('"+anumber+"')</id>\n"
				+"<title type=\"text\">TravelagencyCollection('"+anumber+"')</title>\n"
				+"<category term=\"RMTSAMPLEFLIGHT.Travelagency\"\n"
					+"scheme=\"http://schemas.microsoft.com/ado/2007/08/dataservices/scheme\" />\n"
					+"<link href=\"TravelagencyCollection('"+anumber+"')\" rel=\"edit\" title=\"Travelagency\"/>\n"
				+"<content type=\"application/atom+xml\">\n"
					+"<m:properties>\n"
						+"<d:agencynum>"+anumber+"</d:agencynum>\n"
						+"<d:NAME>"+aname+"</d:NAME>\n"
						+"<d:STREET>"+astreet+"</d:STREET>\n"
						+"<d:POSTBOX />\n"
						+"<d:POSTCODE>28779</d:POSTCODE>\n"
						+"<d:CITY>"+acity+"</d:CITY>\n"
						+"<d:COUNTRY>"+acountry+"</d:COUNTRY>\n"
						+"<d:REGION>04</d:REGION>\n"
						+"<d:TELEPHONE>"+aphone+"</d:TELEPHONE>\n"
						+"<d:URL>"+aurl+"</d:URL>\n"
						+"<d:LANGU>D</d:LANGU>\n"
						+"<d:CURRENCY>EUR</d:CURRENCY>\n"
						+"<d:mimeType>text/html</d:mimeType>\n"
					+"</m:properties>\n"
				+"</content>\n"
			+"</entry>\n";

		try
		{
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
    	updaterequest.setListener(singledata.this);
    	updaterequest.setRequestUrl(helper.serviceDocUrl+"TravelagencyCollection('" + anumber +"')");
    	updaterequest.setRequestMethod(SDMBaseRequest.REQUEST_METHOD_PUT);    	
   	Map<String, String> headers = new HashMap<String, String>();
   	headers.put("X-SUP-APPCID",helper.AppconnID);
   	headers.put("Content-Type", "application/atom+xml");
   	headers.put("X-Requested-With", "XMLHttpRequest");
   	updaterequest.setHeaders(headers);
   	byte[] theByteArray = data.getBytes();
    ((SDMBaseRequest) updaterequest).setData(theByteArray);
    Log.d("Performance","Start executing PUT(update) for single record");
    start = System.currentTimeMillis();
   	reqMan.makeRequest(updaterequest);
		synchronized (lockObject) {
			try {
				lockObject.wait();
			} catch (InterruptedException e) {
				//Log.i("tag","in catch of get service doc"+e);
			}
		}
		
		 } catch (Exception e) {
	            e.printStackTrace();
	      }
     return;
	}
	
	public void delete_record()
	{
		
		try
		{
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
		//cache.clear();	
   	
    	//get service doc
    	deleterequest.setListener(singledata.this);
    	deleterequest.setRequestUrl(helper.serviceDocUrl+"TravelagencyCollection('" + anumber +"')");
    	deleterequest.setRequestMethod(SDMBaseRequest.REQUEST_METHOD_DELETE);    	
   	Map<String, String> headers = new HashMap<String, String>();
   	headers.put("X-SUP-APPCID",helper.AppconnID);
   	headers.put("Content-Type", "application/atom+xml");
   	headers.put("X-Requested-With", "XMLHttpRequest");
   	deleterequest.setHeaders(headers);
   	Log.d("Performance","Start executing delete of single record");
   	start = System.currentTimeMillis();
   	reqMan.makeRequest(deleterequest);
		synchronized (lockObject) {
			try {
				lockObject.wait();
			} catch (InterruptedException e) {
				//Log.i("tag","in catch of get service doc"+e);
			}
		}
		
		 } catch (Exception e) {
	            e.printStackTrace();
	      }
		
	}
	@Override
	public void onError(ISDMRequest arg0, ISDMResponse arg1,
			ISDMRequestStateElement arg2) {
		// TODO Auto-generated method stub
		Log.i("tag","For unsuccessful" + arg1.getStatusLine());
		MessageBox(arg1.getStatusLine().toString());
		synchronized (lockObject) {
			lockObject.notify();
		}
		
	}
	@Override
	public void onSuccess(ISDMRequest arg0, ISDMResponse arg1) {
		// TODO Auto-generated method stub
		HttpEntity responseEntity = arg1.getEntity();
		//Log.i("tag","on success"+responseEntity.toString());
		if( arg0.equals(getrequest)){
			try {
				Log.d("Performance","End executing GET single record");
				stop = System.currentTimeMillis();
				Log.i("tag","For successful GET" + arg1.getStatusLine());
				//MessageBox(arg1.getStatusLine().toString());
				ParseXML(EntityUtils.toString(responseEntity));
				Log.d("Performance","End parsing and request single record");
				end = System.currentTimeMillis();
				//Log.i("tag","For successful GET");
				 Log.d("libs timing", "get single data Req-Resp = "+(stop-start));
                 Log.d("libs timing", "get single data Parser = "+(end-stop));
                 Log.d("libs timing", "get single data E2E = "+(end-start));
				synchronized (lockObject) {
					lockObject.notify();
				}
			} catch (Exception e){
				synchronized (lockObject) {
					lockObject.notify();
				}
			}
		}	
		
		if( arg0.equals(addrequest)){
			try {
				Log.i("tag","For successful add" + arg1.getStatusLine());
				//MessageBox(arg1.getStatusLine().toString());
				Log.d("Performance","End executing POST(add) single record");
				
				stop = System.currentTimeMillis();
	 
				//Log.i("tag","For successful add");
				Log.d("libs timing", "Add entry = "+(stop-start));
				//excel.saveExcelFile(getApplicationContext(),"/out.xls","add",(stop-start),0,0);
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
				//MessageBox(arg1.getStatusLine().toString());
				Log.d("Performance","End executing PUT(update) for single record");
				stop = System.currentTimeMillis();
				//Log.i("tag","For successful update");
				Log.d("libs timing", "update entry = "+(stop-start));
				//excel.saveExcelFile(getApplicationContext(),"/out.xls","update",(stop-start),0,0);
				synchronized (lockObject) {
					lockObject.notify();
				}
			} catch (Exception e){
				synchronized (lockObject) {
					lockObject.notify();
				}
			}
		}
		
		if( arg0.equals(deleterequest)){
			try {
				Log.i("tag","For successful DELETE" + arg1.getStatusLine());
				stop = System.currentTimeMillis();
				//MessageBox(arg1.getStatusLine().toString());
				Log.d("Performance","End executing delete of single record");
				Log.d("libs timing", "delete entry = "+(stop-start));
				//excel.saveExcelFile(getApplicationContext(),"/out.xls","delete",(stop-start),0,0);
				//Log.i("tag","For successful delete");
				
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
