package com.soup.mall.scout;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHttpResponse;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class myoffers extends ListActivity {
	
	  @Override
	  public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.myoffers);
	    
//DefaultHttpClient sClient =  new  DefaultHttpClient();
ArrayAdapter<CharSequence> adapter;
		/*String URL = "http://uvo1u63e0zy4xf45qye.vm.cld.sr:8000/sap/opu/odata/sap/ZSHOPSCOUT3_SRV/customer";
        HttpGet HttpGetServiceDoc = new HttpGet(URL);
        BasicHttpResponse httpResponse;
        try {  
              HttpGetServiceDoc.setHeader("Authorization","Basic R1cxMDBfVVNFUjE0OnNhcEAxMjM=");
              HttpGetServiceDoc.setHeader("Content-Type", "application/atom+xml");
              HttpGetServiceDoc.setHeader("X-SUP-APPCID", helper.APPID);
              HttpGetServiceDoc.setHeader("X-Requested-With", "XMLHttpRequest");
                    httpResponse = (BasicHttpResponse) sClient.execute(HttpGetServiceDoc);
                    InputStream inst = httpResponse.getEntity().getContent();
                    String st = readString(inst);
                    inst.close();	                    
                    collectionItems = ParseXML(st);*/
//collectionItems[0]="Zombie sale 70% off";
//collectionItems[1]="Buy 2 get 2";
String[] collectionItems = {"Zombie sale 70% off - Woodlands","Buy 2 get 2 - Lee"};
                    adapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_list_item_1,collectionItems);
            		setListAdapter(adapter);
            		ListView lv = getListView();
            		lv.setVisibility(View.VISIBLE);
            		lv.setTextFilterEnabled(true);
            		lv.setBackgroundColor(0);
            		//TextView txtscore = (TextView)findViewById(R.id.textscore);
        	        //txtscore.setText("Your current score: "+helper.score.toString());
       /* } catch (Exception e) {
            e.printStackTrace();
      }*/
        
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

		        InputStream is = new ByteArrayInputStream(xmlRecords.getBytes());

				Document doc = dbb.parse(is);

		        NodeList nodes = doc.getElementsByTagName("d:customer_name");
		        NodeList nodes1 = doc.getElementsByTagName("d:game_score");
		        coll = new String[nodes.getLength()];
	
		        for (i = 0; i < nodes.getLength(); i++) {
		        	Node value=nodes.item(i).getChildNodes().item(0);
		        	Node vName=nodes1.item(i).getChildNodes().item(0);
		           String abb =value.getNodeValue();
		           String abbName =vName.getNodeValue();
		           coll[i]= abb+"-"+abbName ;
		        }	        
		        return coll;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

	}