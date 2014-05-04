package com.sap.manoj.smp_libs;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.sap.mobile.lib.cache.CacheException;
import com.sap.mobile.lib.configuration.Constants;
import com.sap.mobile.lib.configuration.IPreferences;
import com.sap.mobile.lib.configuration.Preferences;
import com.sap.mobile.lib.parser.IODataCollection;
import com.sap.mobile.lib.parser.IODataEntry;
import com.sap.mobile.lib.parser.IODataFeed;
import com.sap.mobile.lib.parser.IODataSchema;
import com.sap.mobile.lib.parser.IODataServiceDocument;
import com.sap.mobile.lib.parser.ODataEntry;
import com.sap.mobile.lib.parser.Parser;
import com.sap.mobile.lib.parser.ParserException;
import com.sap.mobile.lib.request.BaseRequest;
import com.sap.mobile.lib.request.ConnectivityParameters;
import com.sap.mobile.lib.request.INetListener;
import com.sap.mobile.lib.request.IRequest;
import com.sap.mobile.lib.request.IRequestStateElement;
import com.sap.mobile.lib.request.IResponse;
import com.sap.mobile.lib.request.RequestManager;
import com.sap.mobile.lib.supportability.ILogger;
import com.sap.mobile.lib.supportability.Logger;


public class singledata extends Activity  implements INetListener{
	public long start,stop,end,startcac,stopcac;
	public static String eIDPost, eIDPut, eIDDel;
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
	//protected Persistence perst;
	public String collections=null;
	public static IODataSchema schema1;
	protected Parser parser;
	//protected Cache cache;
	public static IODataServiceDocument serviceDoc;
	public static IODataFeed entries;
	public static List<IODataCollection> collectionsList;
	String collectionId = "TravelAgencies_DQ";
	public static ArrayList<CharSequence> updated;
	protected ArrayAdapter<CharSequence> adapter;
	protected Logger logger;
	protected Preferences pref;
	protected RequestManager reqMan;
	private Object lockObject = new Object();
	public IRequest getrequest = new BaseRequest();
	public IRequest addrequest = new BaseRequest();
	public IRequest deleterequest = new BaseRequest();
	public IRequest updaterequest = new BaseRequest();
	public BaseRequest getSchema = new BaseRequest();
	public IODataEntry entrypost,entryput,entry1,entrydel;
	public static List<IODataEntry> localentries, tempentries;
	protected static BaseRequest getCollec = new BaseRequest();
	public ConnectivityParameters param = new ConnectivityParameters();
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
		final Button update_o = (Button)findViewById(R.id.update_offline);
		final Button delete_o = (Button)findViewById(R.id.delete_offline);
		final Button add_o = (Button)findViewById(R.id.add_offline);
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
		
		
		add_o.setOnClickListener(new OnClickListener() {           
	    	@Override 
	        public void onClick(View v) { 
	    		anumber = number_e.getText().toString();
	    		aname = name_e.getText().toString();
	    		astreet = street_e.getText().toString();
	    		acity = city_e.getText().toString();
	    		acountry = country_e.getText().toString();
	    		aphone = phone_e.getText().toString();
	    		aurl = url_e.getText().toString();
	    		add_off_record();
	        }
	    });
		update_o.setOnClickListener(new OnClickListener() {           
	    	@Override 
	        public void onClick(View v) {
	    		anumber = number_e.getText().toString();
	    		aname = name_e.getText().toString();
	    		astreet = street_e.getText().toString();
	    		acity = city_e.getText().toString();
	    		acountry = country_e.getText().toString();
	    		aphone = phone_e.getText().toString();
	    		aurl = url_e.getText().toString();
	    		update_off_record();
	        }
	    }); 
		delete_o.setOnClickListener(new OnClickListener() {           
	    	@Override 
	        public void onClick(View v) { 
	    		anumber = number_e.getText().toString();
	    		delete_off_record();
	        }
	    }); 
		
		
	}
	/*public void ParseXML(String xmlRecords)
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
	  }*/

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
		logger = new Logger();
    	pref = new Preferences(this.getApplicationContext() , logger);
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
		//cache.clear();	
   	
    	//get service doc
    	getrequest.setListener(singledata.this);
    	getrequest.setRequestUrl(helper.serviceDocUrl+"TravelAgencies_DQ('" + helper.AGENCY +"')");
    	getrequest.setRequestMethod(BaseRequest.REQUEST_METHOD_GET);    	
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
	
	public void add_off_record()
	{
		String url = helper.serviceDocUrl+"TravelAgencies_DQ";
		for(int i=900;i<1000;i++)
		{
			
		entrypost = new ODataEntry();
		entrypost = helper.reads.get(900);
		// entry.putId("Save");
		/*entrypost.putPropertyValue("agencynum", anumber);
		entrypost.putPropertyValue("NAME", aname);
		entrypost.putPropertyValue("STREET", astreet);
		entrypost.putPropertyValue("POSTBOX", "");
		entrypost.putPropertyValue("POSTCODE", "42294");
		entrypost.putPropertyValue("CITY", acity);
		entrypost.putPropertyValue("COUNTRY", acountry);
		entrypost.putPropertyValue("REGION", "11");

		entrypost.putPropertyValue("TELEPHONE", aphone);
		entrypost.putPropertyValue("URL", aurl);
		entrypost.putPropertyValue("LANGU", "D");
		entrypost.putPropertyValue("CURRENCY", "EUR");
		entrypost.putPropertyValue("mimeType", "text/html");
		// entrypost.setCollectionId("TravelAgencies_DQ");
		entrypost.setCollectionId(url);
		//entryput=entrypost;*/
		String data = null;
		try {

			data = parser.buildODataEntryRequestBody(entrypost, url,helper.schema,1);

		} catch (ParserException e1) {
			
			e1.printStackTrace();
		}

	
	 
		try
		{
		logger = new Logger();
    	pref = new Preferences(this.getApplicationContext() , logger);
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
		
        //param.enableXsrf(true);
    	reqMan = new RequestManager(logger, pref, param, 1); 
    	parser = new Parser(pref, logger);
		//cache = new Cache(pref, logger);
		//cache.clear();	
   	
    	//get service doc
    	addrequest.setListener(singledata.this);
    	addrequest.setRequestUrl(helper.serviceDocUrl+"TravelAgencies_DQ");
    	addrequest.setRequestMethod(BaseRequest.REQUEST_METHOD_POST);    	
   	Map<String, String> headers = new HashMap<String, String>();
   	headers.put("X-SUP-APPCID",helper.AppconnID);
   	headers.put("Content-Type", "application/atom+xml");
   	headers.put("X-Requested-With", "XMLHttpRequest");
   	addrequest.setHeaders(headers);
   	byte[] theByteArray = data.getBytes();
    ((BaseRequest) addrequest).setData(theByteArray);
    Log.d("Performance","Start executing POST(add) single record "+ i);
    startcac = System.currentTimeMillis();
	eIDPost = helper.lcache.addEntry(entrypost);
	stopcac = System.currentTimeMillis();
	Log.d("Performance","Add operation local cache = " + (stopcac-startcac));
	//localentries = helper.ofcache.ReadEntriesLocal(url, eIDPost);
	
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
	}
	
	public void update_off_record()
	{
		String url = helper.serviceDocUrl+"TravelAgencies_DQ";
		
		for(int i=900;i<1000;i++)
		{
		entryput = new ODataEntry();
		entryput = helper.reads.get(i);
		entryput.putPropertyValue("TELEPHONE",""+entryput.getPropertyValue("TELEPHONE").replace("5","6"));
		String data = null;
		try {

			data = parser.buildODataEntryRequestBody(entryput, url,helper.schema,1);

		} catch (ParserException e1) {
			
			e1.printStackTrace();
		}

		try
		{
		logger = new Logger();
    	pref = new Preferences(this.getApplicationContext() , logger);
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
		
        //param.enableXsrf(true);
    	reqMan = new RequestManager(logger, pref, param, 1); 
    	parser = new Parser(pref, logger);
		//cache = new Cache(pref, logger);
		//cache.clear();	
   	
    	//get service doc
    	updaterequest.setListener(singledata.this);
    	updaterequest.setRequestUrl(helper.serviceDocUrl+"TravelAgencies_DQ('" + entryput.getPropertyValue("agencynum") +"')");
    	updaterequest.setRequestMethod(BaseRequest.REQUEST_METHOD_PUT);    	
   	Map<String, String> headers = new HashMap<String, String>();
   	headers.put("X-SUP-APPCID",helper.AppconnID);
   	headers.put("Content-Type", "application/atom+xml");
   	headers.put("X-Requested-With", "XMLHttpRequest");
   	updaterequest.setHeaders(headers);
   	byte[] theByteArray = data.getBytes();
    ((BaseRequest) updaterequest).setData(theByteArray);
    Log.d("Performance","Start executing PUT(update) for single record "+i);
    startcac = System.currentTimeMillis();
    helper.lcache.updateEntry(entryput);
    stopcac = System.currentTimeMillis();
	Log.d("Performance","Update operation local cache = " + (stopcac-startcac));
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
		}
	}
	
	public void delete_off_record()
	{
		for(int i=900;i<1000;i++)
		{
		entrydel = new ODataEntry();
		entrydel = helper.reads.get(i);
		try
		{
		logger = new Logger();
    	pref = new Preferences(this.getApplicationContext() , logger);
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
		
        //param.enableXsrf(true);
    	reqMan = new RequestManager(logger, pref, param, 1); 
    	parser = new Parser(pref, logger);
		//cache = new Cache(pref, logger);
		//cache.clear();	
   	
    	//get service doc
    	deleterequest.setListener(singledata.this);
    	deleterequest.setRequestUrl(helper.serviceDocUrl+"TravelAgencies_DQ('" + entrydel.getPropertyValue("agencynum") +"')");
    	deleterequest.setRequestMethod(BaseRequest.REQUEST_METHOD_DELETE);    	
   	Map<String, String> headers = new HashMap<String, String>();
   	headers.put("X-SUP-APPCID",helper.AppconnID);
   	headers.put("Content-Type", "application/atom+xml");
   	headers.put("X-Requested-With", "XMLHttpRequest");
   	deleterequest.setHeaders(headers);
   	Log.d("Performance","Start executing delete of single record "+i);
   	startcac = System.currentTimeMillis();
   	helper.lcache.deleteEntry(entry1.getCollectionId());
   	stopcac = System.currentTimeMillis();
	Log.d("Performance","Delete operation local cache = " + (stopcac-startcac));
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
	}
	
	public void add_record()
	{
		//anumber = "11000042";
	//	String URL = "https://" + helper.IP +"/smp/online/"+helper.APPNAME +"TravelAgencies_DQ";
		//String URL = "http://smpper12sapperf2.prod.jpaas.sapbydesign.com/smp/online/supa/TravelAgencies_DQ";
		String data ="<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
				+"<entry\n"
				+"xml:base=\"http://ldcig8p.wdf.sap.corp:50018/sap/opu/odata/iwfnd/RMTSAMPLEFLIGHT/\"\n"
				+"xmlns=\"http://www.w3.org/2005/Atom\" xmlns:m=\"http://schemas.microsoft.com/ado/2007/08/dataservices/metadata\"\n"
				+"xmlns:d=\"http://schemas.microsoft.com/ado/2007/08/dataservices\">\n"
				+"<id>http://ldcig8p.wdf.sap.corp:50018/sap/opu/odata/iwfnd/RMTSAMPLEFLIGHT/TravelAgencies_DQ('"+anumber+"')</id>\n"
				+"<title type=\"text\">TravelAgencies_DQ('"+anumber+"')</title>\n"
				+"<category term=\"RMTSAMPLEFLIGHT.Travelagency\"\n"
					+"scheme=\"http://schemas.microsoft.com/ado/2007/08/dataservices/scheme\" />\n"
					+"<link href=\"TravelAgencies_DQ('"+anumber+"')\" rel=\"edit\" title=\"Travelagency\"/>\n"
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
		
		String url = helper.serviceDocUrl+"TravelAgencies_DQ";
		entrypost = new ODataEntry();
		// entry.putId("Save");
		entrypost.putPropertyValue("agencynum", anumber);
		entrypost.putPropertyValue("NAME", aname);
		entrypost.putPropertyValue("STREET", astreet);
		entrypost.putPropertyValue("POSTBOX", "");
		entrypost.putPropertyValue("POSTCODE", "42294");
		entrypost.putPropertyValue("CITY", acity);
		entrypost.putPropertyValue("COUNTRY", acountry);
		entrypost.putPropertyValue("REGION", "11");

		entrypost.putPropertyValue("TELEPHONE", aphone);
		entrypost.putPropertyValue("URL", aurl);
		entrypost.putPropertyValue("LANGU", "D");
		entrypost.putPropertyValue("CURRENCY", "EUR");
		entrypost.putPropertyValue("mimeType", "text/html");
		// entrypost.setCollectionId("TravelAgencies_DQ");
		entrypost.setCollectionId(url);
		//entryput=entrypost;
		//String data = null;
		try {

			data = parser.buildODataEntryRequestBody(entrypost, url,helper.schema,1);

		} catch (ParserException e1) {
			
			e1.printStackTrace();
		}

	
	 
		try
		{
		logger = new Logger();
    	pref = new Preferences(this.getApplicationContext() , logger);
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
		
        //param.enableXsrf(true);
    	reqMan = new RequestManager(logger, pref, param, 1); 
    	parser = new Parser(pref, logger);
		//cache = new Cache(pref, logger);
		//cache.clear();	
   	
    	//get service doc
    	addrequest.setListener(singledata.this);
    	addrequest.setRequestUrl(helper.serviceDocUrl+"TravelAgencies_DQ");
    	addrequest.setRequestMethod(BaseRequest.REQUEST_METHOD_POST);    	
   	Map<String, String> headers = new HashMap<String, String>();
   	headers.put("X-SUP-APPCID",helper.AppconnID);
   	headers.put("Content-Type", "application/atom+xml");
   	headers.put("X-Requested-With", "XMLHttpRequest");
   	addrequest.setHeaders(headers);
   	byte[] theByteArray = data.getBytes();
    ((BaseRequest) addrequest).setData(theByteArray);
    Log.d("Performance","Start executing POST(add) single record");
    startcac = System.currentTimeMillis();
	eIDPost = helper.lcache.addEntry(entrypost);
	stopcac = System.currentTimeMillis();
	Log.d("Performance","Add operation local cache = " + (stopcac-startcac));
	//localentries = helper.ofcache.ReadEntriesLocal(url, eIDPost);
	
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
	//	String URL = "https://" + helper.IP +"/smp/online/"+helper.APPNAME +"/TravelAgencies_DQ('"+anumber+"')";
		//String URL = "http://smpper12sapperf2.prod.jpaas.sapbydesign.com/smp/online/supa/TravelAgencies_DQ('"+anumber+"')";
		/*String data ="<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
				+"<entry\n"
				+"xml:base=\"http://ldcig8p.wdf.sap.corp:50018/sap/opu/odata/iwfnd/RMTSAMPLEFLIGHT/\"\n"
				+"xmlns=\"http://www.w3.org/2005/Atom\" xmlns:m=\"http://schemas.microsoft.com/ado/2007/08/dataservices/metadata\"\n"
				+"xmlns:d=\"http://schemas.microsoft.com/ado/2007/08/dataservices\">\n"
				+"<id>http://ldcig8p.wdf.sap.corp:50018/sap/opu/odata/iwfnd/RMTSAMPLEFLIGHT/TravelAgencies_DQ('"+anumber+"')</id>\n"
				+"<title type=\"text\">TravelAgencies_DQ('"+anumber+"')</title>\n"
				+"<category term=\"RMTSAMPLEFLIGHT.Travelagency\"\n"
					+"scheme=\"http://schemas.microsoft.com/ado/2007/08/dataservices/scheme\" />\n"
					+"<link href=\"TravelAgencies_DQ('"+anumber+"')\" rel=\"edit\" title=\"Travelagency\"/>\n"
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
			+"</entry>\n";*/
		String url = helper.serviceDocUrl+"TravelAgencies_DQ";
		entryput = new ODataEntry();
		// entry.putId("Save");
		//entryput.putId(eIDDel);
		entryput.putPropertyValue("agencynum", anumber);
		entryput.putPropertyValue("NAME", aname);
		entryput.putPropertyValue("STREET", astreet);
		entryput.putPropertyValue("POSTBOX", "");
		entryput.putPropertyValue("POSTCODE", "42294");
		entryput.putPropertyValue("CITY", acity);
		entryput.putPropertyValue("COUNTRY", acountry);
		entryput.putPropertyValue("REGION", "11");

		entryput.putPropertyValue("TELEPHONE","1234");
		entryput.putPropertyValue("URL", aurl);
		entryput.putPropertyValue("LANGU", "D");
		entryput.putPropertyValue("CURRENCY", "EUR");
		entryput.putPropertyValue("mimeType", "text/html");
		// entrypost.setCollectionId("TravelAgencies_DQ");
		entryput.setCollectionId(url);
		entryput = entry1;
		entryput.putPropertyValue("TELEPHONE","1234");
		//entryput.putPropertyValue("TELEPHONE", aphone+"2");
		String data = null;
		try {

			data = parser.buildODataEntryRequestBody(entryput, url,helper.schema,1);

		} catch (ParserException e1) {
			
			e1.printStackTrace();
		}

		try
		{
		logger = new Logger();
    	pref = new Preferences(this.getApplicationContext() , logger);
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
		
        //param.enableXsrf(true);
    	reqMan = new RequestManager(logger, pref, param, 1); 
    	parser = new Parser(pref, logger);
		//cache = new Cache(pref, logger);
		//cache.clear();	
   	
    	//get service doc
    	updaterequest.setListener(singledata.this);
    	updaterequest.setRequestUrl(helper.serviceDocUrl+"TravelAgencies_DQ('" + anumber +"')");
    	updaterequest.setRequestMethod(BaseRequest.REQUEST_METHOD_PUT);    	
   	Map<String, String> headers = new HashMap<String, String>();
   	headers.put("X-SUP-APPCID",helper.AppconnID);
   	headers.put("Content-Type", "application/atom+xml");
   	headers.put("X-Requested-With", "XMLHttpRequest");
   	updaterequest.setHeaders(headers);
   	byte[] theByteArray = data.getBytes();
    ((BaseRequest) updaterequest).setData(theByteArray);
    Log.d("Performance","Start executing PUT(update) for single record");
    startcac = System.currentTimeMillis();
    helper.lcache.updateEntry(entryput);
    stopcac = System.currentTimeMillis();
	Log.d("Performance","Update operation local cache = " + (stopcac-startcac));
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
		logger = new Logger();
    	pref = new Preferences(this.getApplicationContext() , logger);
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
		
        //param.enableXsrf(true);
    	reqMan = new RequestManager(logger, pref, param, 1); 
    	parser = new Parser(pref, logger);
		//cache = new Cache(pref, logger);
		//cache.clear();	
   	
    	//get service doc
    	deleterequest.setListener(singledata.this);
    	deleterequest.setRequestUrl(helper.serviceDocUrl+"TravelAgencies_DQ('" + anumber +"')");
    	deleterequest.setRequestMethod(BaseRequest.REQUEST_METHOD_DELETE);    	
   	Map<String, String> headers = new HashMap<String, String>();
   	headers.put("X-SUP-APPCID",helper.AppconnID);
   	headers.put("Content-Type", "application/atom+xml");
   	headers.put("X-Requested-With", "XMLHttpRequest");
   	deleterequest.setHeaders(headers);
   	Log.d("Performance","Start executing delete of single record");
   	startcac = System.currentTimeMillis();
   	helper.lcache.deleteEntry(entry1.getCollectionId());
   	stopcac = System.currentTimeMillis();
	Log.d("Performance","Delete operation local cache = " + (stopcac-startcac));
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
	
	public void setdata()
	{
		 entry1 = entries.getEntries().get(0);
	  anumber = entry1.getPropertyValue("agencynum");
  	  aphone = entry1.getPropertyValue("TELEPHONE");  
  	  aurl = entry1.getPropertyValue("URL");
  	  aname = entry1.getPropertyValue("NAME");
  	  astreet = entry1.getPropertyValue("STREET");
  	  acity = entry1.getPropertyValue("CITY");
  	  acountry = entry1.getPropertyValue("COUNTRY");
  	  eIDDel = entry1.getId();
	}
	
	@Override
	public void onError(IRequest arg0, IResponse arg1,
			IRequestStateElement arg2) {
		Log.d("Performance","Error" + arg1.getStatusLine());
		//MessageBox(arg1.getStatusLine().toString());
		synchronized (lockObject) {
			lockObject.notify();
		}
		
	}
	@Override
	public void onSuccess(IRequest arg0, IResponse arg1) {
		HttpEntity responseEntity = arg1.getEntity();
		//Log.i("tag","on success"+responseEntity.toString());
		if( arg0.equals(getrequest)){
			try {
				Log.d("Performance","End executing GET single record");
				stop = System.currentTimeMillis();
				Log.i("tag","For successful GET" + arg1.getStatusLine());
				//MessageBox(arg1.getStatusLine().toString());
				//ParseXML(EntityUtils.toString(responseEntity));
				entries = parser.parseODataFeed(EntityUtils.toString(responseEntity),"TravelAgencies_DQ", helper.schema);
				Log.d("Performance","End parsing and request single record");
				end = System.currentTimeMillis();
				setdata();
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
				helper.lcache.clearLocalEntry(entrypost.getCollectionId());
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
				helper.lcache.clearLocalEntry(entryput.getCollectionId());
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
				helper.lcache.clearLocalEntry(entry1.getCollectionId());
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
