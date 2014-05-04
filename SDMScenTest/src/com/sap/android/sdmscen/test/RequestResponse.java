package com.sap.android.sdmscen.test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import junit.framework.AssertionFailedError;
import junit.framework.TestResult;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import android.test.AndroidTestCase;
import android.util.Log;

import com.sap.mobile.lib.sdmcache.SDMCache;
import com.sap.mobile.lib.sdmconfiguration.ISDMPreferences;
import com.sap.mobile.lib.sdmconfiguration.SDMPreferences;
import com.sap.mobile.lib.sdmconnectivity.ISDMNetListener;
import com.sap.mobile.lib.sdmconnectivity.ISDMRequest;
import com.sap.mobile.lib.sdmconnectivity.ISDMRequestStateElement;
import com.sap.mobile.lib.sdmconnectivity.ISDMResponse;
import com.sap.mobile.lib.sdmconnectivity.SDMBaseRequest;
import com.sap.mobile.lib.sdmconnectivity.SDMConnectivityParameters;
import com.sap.mobile.lib.sdmconnectivity.SDMRequestManager;
import com.sap.mobile.lib.sdmparser.ISDMODataEntry;
import com.sap.mobile.lib.sdmparser.ISDMODataFunctionImport;
import com.sap.mobile.lib.sdmparser.ISDMODataLink;
import com.sap.mobile.lib.sdmparser.ISDMODataSchema;
import com.sap.mobile.lib.sdmparser.ISDMODataServiceDocument;
import com.sap.mobile.lib.sdmparser.SDMODataEntry;
import com.sap.mobile.lib.sdmparser.SDMParser;
import com.sap.mobile.lib.sdmpersistence.SDMPersistence;
import com.sap.mobile.lib.supportability.ISDMLogger;
import com.sap.mobile.lib.supportability.SDMLogger;

public class RequestResponse extends AndroidTestCase implements ISDMNetListener {

	protected SDMLogger logger;
	protected SDMPreferences pref;
	protected SDMConnectivityParameters param = new SDMConnectivityParameters();
	protected SDMRequestManager reqMan;
	protected TestResult result;
	protected SDMParser parser;
	protected Boolean verdict = null;
	private Object lockObject = new Object();
	public SDMBaseRequest br = new SDMBaseRequest();	
	protected SDMCache cache;
	protected SDMPersistence perst;
	private HttpResponse mResponse;
	public ISDMRequest getrequest = new SDMBaseRequest();	
	public SDMBaseRequest getSchema = new SDMBaseRequest();
	protected static SDMBaseRequest getCollec = new SDMBaseRequest();
	public SDMBaseRequest search = new SDMBaseRequest();
	public ISDMRequest deleterequest = new SDMBaseRequest();
	public ISDMRequest postrequest = new SDMBaseRequest();
	public ISDMRequest putrequest = new SDMBaseRequest();
	public static List<ISDMODataEntry> entries;
	String collectionId = "TravelagencyCollection";
	public ISDMODataEntry entry;
	public boolean isCertRegistration;
	private String ERRORMSG = "";
	protected String PUT_ID = "firstname ne AND";
	public String subscription_id = "";
	public int post_flag = 0;
	public String agency_num = "";
	public RequestResponse()
	{
		
	}

	
	@Override
	public void run(TestResult result) {
		try{
			
			setUp();	
			this.result = result;
			this.result.startTest(this);
			
			if(Helper.serviceDocUrl.indexOf("odata")>0)
				param.enableXsrf(true);
			Log.i("Deepika","App End Point:" + Helper.serviceDocUrl);
			GetOperation();
			postOData();
			putoData();
			deleteOData();
				
			tearDown();
			this.result.endTest(this);
			
		}
			catch(Exception e){
			result.addFailure(this, new AssertionFailedError(e.toString()));
			result.endTest(this);
			return;
			}
	}
	
	@Override
	protected void tearDown() throws Exception {
		try {
					
			
			pref.resetPreference(ISDMPreferences.SDM_CONNECTIVITY_HANDLER_CLASS_NAME);
			pref.removePreference("CUSTOM_PREF_USERNAME");
			cache.clear();
			reqMan.terminate();
		
		} catch (Exception e) {
			result.addFailure(this, new AssertionFailedError("Exception for teardown in RR:" +e.toString()));
		}
	}
	
