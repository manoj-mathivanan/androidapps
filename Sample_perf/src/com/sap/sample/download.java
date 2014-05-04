package com.sap.sample;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;

import org.apache.http.util.EntityUtils;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import android.content.Context;
import android.util.Log;

import com.sap.mobile.lib.sdmconfiguration.ISDMPreferences;
import com.sap.mobile.lib.sdmconfiguration.SDMPreferences;
import com.sap.mobile.lib.sdmconfiguration.SDMPreferencesException;
import com.sap.mobile.lib.sdmconnectivity.ISDMNetListener;
import com.sap.mobile.lib.sdmconnectivity.ISDMRequest;
import com.sap.mobile.lib.sdmconnectivity.ISDMRequestStateElement;
import com.sap.mobile.lib.sdmconnectivity.ISDMResponse;
import com.sap.mobile.lib.sdmconnectivity.SDMBaseRequest;
import com.sap.mobile.lib.sdmconnectivity.SDMConnectivityParameters;
import com.sap.mobile.lib.sdmconnectivity.SDMRequestManager;
import com.sap.mobile.lib.sdmconnectivity.SDMRequestStateElement;
import com.sap.mobile.lib.sdmparser.ISDMParser;
import com.sap.mobile.lib.sdmparser.SDMParser;
import com.sap.mobile.lib.sdmparser.SDMParserException;
import com.sybase.mobile.lib.log.ODPLogger;

public class download implements ISDMNetListener{
	public static final String TAG = "ServiceDocProvider";
	private volatile  Thread docDownloaderThread;
	public  String serviceDocument = null;
	public  byte[] serviceDocument1 = null;
	public static ISDMParser parser;
	public static int putResponseCode = 0;
	private String URL;
	public static String baseUrl;
	public static byte[] imageData;
	static byte[] uploaddata =  null;
	public MyLogger mLogger = new MyLogger();
	public Context contex;
	public SDMPreferences mPreferences;
	public SDMConnectivityParameters mParameters;
	public SDMRequestManager sDMRequestManager;
	public ODPLogger log = new ODPLogger();
	//AlertDialog.Builder builder = new AlertDialog.Builder(contex);
	public  download(Context contex){
		this.contex=contex;
	
		
		mPreferences = new SDMPreferences(contex, mLogger);
		try {

			mPreferences.setStringPreference(ISDMPreferences.SAP_APPLICATIONID_HEADER_VALUE,"attachment");
			mPreferences.setIntPreference(ISDMPreferences.SDM_CONNECTIVITY_CONNTIMEOUT,25000);
			mPreferences.setIntPreference(ISDMPreferences.SDM_CONNECTIVITY_HTTPS_PORT,443);
			mPreferences.setIntPreference(ISDMPreferences.SDM_CONNECTIVITY_HTTP_PORT,80);
			mPreferences.setStringPreference(ISDMPreferences.SDM_CONNECTIVITY_HANDLER_CLASS_NAME,"com.sybase.mobile.lib.client.IMOConnectionHandler");
			//mPreferences.setStringPreference(ISDMPreferences.SDM_CONNECTIVITY_HANDLER_CLASS_NAME,"com.sap.mobile.lib.sdmconnectivity.SocketConnectionHandler");
		} catch (SDMPreferencesException e) {
			mLogger.e(getClass().getName(), "Error setting preferences.", e);
		}
		mParameters= new SDMConnectivityParameters();
		ImageUploadDownload obj = new ImageUploadDownload();
			// Username and password is mandatory
			mParameters.setUserName("perfandroid");
			mParameters.setUserPassword("perfandroid");
			mParameters.enableXsrf(true);
		
		/*mParameters.setUserName("LINKEAN");
		mParameters.setUserPassword("CUSTFACT");*/
			// SDMPreferences needs the context and a logger implementation
			 sDMRequestManager=new SDMRequestManager(mLogger, mPreferences, mParameters, SDMRequestManager.MAX_THREADS);
		try {
			parser = new SDMParser(mPreferences, mLogger);
		} catch (SDMParserException e2) {
			
			Log.e(TAG, "Parser Initialization failed!");
			return;
		}
	}
	public byte[] getServiceDocument(String URL) {
		Log.d(TAG, "Get Service Document");
		log.p("Started making Request for Download Image","Request Sent for Image");
		Log.d(TAG, "Get Service Document");
		/**
		 * create request
		 */
		final ISDMRequest request = new SDMBaseRequest();
		this.URL = URL;
		request.setRequestMethod(ISDMRequest.REQUEST_METHOD_GET);
		HashMap<String, String> headers = new HashMap<String, String>();
		headers.put("Content-Type", "Application/atom+xml");
		headers.put("X-Requested-With", "XMLHttpRequest");

		// headers.put(HEADER_ACCEPT,ATOM_XML);

		request.setListener(this);
		((SDMBaseRequest) request).setRequestUrl(URL);
		request.setHeaders(headers);
		docDownloaderThread = new Thread() {

			public void run() {

				sDMRequestManager.makeRequest(request);
			}
		};
		int timeOut = 0;
		docDownloaderThread.start();
		try {
			while (serviceDocument1 == null)
				timeOut = timeOut + 1;
			Thread.sleep(3000);

			Log.i("AMBIKA",""+timeOut);
		} catch (Exception e) {
			Log.i("AMBIKA","Exception",e);
		}
		Log.i("AMBIKA",serviceDocument1.toString());
		return serviceDocument1;
	}
	
	
	// Added by Rohith
	
