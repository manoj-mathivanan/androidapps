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
import org.w3c.dom.CharacterData;
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
import android.widget.TextView;

public class MainActivity extends Activity{
	String points=null;;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

final String[] collectionItems;
final Button btn = (Button) findViewById(R.id.button1);
		btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				load();
				btn.setVisibility(View.GONE);
				TextView txt1=(TextView)findViewById(R.id.TextView01);
				txt1.setVisibility(View.VISIBLE);
				TextView txt2=(TextView)findViewById(R.id.TextView02);
				txt2.setVisibility(View.VISIBLE);
				txt2.setText(points);
				 Button scout = (Button) findViewById(R.id.button3);
				 scout.setVisibility(View.VISIBLE);
			}
		});
		
		final Button startbutton = (Button) findViewById(R.id.button3);
		startbutton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent goToNextActivity = new Intent(MainActivity.this, park.class);
				startActivity(goToNextActivity);
				
			}
		});
	}
	public void load()
	{
		DefaultHttpClient sClient =  new  DefaultHttpClient();
		final EditText number_e = (EditText)findViewById(R.id.editText1);
		String URL = "https://smpmaas-smpdemo.hana.ondemand.com/MallScout/customer('"+number_e.getText().toString()+"')";
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
                    ParseXML(st);
        } catch (Exception e) {
            e.printStackTrace();
      }
        
        helper.user_num = number_e.getText().toString();
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
            	  NodeList nodelist = element.getElementsByTagName("d:game_score");
            	  Element element1= (Element) nodelist.item(0);
            	  NodeList fstname = element1.getChildNodes();
            	  points = (fstname.item(0)).getNodeValue();
            	  helper.score = Integer.parseInt(points);
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}


}