	protected void setUp() throws Exception {
		
		try{


		logger = new SDMLogger();
		
		pref = new SDMPreferences(mContext, logger);
		pref.setBooleanPreference(ISDMPreferences.SDM_PERSISTENCE_SECUREMODE, false);

		
		pref.setStringPreference("CUSTOM_PREF_USERNAME", Helper.USERNAME);
		logger.setLogLevel(ISDMLogger.DEBUG);		
		param = new SDMConnectivityParameters();		
		if(isCertRegistration)
		{
			param.setUserName(Helper.USERNAME_CERT.toString());
			param.setUserPassword(Helper.PASSWORD_CERT);
			Log.i("Deepika","Certificate:"+ Helper.PASSWORD_CERT);
		}
		else
		{
			param.setUserName(pref.getStringPreference("CUSTOM_PREF_USERNAME"));
			param.setUserPassword(Helper.PASSWORD);

		}
		reqMan = new SDMRequestManager(logger, pref, param, 1);	
		parser = new SDMParser(pref, logger);
		pref.setIntPreference(ISDMPreferences.SDM_CONNECTIVITY_CONNTIMEOUT, 7000);
		cache = new SDMCache(pref, logger);
		cache.clear();	
					
		}  catch (Exception e) {
			verdict = false;
			result.addFailure(this, new AssertionFailedError("Exception for Setup for RR:" +e.toString()));
		}
			
		
		
	}
	
	public void GetOperation()
	{
		try
		{

		getrequest.setRequestUrl(Helper.serviceDocUrl);

		
		getrequest.setRequestMethod(SDMBaseRequest.REQUEST_METHOD_GET);
		getrequest.setListener(this);		
		
		reqMan.makeRequest(getrequest);
		synchronized (lockObject) {
			try {
				lockObject.wait();
			} catch (InterruptedException e) {
				result.addError(this, e);
			}
		}
		
				
		
		//get Schema
		String schemaUrl = Helper.serviceDocUrl+"$metadata";
		getSchema.setRequestUrl(schemaUrl);
		getSchema.setRequestMethod(SDMBaseRequest.REQUEST_METHOD_GET);
		getSchema.setListener(this);		
		reqMan.makeRequest(getSchema);
		
				
		synchronized (lockObject) {
			try {
				lockObject.wait();
			} catch (InterruptedException e) {
				result.addError(this, e);
			}
		}
		
		String collecUrl = Helper.serviceDocUrl + collectionId;
	
		getCollec.setRequestUrl(collecUrl);
		getCollec.setRequestMethod(SDMBaseRequest.REQUEST_METHOD_GET);
		getCollec.setListener(this);
		reqMan.makeRequest(getCollec);

		synchronized (lockObject) {
			try {
				lockObject.wait();
			} catch (InterruptedException e) {
				Log.i("App msg", "request failed: " + e.getMessage());

			}

		}
		
		}
		catch(Exception e)
		{
			Log.i("Deepika new exception",e.toString());
		}
	}
	
	
	
	
	@Override
	public void onError(ISDMRequest aRequest, ISDMResponse aResponse,
			ISDMRequestStateElement aRequestStateElement) {
		// TODO Auto-generated method stub
		
		Log.i("Deepika","Inside onError");
		
		try
		{
		
			
//			Log.i("Deepika","In Error:" + aResponse.getStatusLine().toString());
			//To get response code
			Log.i("Deepika","Status Code:" + String.valueOf(aResponse.getStatusLine().getStatusCode()));
			Log.i("Deepika","Err msg:" + aResponse.getStatusLine().getReasonPhrase());
			if( aRequest.equals(getrequest)){
				result.addFailure(this,new AssertionFailedError("Get of service document failed:   " + String.valueOf(aResponse.getStatusLine().getStatusCode()) + ":" +aResponse.getStatusLine().getReasonPhrase()));
				Log.i("Deepika","For failure GET");
			}		
			
			if( aRequest.equals(getSchema)){
				result.addFailure(this,new AssertionFailedError("Get of metadata document failed:   " + String.valueOf(aResponse.getStatusLine().getStatusCode()) + ":" +aResponse.getStatusLine().getReasonPhrase()));
				Log.i("Deepika","For failure GET metadata");
			}
			if(aRequest.equals(getCollec))
			{
				result.addFailure(this,new AssertionFailedError("Get of metadata document failed:    " +String.valueOf(aResponse.getStatusLine().getStatusCode()) + ":" + aResponse.getStatusLine().getReasonPhrase()));
				Log.i("Deepika","For failure GET Collection");
			}
			
			if( aRequest.equals(putrequest)){			
					
				result.addFailure(this,new AssertionFailedError("PUT failed:    " +String.valueOf(aResponse.getStatusLine().getStatusCode()) + ":" + aResponse.getStatusLine().getReasonPhrase()));
				
				Log.i("Deepika","For failure PUT");
			}
			if( aRequest.equals(deleterequest)){			
				Log.i("Deepika","For failure DELETE");
				result.addFailure(this,new AssertionFailedError("DELETE failed:   " +String.valueOf(aResponse.getStatusLine().getStatusCode()) + ":" + aResponse.getStatusLine().getReasonPhrase()));		
			}
			if (aRequest.equals(postrequest)){		
		
				result.addFailure(this,new AssertionFailedError("POST failed:     " + String.valueOf(aResponse.getStatusLine().getStatusCode()) + ":" +aResponse.getStatusLine().getReasonPhrase()));
				Log.i("Deepika","For failure POST");
			}
			synchronized (lockObject) {
				lockObject.notify();
			}
			
			verdict = false;
		}
		catch(Exception e)
		{
			synchronized (lockObject) {
				lockObject.notify();
			}
			e.printStackTrace();
		}
	}

