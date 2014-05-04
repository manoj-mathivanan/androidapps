package com.sap.sample;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.http.HttpResponse;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.sap.mobile.lib.sdmconnectivity.ISDMNetListener;
import com.sap.mobile.lib.sdmconnectivity.ISDMRequest;
import com.sap.mobile.lib.sdmconnectivity.ISDMRequestStateElement;
import com.sap.mobile.lib.sdmconnectivity.ISDMResponse;

public class PushHandeler implements ISDMNetListener {
	public Context context;
	public NotificationManager mNM;
	 public  PushHandeler(Context context,NotificationManager nmh){
		 this.context=context;
		 this.mNM=nmh;
	 }
	public void onError(ISDMRequest arg0, HttpResponse arg1,
			ISDMRequestStateElement arg2) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		Log.i("AMBIKA", "Push Notif Value: "+arg1);
		Notification notification = new Notification(R.drawable.icon, "Notification",System.currentTimeMillis());       
		   PendingIntent contentIntent = PendingIntent.getActivity(context, 0,                
				   new Intent(context, samplePerf.class), 0);       
		   notification.setLatestEventInfo(context, "Notification", "Error subscribing to user as the subscription already exists", contentIntent);
				   
		  // MessageBox(rsp.toString());
		   mNM.notify(400, notification); 
		//MessageBox("push notification error");
	}

	public void onSuccess(ISDMRequest arg0, HttpResponse rsp) {
		// TODO Auto-generated method stub
		Notification notification = new Notification(R.drawable.icon, "Notification",System.currentTimeMillis());       
		   PendingIntent contentIntent = PendingIntent.getActivity(context, 0,                
				   new Intent(context, samplePerf.class), 0);       
		   notification.setLatestEventInfo(context, "Notification", "User has been modified", contentIntent);
				   
		  // MessageBox(rsp.toString());
		   mNM.notify(501, notification); 
		  // ParseXML(getContent(rsp.getEntity())); //, contentIntent);
			//Log.d(TAG, "Success after request, " + rq.getRequestUrl());
		 try{
		   Log.d("AMBIKA", "Responce: "+ rsp.getEntity().getContent());
		   Log.d("AMBIKA", "Responce: "+ rsp.getEntity());
		   Log.d("AMBIKA", "Responce: "+ rsp.getStatusLine());
		   Log.d("AMBIKA", "Responce: "+ rsp.getParams());
		   Log.d("AMBIKA", "Responce: "+ rsp.toString());
		   Log.d("AMBIKA", "Responce: "+ rsp.getLocale());
		   
		   }
		 catch (Exception e) {
			// TODO: handle exception
		}
	}
	public String ParseXML(String xmlRecords) {
		int i = 0;
		String[] coll;
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			InputSource is = new InputSource();
			is.setCharacterStream(new StringReader(xmlRecords));
			Document doc = db.parse(is);
			NodeList nodes = doc.getElementsByTagName("atom:entry");
			coll = new String[nodes.getLength()];
			// iterate the employees
			for (i = 0; i < nodes.getLength(); i++) {
				Element element = (Element) nodes.item(i);
				NodeList name = element.getElementsByTagName("atom:title");
				Element line = (Element) name.item(0);
				return getCharacterDataFromElement(line);
				/*NodeList title = element.getElementsByTagName("m:properties");
				line = (Element) title.item(0);
				System.out.println("Title: "
						+ getCharacterDataFromElement(line));
				NodeList Custitle = element.getElementsByTagName("d:agencynum");
				line = (Element) Custitle.item(0);
				String abb = getCharacterDataFromElement(line).toString();
				coll[i] = abb;
				System.out.println("Title: "
						+ getCharacterDataFromElement(line));*/
			}
			return "a push come";
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
	public void onError(ISDMRequest arg0, ISDMResponse arg1,
			ISDMRequestStateElement arg2) {
		// TODO Auto-generated method stub
		
	}
	public void onSuccess(ISDMRequest arg0, ISDMResponse arg1) {
		// TODO Auto-generated method stub
		
	}
}
