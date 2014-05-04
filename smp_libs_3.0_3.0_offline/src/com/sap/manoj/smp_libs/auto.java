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

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class auto extends Activity implements INetListener{
	public Object lockObject = new Object();
	public Logger logger;
	public Preferences pref;
	//public Persistence perst;
	public RequestManager reqMan;
	public ConnectivityParameters param;
	public Parser parser;
	public IODataServiceDocument serviceDoc;
	public IODataSchema schema;
	public IRequest request = new BaseRequest();
	//public Cache cache;
	public byte[] serviceDocument2 = null;
	public IODataFeed entries;
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
				logger = new Logger();
	        	pref = new Preferences(getApplicationContext(), logger);
	        	param = new ConnectivityParameters();
	        	pref.setIntPreference(IPreferences.CONNECTIVITY_CONNTIMEOUT, 75000);
	        	pref.setIntPreference(IPreferences.CONNECTIVITY_SCONNTIMEOUT, 75000);
	        	pref.setStringPreference(IPreferences.CONNECTIVITY_HANDLER_CLASS_NAME, Constants.HTTP_HANDLER_CLASS);
	        	pref.setBooleanPreference(IPreferences.PERSISTENCE_SECUREMODE, false);
	        	logger.setLogLevel(ILogger.DEBUG);
	        	param = new ConnectivityParameters(); 
	        	param.setUserName(helper.userName);                    // username for all other onboardings except CERT.
	            param.setUserPassword(helper.password);                // password for all other onboardings except CERT.			
	            param.enableXsrf(true);
	        	RequestManager reqMan = new RequestManager(logger, pref, param, 1); 
	        	parser = new Parser(pref, logger);
	        	//parser.enableParserPerformanceLog(true,getApplicationContext());
				//cache = new Cache(pref, logger);	
				request.setListener(auto.this);
				Map<String, String> headers = new HashMap<String, String>();
		       	//headers.put("X-SUP-APPCID",helper.AppconnID);
		       	headers.put("Content-Type", "application/atom+xml"); 
		       	headers.put("X-Requested-With", "XMLHttpRequest");
		       	request.setHeaders(headers);
		       	String data ="<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
						+"<entry\n"
						+"xml:base=\"http://ldcig8p.wdf.sap.corp:50018/sap/opu/odata/iwfnd/RMTSAMPLEFLIGHT/\"\n"
						+"xmlns=\"http://www.w3.org/2005/Atom\" xmlns:m=\"http://schemas.microsoft.com/ado/2007/08/dataservices/metadata\"\n"
						+"xmlns:d=\"http://schemas.microsoft.com/ado/2007/08/dataservices\">\n"
						+"<id>http://ldcig8p.wdf.sap.corp:50018/sap/opu/odata/iwfnd/RMTSAMPLEFLIGHT/TravelAgencies_DQ('00000009')</id>\n"
						+"<title type=\"text\">TravelAgencies_DQ('00000009')</title>\n"
						+"<category term=\"RMTSAMPLEFLIGHT.Travelagency\"\n"
							+"scheme=\"http://schemas.microsoft.com/ado/2007/08/dataservices/scheme\" />\n"
							+"<link href=\"TravelAgencies_DQ('00000009')\" rel=\"edit\" title=\"Travelagency\"/>\n"
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
					//http://ldcig8p.wdf.sap.corp:50018/sap/opu/odata/iwfnd/RMTSAMPLEFLIGHT/TravelAgencies_DQ?$format=json&$top=531
		       		
				Log.d("Performance","iteration: "+i);
				//cache.clear();

				serviceDoc=null;
				request.setRequestUrl(helper.serviceDocUrl);
				request.setRequestMethod(BaseRequest.REQUEST_METHOD_GET);
				headers.put("Content-Type", "application/atom+xml"); 
				request.setHeaders(headers);
				reqtype=1;
				helper.start = System.currentTimeMillis();
				reqMan.makeRequest(request);
				synchronized (lockObject) {
					lockObject.wait();
	    		}
				
				//metadata
				schema=null;
				request.setRequestUrl(helper.serviceDocUrl+"$metadata");
				request.setRequestMethod(BaseRequest.REQUEST_METHOD_GET);
				headers.put("Content-Type", "application/atom+xml"); 
				request.setHeaders(headers);
				reqtype=2;
				helper.start = System.currentTimeMillis();
				reqMan.makeRequest(request);
				synchronized (lockObject) {
					lockObject.wait();
	    		}
				
				//collection 300 records
				request.setRequestUrl(helper.serviceDocUrl+"TravelAgencies_DQ?$format=json&$top=300");
				request.setRequestMethod(BaseRequest.REQUEST_METHOD_GET);
				headers.put("Accept-Encoding","gzip");
				request.setHeaders(headers);
				reqtype=3;
				helper.start = System.currentTimeMillis();
				reqMan.makeRequest(request);
				synchronized (lockObject) {
					lockObject.wait();
	    		}
				
				//1000 records
				request.setRequestUrl(helper.serviceDocUrl+"TravelAgencies_DQ?$format=json&$top=1000");
				request.setRequestMethod(BaseRequest.REQUEST_METHOD_GET);
				reqtype=4;
				helper.start = System.currentTimeMillis();
				reqMan.makeRequest(request);
				synchronized (lockObject) {
					lockObject.wait();
	    		}
				//Thread.sleep(10000);
				
				//get
				
				request.setRequestUrl(helper.serviceDocUrl+"TravelAgencies_DQ('00000009')");
				request.setRequestMethod(BaseRequest.REQUEST_METHOD_GET);
				headers.remove("Accept-Encoding");
				reqtype=5;
				helper.start = System.currentTimeMillis();
				reqMan.makeRequest(request);
				synchronized (lockObject) {
					lockObject.wait();
	    		}
				
				//put
				request.setRequestUrl(helper.serviceDocUrl+"TravelAgencies_DQ('00000009')");
				request.setRequestMethod(BaseRequest.REQUEST_METHOD_PUT);  
				((BaseRequest)request).setData(theByteArray);
				reqtype=6;
				helper.start = System.currentTimeMillis();
				reqMan.makeRequest(request);
				synchronized (lockObject) {
					lockObject.wait();
	    		}
				//delete
				request.setRequestUrl(helper.serviceDocUrl+"TravelAgencies_DQ('00000009')");
				request.setRequestMethod(BaseRequest.REQUEST_METHOD_DELETE);  
				reqtype=7;
				helper.start = System.currentTimeMillis();
				reqMan.makeRequest(request);
				synchronized (lockObject) {
					lockObject.wait();
	    		}
				//post
				request.setRequestUrl(helper.serviceDocUrl+"TravelAgencies_DQ");
				request.setRequestMethod(BaseRequest.REQUEST_METHOD_POST);  
				((BaseRequest)request).setData(theByteArray);
				reqtype=8;
				helper.start = System.currentTimeMillis();
				reqMan.makeRequest(request);
				synchronized (lockObject) {
					lockObject.wait();
	    		}
				
				//upload 100kb 
				{
			  	byte[] data2 = readFile(new File("/mnt/sdcard/data/app/100kb.jpg"));
					//be sure to have the correct location of the image
			  	//byte[] data2 = readFile(new File("/data/100kb.jpg"));
					//byte[] data2 = readFile(new File(getResources().openRawResource(R.raw.this));
				request.setRequestUrl(helper.serviceDocUrl+"CarrierCollection('LH')/$value");
				request.setRequestMethod(BaseRequest.REQUEST_METHOD_PUT);  
				headers.put("Content-Type", "image/jpeg"); 
				request.setHeaders(headers);
			    ((BaseRequest)request).setData(data2);
				reqtype=9;
				helper.start = System.currentTimeMillis();
				reqMan.makeRequest(request);
				synchronized (lockObject) {
					lockObject.wait();
	    		}
				}
				
				//download 100kb
				request.setRequestUrl(helper.serviceDocUrl+"CarrierCollection('LH')/$value");
				request.setRequestMethod(BaseRequest.REQUEST_METHOD_GET);
				reqtype=10;
				helper.start = System.currentTimeMillis();
				reqMan.makeRequest(request);
				synchronized (lockObject) {
					lockObject.wait();
	    		}
				//upload 500kb
				byte[] data2 = readFile(new File("/mnt/sdcard/data/app/500kb.jpg"));
				//byte[] data2 = readFile(new File("/data/500kb.jpg"));
				request.setRequestUrl(helper.serviceDocUrl+"CarrierCollection('LH')/$value");
				request.setRequestMethod(BaseRequest.REQUEST_METHOD_PUT);  
			    ((BaseRequest)request).setData(data2);
				reqtype=11;
				helper.start = System.currentTimeMillis();
				reqMan.makeRequest(request);
				synchronized (lockObject) {
					lockObject.wait();
	    		}
				//download 500kb
				request.setRequestUrl(helper.serviceDocUrl+"CarrierCollection('LH')/$value");
				request.setRequestMethod(BaseRequest.REQUEST_METHOD_GET);
				reqtype=12;
				helper.start = System.currentTimeMillis();
				reqMan.makeRequest(request);
				synchronized (lockObject) {
					lockObject.wait();
	    		}
				//helper.row++;
				Thread.sleep(2000);
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
	public void onError(IRequest arg0, IResponse arg1,
			IRequestStateElement arg2) {
		synchronized (lockObject) {
			lockObject.notify();
		}		
		//Log.d("Performance","Error "+ arg1.getStatusLine().toString());
		Log.d("Timings", "Error");
		helper.stop = System.currentTimeMillis();
		excel.saveExcelFile(getApplicationContext(),reqtype+100);
		//Log.d("Timings",reqtype+" - Req/Resp="+(helper.stop-helper.start)+" Parser="+(helper.end-helper.stop)+" E2E="+(helper.end-helper.start));
		
	}

	@Override
	public void onSuccess(IRequest arg0, IResponse arg1) {
		try{
		HttpEntity responseEntity = arg1.getEntity();
		if(reqtype==1){
				helper.stop = System.currentTimeMillis();
				serviceDoc = parser.parseODataServiceDocument(EntityUtils.toString(responseEntity));
				//cache.setODataServiceDocument(serviceDoc);
				helper.end = System.currentTimeMillis();
				Log.d("Performance","Service document success");
		}		
		if(reqtype==2){
				schema=null;
				String xmlString = EntityUtils.toString(responseEntity);
				helper.stop = System.currentTimeMillis();
				schema = parser.parseODataSchema(xmlString, serviceDoc);
				//cache.setODataSchema(schema);
				helper.end=System.currentTimeMillis();
				Log.d("Performance","Metadata success");
		}
		if (reqtype==3) {
			//String xmlst = EntityUtils.toString(responseEntity);
			Log.d("Performance","data");
				InputStream inst = responseEntity.getContent();
				byte[] a = decompress(inst);
				
				String str = new String(a);
				helper.stop=System.currentTimeMillis();
				Log.d("Performance","start parsing");
				//parser.par
				entries = parser.parseODataFeed(str,"TravelAgencies_DQ", schema);
				helper.end= System.currentTimeMillis();
				Log.d("Performance","stop parsing. Parsing time ="+(helper.end-helper.stop));
				//cache.setODataEntries(entries);
				//perst = new Persistence(pref, logger);
				//perst.storeCache(cache);
				//cache.clear();
				//perst.loadCache(cache);
				Log.d("Performance","300 entries collection success");
		}
		if (reqtype==4) {
			InputStream inst = responseEntity.getContent();
			byte[] a = decompress(inst);
			Log.d("Performance","start parsing");
			helper.stop=System.currentTimeMillis();
			
			entries = parser.parseODataFeed(new String(a),"TravelAgencies_DQ", schema);
			helper.end= System.currentTimeMillis();
			Log.d("Performance","stop parsing. Parsing time ="+(helper.end-helper.stop));
			//cache.setODataEntries(entries);
			//perst = new Persistence(pref, logger);
			//perst.storeCache(cache);
			//cache.clear();
			//perst.loadCache(cache);
			Log.d("Performance","1000 entries collection success");
		}
		if (reqtype==5) {
			helper.stop=System.currentTimeMillis();
			//ParseXML(EntityUtils.toString(responseEntity));
			entries = parser.parseODataFeed(EntityUtils.toString(responseEntity),"TravelAgencies_DQ", schema);
			helper.end=System.currentTimeMillis();
			Log.d("Performance","stop parsing. Parsing time ="+(helper.end-helper.stop));
			Log.d("Performance","GET single data success");
			//serviceDoc=null;
			//=null;
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
		//Log.d("Timings",reqtype+" - Req/Resp="+(helper.stop-helper.start)+" Parser="+(helper.end-helper.stop)+" E2E="+(helper.end-helper.start));
		Thread.sleep(1000);
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
	       // ByteArrayInputStream bis = (ByteArrayInputStream) in;
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