	public int uploadImage(String URL, String filePath) {
		
		
		log.p("Started making Request for Upload Image","Request Sent for Image");
		final ISDMRequest putRequest = buildPUTRequest(URL,filePath,this);
				sDMRequestManager.makeRequest(putRequest);
			
		return putResponseCode;
		
		
	}
	
    public static final ISDMRequest buildPUTRequest(final String url,String filePath,
            final ISDMNetListener listener) {
      
    	//log.p("Started making Request for Image","Request Sent for Image");
        
        HashMap<String, String> headers = new HashMap<String, String>();
  headers.put("Content-Type", "image/jpeg");
  headers.put("X-Requested-With", "XMLHttpRequest");
  // headers.put("X-Requested-With", "XMLHttpRequest");
  // headers.put(HEADER_ACCEPT,ATOM_XML);

        
        ISDMRequest ret = new SDMBaseRequest();
        ret.setRequestMethod(ISDMRequest.REQUEST_METHOD_PUT);
        ret.setHeaders(headers);
        ret.setRequestUrl(url);
        ret.setListener(listener);
       
     //   AssetManager assetManager = getResources().getAssets(); 
        
        byte[] data =  null;
        
        try {
        	//filepath = "/data/app/picK.jpg"
  		data = readFile(filePath);
  	} catch (IOException e) {
  		// TODO Auto-generated catch block
  		e.printStackTrace();
  	}
     //   ret.setData(uploaddata);
        ret.setData(data);
        return ret;
	
}

public static byte[] readFile (String file) throws IOException { 
       return readFile(new File(file)); 
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
	// Added by Rohith
   
   
/*private String getContent(HttpEntity entity) {
		if (entity == null)
			return null;
		String ret = null;
		try {
			ret = EntityUtils.toString(entity);
		} catch (ParseException e) {
			Log.w(TAG, "getContent()" + e.getMessage());
		} catch (IOException e) {
			Log.w(TAG, "getContent()" + e.getMessage());
		}
		return ret;
	}*/

/*	public String ParseXML(String xmlRecords)
	{
		int i=0;
		String coll = "";
		String abb = "";
	    try {
	        DocumentBuilderFactory dbf =
	            DocumentBuilderFactory.newInstance();
	        DocumentBuilder db = dbf.newDocumentBuilder();
	        InputSource is = new InputSource();
	        is.setCharacterStream(new StringReader(xmlRecords));


	        Document doc = db.parse(is);
	        NodeList nodes = doc.getElementsByTagName("atom:entry");
	        for (i = 0; i < nodes.getLength(); i++) {
	           Element element = (Element) nodes.item(i);
	           
	           NodeList pdf1 = element.getElementsByTagName("d:PDFFileContent");
	           Element pdfline = (Element) pdf1.item(0);
	           System.out.println("PDF: "+getCharacterDataFromElement(pdfline));

	           
	           abb = getCharacterDataFromElement(pdfline);
	           coll= new String(abb);
	          
	        }
	        Log.i("AMBIKA", "abb: ");
	        return coll;
	    }
	    catch (Exception e) {
	        e.printStackTrace();
	    }
	return coll;   
       
	    
	  }*/

	public static String getCharacterDataFromElement(Element e) {
	    Node child = e.getFirstChild();
	    if (child instanceof CharacterData) {
	       CharacterData cd = (CharacterData) child;
	       return cd.getData();
	    }
	    
	    return "?";
	  }
	/**
	 * The request listener implementation
	 */


	public void onError(ISDMRequest rq, ISDMResponse rse,
			ISDMRequestStateElement arg2) {
		// TODO Auto-generated method stub
		Log.d(TAG, "Error after request, url: " + rq.getRequestUrl() + ", "
				+ rq.getRequestMethod());
		String errorCode = "";
		/**
		 * Evaluating error code from SDMRequestStateElement
		 */
		
		
		switch (arg2.getErrorCode()) {
		case SDMRequestStateElement.AUTHENTICATION_ERROR:
			/**
			 * Authentication-related error
			 */
			errorCode = "Authentication error.";
			break;
		case SDMRequestStateElement.CLIENT_ERROR:
			/**
			 * Error in client code
			 */
			errorCode = "Error in mobile client!";
			break;
		case SDMRequestStateElement.HTTP_ERROR:
			/**
			 * Error in HTTP layer
			 */
			errorCode = "HTTP error occured.";
			break;
		case SDMRequestStateElement.NETWORK_ERROR:
			/**
			 * there was a network error, when there was a read/write to/from
			 * stream
			 */
			errorCode = "Error in network connection.";
			break;
		case SDMRequestStateElement.PARSE_ERROR:
			/**
			 * problem with parsing the response xml
			 */
			errorCode = "Error in parsing the response from the server.";
			break;
		default:
			errorCode = "Unknown error.";
		}
		String exceptionMsg = "null";
		/**
		 * Get the actual exception from SDMRequestStateElement
		 */
		
		final Exception fex = arg2.getException();
		if (fex == null) {
			errorCode += "";
		} else {
			errorCode += " " + fex.getCause();
			exceptionMsg = fex.getMessage();
		}
		log.p("Stopped making Request for Image came to OnError","Response Received for Image");
		/**
		 * Get the HTTP status code Same value as
		 * HttpResponse.getStatusLine().getStatusCode(), but this is still
		 * available when no response from the server.
		 */
		final int httpStatusCode = arg2.getHttpStatusCode();
		Log.e(TAG, "Failed request:" + errorCode + ", HTTP " + httpStatusCode
				+ ", Exception: " + exceptionMsg);
		
		
	}
	public void onSuccess(ISDMRequest rq, ISDMResponse rsp) {
		// TODO Auto-generated method stub
		Log.d(TAG, "Success after request, " + rq.getRequestUrl());
		
		/**
		 * Parsing service document
		 */
		
	
		if (rq.getRequestMethod() == 2 || rq.getRequestMethod()==3) {
			log.p("Stopped making Request for Upload Image","Response Received for Image");
			putResponseCode = rsp.getStatusLine().getStatusCode();
		
		}
		
		if (rq.getRequestUrl().equals(URL)) {
			
			try {
				log.p("Stopped making Request for Download Image","Response Received for Image");
				imageData = EntityUtils.toByteArray(rsp.getEntity());
				serviceDocument1 = imageData;
				putResponseCode = rsp.getStatusLine().getStatusCode();
			} catch (IllegalArgumentException e) {
				Log.e(TAG, "Service Document is null! " + e);
				return;
			} catch (Exception e) {
				Log.e(TAG, "Parser error! " + e);
			}
			/**
			 * Add the received document to the cache
			 */
			
			  
		}
		
	}


}