	@Override
	public void onSuccess(ISDMRequest aRequest, ISDMResponse aResponse) {
		// TODO Auto-generated method stub
		HttpEntity responseEntity = aResponse.getEntity();
		
		if( aRequest.equals(getrequest)){
			try {
				
				ISDMODataServiceDocument serviceDoc = parser.parseSDMODataServiceDocumentXML(EntityUtils.toString(responseEntity));
				cache.setSDMODataServiceDocument(serviceDoc);
                
				verdict = true;
				Log.i("Deepika","For successful GET");
				
				synchronized (lockObject) {
					lockObject.notify();
				}
			} catch (Exception e){
				result.addFailure(this, new AssertionFailedError("Parsing of service document failed:"+e.toString()));			
				verdict = false;
				synchronized (lockObject) {
					lockObject.notify();
				}
			}
		}		
		
		if( aRequest.equals(getSchema)){
			try{
				String xmlString = EntityUtils.toString(responseEntity);
		
				ISDMODataSchema schema = parser.parseSDMODataSchemaXML(xmlString, cache.getSDMODataServiceDocument());
				
				cache.setSDMODataSchema(schema);
				
				verdict = true;
				Log.i("Deepika","For successful GET metadata");
				synchronized (lockObject) {
					lockObject.notify();
				}
				
			}catch(Exception e){
				result.addFailure(this, new AssertionFailedError("Parsing of metadata document failed:"+e.toString()));			
				verdict = false;
				synchronized (lockObject) {
					lockObject.notify();
				}
			}
		}
		if (aRequest.equals(getCollec)) {
			try {
				verdict = true;

				String xmlString = EntityUtils.toString(responseEntity);
				entries = parser.parseSDMODataEntriesXML(xmlString,
						collectionId, cache.getSDMODataSchema());
				
				cache.setSDMODataEntries(entries);
				perst = new SDMPersistence(pref, logger);
				
				perst.storeSDMCache(cache);
				
				cache.clear();
				
				perst.loadSDMCache(cache);
				
				
				
				Log.i("Deepika","For successfa ul GET Collection");
						
				synchronized (lockObject) {
					lockObject.notify();
				}

			} catch (Exception e) {
				result.addFailure(this, new AssertionFailedError("Parsing of metadata document failed:"+e.toString()));	

				verdict = false;
				synchronized (lockObject) {
					lockObject.notify();
				}
			}
		}
		
		if( aRequest.equals(putrequest)){			
				
				verdict = true;		
				Log.i("Deepika","For successful PUT");
				synchronized (lockObject) {
					lockObject.notify();
				}
			
			
		}
		if( aRequest.equals(deleterequest)){			
			Log.i("Deepika","For successful DELETE");
			verdict = true;	
			synchronized (lockObject) {
				lockObject.notify();
			}			
		}
		if (aRequest.equals(postrequest)){		
	
			try {	
			
					entry = (parser.parseSDMODataEntriesXML(EntityUtils.toString(responseEntity), "TravelagencyCollection", cache.getSDMODataSchema())).get(0);
					agency_num = entry.getPropertyValue("agencynum");
				
				verdict = true;
				Log.i("Deepika","For successful POST");
				synchronized (lockObject) {
					lockObject.notify();
				}			
				
			} catch (Exception e){
				result.addFailure(this, new AssertionFailedError("Parsing the entry of POST failed:" +e.toString()));			
				verdict = false;
				synchronized (lockObject) {
					lockObject.notify();
				}
			}
		}
		
	}	
	
