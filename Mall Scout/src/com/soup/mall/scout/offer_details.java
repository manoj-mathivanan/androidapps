package com.soup.mall.scout;

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
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class offer_details extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.offer_details);

		load();
		
		final Button buddy = (Button) findViewById(R.id.button10);
		buddy.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent goToNextActivity = new Intent(offer_details.this, buddy.class);
				startActivity(goToNextActivity);
				
			}
		});
		
		final Button locate = (Button) findViewById(R.id.buttonloc);
		locate.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent goToNextActivity = new Intent(offer_details.this, map.class);
				startActivity(goToNextActivity);
				
			}
		});
		
	}
	
	


	public void load() {
		// TODO Auto-generated method stub
		DefaultHttpClient sClient =  new  DefaultHttpClient();
		
		String URL = "https://smpmaas-smpdemo.hana.ondemand.com/MallScout/shopes('"+helper.shopid.toString()+"')";
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
                    ParseXML1(st);
        } catch (Exception e) {
            e.printStackTrace();
      }
        try {  
        	URL = "https://smpmaas-smpdemo.hana.ondemand.com/MallScout/offers('"+helper.shopid.toString()+"')";
            HttpGetServiceDoc = new HttpGet(URL);
            HttpGetServiceDoc.setHeader("Authorization","Basic R1cxMDBfVVNFUjE0OnNhcEAxMjM=");
            HttpGetServiceDoc.setHeader("Content-Type", "application/atom+xml");
            HttpGetServiceDoc.setHeader("X-SUP-APPCID", helper.APPID);
            HttpGetServiceDoc.setHeader("X-Requested-With", "XMLHttpRequest");
            
                  httpResponse = (BasicHttpResponse) sClient.execute(HttpGetServiceDoc);
                  InputStream inst = httpResponse.getEntity().getContent();
                  String st = readString(inst);
                  inst.close();	                    
                  ParseXML(st);
      } catch (Exception e) {
          e.printStackTrace();
    }
        final ImageView img01 = (ImageView)findViewById(R.id.imagedet);
        switch (helper.shopid) 
        {
        case 1:
        img01.setImageResource(R.drawable.m1);
        break;
        case 2:
            img01.setImageResource(R.drawable.m2);
            break;
        case 3:
            img01.setImageResource(R.drawable.m3);
            break;
        case 4:
            img01.setImageResource(R.drawable.m4);
            break;
        case 5:
            img01.setImageResource(R.drawable.m5);
            break;
        case 6:
            img01.setImageResource(R.drawable.m6);
            break;
        case 7:
            img01.setImageResource(R.drawable.m7);
            break;
        case 8:
            img01.setImageResource(R.drawable.m8);
            break;
        }
        
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
	public void ParseXML1(String xmlRecords)
	{
		Log.d("Performance","Start Parsing of GET single record");
		int i=0;
	    try {
	    	TextView tname = (TextView)findViewById(R.id.textname);
	    	TextView tfloor = (TextView)findViewById(R.id.textfloor);
	    	TextView tloc = (TextView)findViewById(R.id.textloc);
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
          	  NodeList nodelist = element.getElementsByTagName("d:shop_name");
          	  Element element1= (Element) nodelist.item(0);
          	  NodeList fstname = element1.getChildNodes();
          	  //points = (fstname.item(0)).getNodeValue();
          	  tname.setText(tname.getText().toString()+(fstname.item(0)).getNodeValue());
            }	      
            if (node.getNodeType() == Node.ELEMENT_NODE)
            {
          	  Element element =(Element) node;
          	  NodeList nodelist = element.getElementsByTagName("d:floor");
          	  Element element1= (Element) nodelist.item(0);
          	  NodeList fstname = element1.getChildNodes();
          	  //points = (fstname.item(0)).getNodeValue();
          	  tfloor.setText(tfloor.getText().toString()+(fstname.item(0)).getNodeValue());
            }
            if (node.getNodeType() == Node.ELEMENT_NODE)
            {
          	  Element element =(Element) node;
          	  NodeList nodelist = element.getElementsByTagName("d:location");
          	  Element element1= (Element) nodelist.item(0);
          	  NodeList fstname = element1.getChildNodes();
          	  //points = (fstname.item(0)).getNodeValue();
          	  tloc.setText(tloc.getText().toString()+(fstname.item(0)).getNodeValue());
            }
	        }
	    }
	    catch (Exception e) {
	        e.printStackTrace();
	    } 
	  }

	public void ParseXML(String xmlRecords)
	{
		Log.d("Performance","Start Parsing of GET single record");
		int i=0;
	    try {
	    	TextView tname = (TextView)findViewById(R.id.textoffer);
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
          	  NodeList nodelist = element.getElementsByTagName("d:offer_text");
          	  Element element1= (Element) nodelist.item(0);
          	  NodeList fstname = element1.getChildNodes();
          	  //points = (fstname.item(0)).getNodeValue();
          	  tname.setText(tname.getText().toString()+(fstname.item(0)).getNodeValue());
            }	      
            }
	    }
	    catch (Exception e) {
	        e.printStackTrace();
	    } 
	  }


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}


}
