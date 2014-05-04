package com.sap.manoj.smp_libs;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.io.StringReader;
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
import com.sap.mobile.lib.sdmparser.ISDMODataEntry;
import com.sap.mobile.lib.sdmparser.ISDMODataSchema;
import com.sap.mobile.lib.sdmparser.ISDMODataServiceDocument;
import com.sap.mobile.lib.sdmparser.SDMParser;
import com.sap.mobile.lib.sdmpersistence.SDMPersistence;
import com.sap.mobile.lib.supportability.ISDMLogger;
import com.sap.mobile.lib.supportability.SDMLogger;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class auto extends Activity implements ISDMNetListener{
	public Object lockObject = new Object();
	public SDMLogger logger;
	public SDMPreferences pref;
	public SDMPersistence perst;
	public SDMRequestManager reqMan;
	public SDMConnectivityParameters param;
	public SDMParser parser;
	public ISDMRequest request = new SDMBaseRequest();
	public SDMCache cache;
	public byte[] serviceDocument2 = null;
	public List<ISDMODataEntry> entries;
	public int reqtype = 0;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auto);
        
        Button start = (Button)findViewById(R.id.buttons);
        start.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				try{
				logger = new SDMLogger();
	        	pref = new SDMPreferences(getApplicationContext(), logger);
	        	param = new SDMConnectivityParameters();
	        	pref.setIntPreference(ISDMPreferences.SDM_CONNECTIVITY_CONNTIMEOUT, 75000);
	        	pref.setIntPreference(ISDMPreferences.SDM_CONNECTIVITY_SCONNTIMEOUT, 75000);
	        	pref.setStringPreference(ISDMPreferences.SDM_CONNECTIVITY_HANDLER_CLASS_NAME, SDMConstants.SDM_HTTP_HANDLER_CLASS);
	        	pref.setBooleanPreference(ISDMPreferences.SDM_PERSISTENCE_SECUREMODE, false);
	        	logger.setLogLevel(ISDMLogger.DEBUG);
	        	param = new SDMConnectivityParameters(); 
	        	param.setUserName(helper.userName);                    // username for all other onboardings except CERT.
	            param.setUserPassword(helper.password);                // password for all other onboardings except CERT.			
	            param.enableXsrf(true);
	        	SDMRequestManager reqMan = new SDMRequestManager(logger, pref, param, 1); 
	        	parser = new SDMParser(pref, logger);
	        	//parser.enableParserPerformanceLog(true,getApplicationContext());
				cache = new SDMCache(pref, logger);	
				request.setListener(auto.this);
				Map<String, String> headers = new HashMap<String, String>();
		       	headers.put("X-SUP-APPCID",helper.AppconnID);
		       	headers.put("Content-Type", "application/atom+xml"); 
		       	headers.put("X-Requested-With", "XMLHttpRequest");
		       	request.setHeaders(headers);
		       	String data ="<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
						+"<entry\n"
						+"xml:base=\"http://ldcig8p.wdf.sap.corp:50018/sap/opu/odata/iwfnd/RMTSAMPLEFLIGHT/\"\n"
						+"xmlns=\"http://www.w3.org/2005/Atom\" xmlns:m=\"http://schemas.microsoft.com/ado/2007/08/dataservices/metadata\"\n"
						+"xmlns:d=\"http://schemas.microsoft.com/ado/2007/08/dataservices\">\n"
						+"<id>http://ldcig8p.wdf.sap.corp:50018/sap/opu/odata/iwfnd/RMTSAMPLEFLIGHT/TravelagencyCollection('00000009')</id>\n"
						+"<title type=\"text\">TravelagencyCollection('00000009')</title>\n"
						+"<category term=\"RMTSAMPLEFLIGHT.Travelagency\"\n"
							+"scheme=\"http://schemas.microsoft.com/ado/2007/08/dataservices/scheme\" />\n"
							+"<link href=\"TravelagencyCollection('00000009')\" rel=\"edit\" title=\"Travelagency\"/>\n"
						+"<content type=\"application/atom+xml\">\n"
							+"<m:properties>\n"
								+"<d:agencynum>00000009</d:agencynum>\n"
								+"<d:NAME>Happy Holiday126</d:NAME>\n"
								+"<d:STREET>Rastenburger Str. 12</d:STREET>\n"
								+"<d:POSTBOX />\n"
								+"<d:POSTCODE>28779</d:POSTCODE>\n"
								+"<d:CITY>Bremen</d:CITY>\n"
								+"<d:COUNTRY>DE</d:COUNTRY>\n"
								+"<d:REGION>04</d:REGION>\n"
								+"<d:TELEPHONE>11113</d:TELEPHONE>\n"
								+"<d:URL>http://www.happy</d:URL>\n"
								+"<d:LANGU>D</d:LANGU>\n"
								+"<d:CURRENCY>EUR</d:CURRENCY>\n"
								+"<d:mimeType>text/html</d:mimeType>\n"
							+"</m:properties>\n"
						+"</content>\n"
					+"</entry>\n";
		       	byte[] theByteArray = data.getBytes();
		       	
		       	
		       	
				for (int i =1;i<=20;i++)
				{
				//300 records
				//service document
				Log.d("Performance","iteration: "+i);
				cache.clear();	
				request.setRequestUrl(helper.serviceDocUrl);
				request.setRequestMethod(SDMBaseRequest.REQUEST_METHOD_GET);
				reqtype=1;
				helper.start = System.currentTimeMillis();
				reqMan.makeRequest(request);
				synchronized (lockObject) {
					lockObject.wait();
	    		}
				//metadata
				request.setRequestUrl(helper.serviceDocUrl+"$metadata");
				request.setRequestMethod(SDMBaseRequest.REQUEST_METHOD_GET);
				reqtype=2;
				helper.start = System.currentTimeMillis();
				reqMan.makeRequest(request);
				synchronized (lockObject) {
					lockObject.wait();
	    		}
				//collection 300 records
				request.setRequestUrl(helper.serviceDocUrl+"TravelagencyCollection?$top=288");
				request.setRequestMethod(SDMBaseRequest.REQUEST_METHOD_GET);
				headers.put("Accept-Encoding","gzip");
				request.setHeaders(headers);
				reqtype=3;
				helper.start = System.currentTimeMillis();
				reqMan.makeRequest(request);
				synchronized (lockObject) {
					lockObject.wait();
	    		}
				//1000 records
				request.setRequestUrl(helper.serviceDocUrl+"TravelagencyCollection?$top=1000");
				request.setRequestMethod(SDMBaseRequest.REQUEST_METHOD_GET);
				reqtype=4;
				helper.start = System.currentTimeMillis();
				reqMan.makeRequest(request);
				synchronized (lockObject) {
					lockObject.wait();
	    		}
				//get
				request.setRequestUrl(helper.serviceDocUrl+"TravelagencyCollection('00000009')");
				request.setRequestMethod(SDMBaseRequest.REQUEST_METHOD_GET);
				headers.remove("Accept-Encoding");
				reqtype=5;
				helper.start = System.currentTimeMillis();
				reqMan.makeRequest(request);
				synchronized (lockObject) {
					lockObject.wait();
	    		}
				//put
				request.setRequestUrl(helper.serviceDocUrl+"TravelagencyCollection('00000009')");
				request.setRequestMethod(SDMBaseRequest.REQUEST_METHOD_PUT);  
				((SDMBaseRequest)request).setData(theByteArray);
				reqtype=6;
				helper.start = System.currentTimeMillis();
				reqMan.makeRequest(request);
				synchronized (lockObject) {
					lockObject.wait();
	    		}
				//delete
				request.setRequestUrl(helper.serviceDocUrl+"TravelagencyCollection('00000009')");
				request.setRequestMethod(SDMBaseRequest.REQUEST_METHOD_DELETE);  
				reqtype=7;
				helper.start = System.currentTimeMillis();
				reqMan.makeRequest(request);
				synchronized (lockObject) {
					lockObject.wait();
	    		}
				//post
				request.setRequestUrl(helper.serviceDocUrl+"TravelagencyCollection");
				request.setRequestMethod(SDMBaseRequest.REQUEST_METHOD_POST);  
				((SDMBaseRequest)request).setData(theByteArray);
				reqtype=8;
				helper.start = System.currentTimeMillis();
				reqMan.makeRequest(request);
				synchronized (lockObject) {
					lockObject.wait();
	    		}/*
				//upload 100kb 
				{
			  	byte[] data2 = readFile(new File("/mnt/sdcard/data/app/100kb.jpg"));
				request.setRequestUrl(helper.serviceDocUrl+"CarrierCollection('LH')/$value");
				request.setRequestMethod(SDMBaseRequest.REQUEST_METHOD_PUT);  
			    ((SDMBaseRequest)request).setData(data2);
				reqtype=9;
				helper.start = System.currentTimeMillis();
				reqMan.makeRequest(request);
				synchronized (lockObject) {
					lockObject.wait();
	    		}
				}
				//download 100kb
				request.setRequestUrl(helper.serviceDocUrl+"CarrierCollection('LH')/$value");
				request.setRequestMethod(SDMBaseRequest.REQUEST_METHOD_GET);
				reqtype=10;
				helper.start = System.currentTimeMillis();
				reqMan.makeRequest(request);
				synchronized (lockObject) {
					lockObject.wait();
	    		}
				//upload 500kb
				byte[] data2 = readFile(new File("/mnt/sdcard/data/app/500kb.jpg"));
				request.setRequestUrl(helper.serviceDocUrl+"CarrierCollection('LH')/$value");
				request.setRequestMethod(SDMBaseRequest.REQUEST_METHOD_PUT);  
			    ((SDMBaseRequest)request).setData(data2);
				reqtype=11;
				helper.start = System.currentTimeMillis();
				reqMan.makeRequest(request);
				synchronized (lockObject) {
					lockObject.wait();
	    		}
				//download 500kb
				request.setRequestUrl(helper.serviceDocUrl+"CarrierCollection('LH')/$value");
				request.setRequestMethod(SDMBaseRequest.REQUEST_METHOD_GET);
				reqtype=12;
				helper.start = System.currentTimeMillis();
				reqMan.makeRequest(request);
				synchronized (lockObject) {
					lockObject.wait();
	    		}
				//helper.row++;
				Thread.sleep(120000);*/
				}
				}
				catch(Exception e){
					e.printStackTrace();
				}
			}
		});
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

	@Override
	public void onError(ISDMRequest arg0, ISDMResponse arg1,
			ISDMRequestStateElement arg2) {
		Log.d("Performance","Error");
		synchronized (lockObject) {
			lockObject.notify();
		}		
	}

	@Override
	public void onSuccess(ISDMRequest arg0, ISDMResponse arg1) {
		try{
		HttpEntity responseEntity = arg1.getEntity();
		if(reqtype==1){
				helper.stop = System.currentTimeMillis();
				ISDMODataServiceDocument serviceDoc = parser.parseSDMODataServiceDocumentXML(EntityUtils.toString(responseEntity));
				cache.setSDMODataServiceDocument(serviceDoc);
				helper.end = System.currentTimeMillis();
				Log.d("Performance","Service document success");
		}		
		if(reqtype==2){
				String xmlString = EntityUtils.toString(responseEntity);
				helper.stop = System.currentTimeMillis();
				ISDMODataSchema schema = parser.parseSDMODataSchemaXML(xmlString, cache.getSDMODataServiceDocument());
				cache.setSDMODataSchema(schema);
				helper.end=System.currentTimeMillis();
				Log.d("Performance","Metadata success");
		}
		if (reqtype==3) {
				InputStream inst = responseEntity.getContent();
				byte[] a = decompress(inst);
				helper.stop=System.currentTimeMillis();
				Log.d("Performance","start parsing");
				entries = parser.parseSDMODataEntriesXML(new String(a),"TravelagencyCollection", cache.getSDMODataSchema());
				cache.setSDMODataEntries(entries);
				helper.end= System.currentTimeMillis();
				Log.d("Performance","stop parsing. Parsing time ="+(helper.end-helper.stop));
				perst = new SDMPersistence(pref, logger);
				perst.storeSDMCache(cache);
				cache.clear();
				perst.loadSDMCache(cache);
				Log.d("Performance","300 entries collection success");
		}
		if (reqtype==4) {
			InputStream inst = responseEntity.getContent();
			byte[] a = decompress(inst);
			Log.d("Performance","start parsing");
			helper.stop=System.currentTimeMillis();
			entries = parser.parseSDMODataEntriesXML(new String(a),"TravelagencyCollection", cache.getSDMODataSchema());
			cache.setSDMODataEntries(entries);
			helper.end= System.currentTimeMillis();
			Log.d("Performance","stop parsing. Parsing time ="+(helper.end-helper.stop));
			perst = new SDMPersistence(pref, logger);
			perst.storeSDMCache(cache);
			cache.clear();
			perst.loadSDMCache(cache);
			Log.d("Performance","1000 entries collection success");
		}
		if (reqtype==5) {
			helper.stop=System.currentTimeMillis();
			ParseXML(EntityUtils.toString(responseEntity));
			helper.end=System.currentTimeMillis();
			Log.d("Performance","GET single data success");
		}
		if (reqtype==6) {
			helper.stop=System.currentTimeMillis();
			helper.end=System.currentTimeMillis();
			Log.d("Performance","PUT single data success");
		}
		if (reqtype==7) {
			helper.stop=System.currentTimeMillis();
			helper.end=System.currentTimeMillis();
			Log.d("Performance","DELETE single data success");
		}
		if (reqtype==8) {
			helper.stop=System.currentTimeMillis();
			helper.end=System.currentTimeMillis();
			Log.d("Performance","ADD single data success");
		}
		if (reqtype==9) {
			helper.stop=System.currentTimeMillis();
			helper.end=System.currentTimeMillis();
			Log.d("Performance","Upload 100kb success");
		}
		if (reqtype==10) {
			serviceDocument2 = EntityUtils.toByteArray(responseEntity);
			helper.stop=System.currentTimeMillis();
			helper.end=System.currentTimeMillis();
			Log.d("Performance","Download 100kb success");
		}
		if (reqtype==11) {
			helper.stop=System.currentTimeMillis();
			helper.end=System.currentTimeMillis();
			Log.d("Performance","Upload 500kb success");
		}
		if (reqtype==12) {
			serviceDocument2 = EntityUtils.toByteArray(responseEntity);
			helper.stop=System.currentTimeMillis();
			helper.end=System.currentTimeMillis();
			Log.d("Performance","Downlaod 500kb success");
		}
		excel.saveExcelFile(getApplicationContext(),reqtype);
		Thread.sleep(2000);
		synchronized (lockObject) {
			lockObject.notify();
		}		
		}catch(Exception e){
			synchronized (lockObject) {
				lockObject.notify();
			}		
			e.printStackTrace();
		}
	}
	public void MessageBox(String message) {
		Toast.makeText(this, message, Toast.LENGTH_LONG).show();
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
	public void ParseXML(String xmlRecords)
	{
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
            	  //anumber = (fstname.item(0)).getNodeValue();
            	  
            	  NodeList nodelist1 = element.getElementsByTagName("d:TELEPHONE");
            	  Element element2= (Element) nodelist1.item(0);
            	  NodeList fstname1 = element2.getChildNodes();
            	  //aphone = (fstname1.item(0).getNodeValue());
            	  
            	  NodeList nodelist2 = element.getElementsByTagName("d:URL");
            	  Element element3= (Element) nodelist2.item(0);
            	  NodeList fstname2 = element3.getChildNodes();
            	  //aurl = (fstname2.item(0).getNodeValue());

            	  NodeList nodelistA = element.getElementsByTagName("d:NAME");
            	  Element element1A= (Element) nodelistA.item(0);
            	  NodeList fstnameA = element1A.getChildNodes();
            	  //aname = (fstnameA.item(0).getNodeValue());
            	  
            	  NodeList nodelistB = element.getElementsByTagName("d:STREET");
            	  Element element2B= (Element) nodelistB.item(0);
            	  NodeList fstname1B = element2B.getChildNodes();
            	  //astreet = (fstname1B.item(0).getNodeValue());
            	 
            	  NodeList nodelistC = element.getElementsByTagName("d:CITY");
            	  Element element2C= (Element) nodelistC.item(0);
            	  NodeList fstname1C = element2C.getChildNodes();
            	  //acity = (fstname1C.item(0).getNodeValue());
            	  
            	  NodeList nodelist2D = element.getElementsByTagName("d:COUNTRY");
            	  Element element3D= (Element) nodelist2D.item(0);
            	  NodeList fstname2D = element3D.getChildNodes();
            	  //acountry = (fstname2D.item(0).getNodeValue());
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
 
}