	public void postOData()
    {
    	post_flag=1;
    	String PostURL =Helper.serviceDocUrl+"TravelagencyCollection";
    	
    	
    
    	String postdata ="<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
                +"<entry\n"
                +"xml:base=\"http://vmw3815.wdf.sap.corp:50009/sap/opu/odata/iwfnd/RMTSAMPLEFLIGHT/\"\n"
                +"xmlns=\"http://www.w3.org/2005/Atom\" xmlns:m=\"http://schemas.microsoft.com/ado/2007/08/dataservices/metadata\"\n"
                +"xmlns:d=\"http://schemas.microsoft.com/ado/2007/08/dataservices\">\n"
                +"<category term=\"RMTSAMPLEFLIGHT.Travelagency\"\n"
                      +"scheme=\"http://schemas.microsoft.com/ado/2007/08/dataservices/scheme\" />\n"
                +"<content type=\"application/atom+xml\">\n"
                      +"<m:properties>\n"
                            +"<d:agencynum>34567899</d:agencynum>\n"
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
          +"</entry>";

		
		
		postrequest.setRequestMethod(ISDMRequest.REQUEST_METHOD_POST);
		postrequest.setRequestUrl(PostURL);
		postrequest.setListener(this);
			
		Map<String,String> headers = new HashMap<String,String>();

        headers.put("Content-Type", "application/atom+xml");

        postrequest.setHeaders(headers);

		final byte[] theByteArray = postdata.getBytes();
		postrequest.setData(theByteArray);
	    reqMan.makeRequest(postrequest);
	    
	    synchronized (lockObject) {
			try {
				lockObject.wait();
			} catch (InterruptedException e) {
				Log.i("App msg", "request failed: "+e.getMessage());
				
			}
	    }
    	
    }
    
    public void putoData()
    {
    	String putBody="<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
                +"<entry\n"
                +"xml:base=\"http://vmw3815.wdf.sap.corp:50009/sap/opu/odata/iwfnd/RMTSAMPLEFLIGHT/\"\n"
                +"xmlns=\"http://www.w3.org/2005/Atom\" xmlns:m=\"http://schemas.microsoft.com/ado/2007/08/dataservices/metadata\"\n"
                +"xmlns:d=\"http://schemas.microsoft.com/ado/2007/08/dataservices\">\n"
                +"<category term=\"RMTSAMPLEFLIGHT.Travelagency\"\n"
                      +"scheme=\"http://schemas.microsoft.com/ado/2007/08/dataservices/scheme\" />\n"
                +"<content type=\"application/atom+xml\">\n"
                      +"<m:properties>\n"
                            +"<d:agencynum>34567899</d:agencynum>\n"
                            +"<d:NAME>Happy Holiday126</d:NAME>\n"
                            +"<d:STREET>Rastenburger Str. 12</d:STREET>\n"
                            +"<d:POSTBOX />\n"
                            +"<d:POSTCODE>28779</d:POSTCODE>\n"
                            +"<d:CITY>Bremen</d:CITY>\n"
                            +"<d:COUNTRY>DE</d:COUNTRY>\n"
                            +"<d:REGION>04</d:REGION>\n"
                            +"<d:TELEPHONE>11111</d:TELEPHONE>\n"
                            +"<d:URL>http://www.happy</d:URL>\n"
                            +"<d:LANGU>D</d:LANGU>\n"
                            +"<d:CURRENCY>EUR</d:CURRENCY>\n"
                            +"<d:mimeType>text/html</d:mimeType>\n"
                      +"</m:properties>\n"
                +"</content>\n"
          +"</entry>";
    	String editUrl =Helper.serviceDocUrl+"TravelagencyCollection('"+agency_num+"')";
    
		Map<String,String> headers = new HashMap<String,String>();

        headers.put("Content-Type", "application/atom+xml");

        putrequest.setHeaders(headers);
	

		
		final byte[] theByteArray = putBody.getBytes();
		putrequest.setData(theByteArray);
		putrequest.setRequestUrl(editUrl);
		putrequest.setListener(this);
		
			
		putrequest.setRequestMethod(SDMBaseRequest.REQUEST_METHOD_PUT);
		reqMan.makeRequest(putrequest);
	
		synchronized (lockObject) {
			try {
				lockObject.wait();
			} catch (InterruptedException e) {
				Log.i("App msg", "request failed: "+e.getMessage());
			}
		}	
	
    }
    
    public void deleteOData()
    {
       
    	String DeleteUrl =Helper.serviceDocUrl+"TravelagencyCollection('"+agency_num+"')";
		
		deleterequest.setRequestMethod(ISDMRequest.REQUEST_METHOD_DELETE);
		deleterequest.setRequestUrl(DeleteUrl);
		deleterequest.setListener(this);
		
		Map<String,String> headers = new HashMap<String,String>();

        headers.put("Content-Type", "application/atom+xml");

        deleterequest.setHeaders(headers);
		
		reqMan.makeRequest(deleterequest);
		
		synchronized (lockObject) {
			try {
				lockObject.wait();
			} catch (InterruptedException e) {
				Log.i("App msg", "request failed: "+e.getMessage());
			}
		}

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


}

    

