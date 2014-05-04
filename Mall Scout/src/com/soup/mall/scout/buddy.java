package com.soup.mall.scout;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;

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
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class buddy extends Activity{

	private ArrayAdapter<String> listAdapter;
	private ArrayAdapter<HashMap<String, String>> listAdapter2;
	private EndCallListener callListener = new EndCallListener();
	ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.buddy);
		ListView list = (ListView) findViewById(R.id.listView1);

		DefaultHttpClient sClient =  new  DefaultHttpClient();
		
		String URL = "https://smpmaas-smpdemo.hana.ondemand.com/MallScout/buddies";
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
                    Log.d("test",st);
                    inst.close();	                    
                    ParseXML1(st);
        } catch (Exception e) {
            e.printStackTrace();
      }
		

		SimpleAdapter mSchedule = new SimpleAdapter(this, mylist,
				R.layout.slightlycomplexrow, new String[] { "name", "amount" },
				new int[] { R.id.FROM_CELL, R.id.TO_CELL });
		list.setAdapter(mSchedule);

		TelephonyManager mTM = (TelephonyManager) this
				.getSystemService(Context.TELEPHONY_SERVICE);
		mTM.listen(callListener, PhoneStateListener.LISTEN_CALL_STATE);
		
		list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Object o = parent.getItemAtPosition(position);
				HashMap<String, String> hashMap = ((HashMap<String, String>) o);
				String str = hashMap.get("phone");
				Toast.makeText(getBaseContext(), str, Toast.LENGTH_SHORT)
						.show();
				Intent intent = new Intent(Intent.ACTION_CALL);
				intent.setData(Uri.parse("tel:" + str));
				startActivityForResult(intent, 0);
				Log.i("ShopScoutBuddyActivity", "CALL INTENT STARTED");
			}
		});

		
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.i("ShopScoutBuddyActivity", "ReqCode " + requestCode);
		Log.i("ShopScoutBuddyActivity", "ResCode " + resultCode);
		Intent goToNextActivity = new Intent(this, buddy.class);
		startActivity(goToNextActivity);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
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
		int i=0;
	    try {
	        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	        DocumentBuilder db = dbf.newDocumentBuilder();
	        InputSource is = new InputSource();
	        is.setCharacterStream(new StringReader(xmlRecords));
	        Document doc = db.parse(is);
	        NodeList nodes = doc.getElementsByTagName("m:properties");
	        HashMap<String, String> map ;//= new HashMap<String, String>();
			map = new HashMap<String, String>();
			
	        for (i = 0; i < nodes.getLength(); i++) {
              Node node = nodes.item(i);
          if (node.getNodeType() == Node.ELEMENT_NODE)
          {
        	  map = new HashMap<String, String>();
        	  Element element =(Element) node;
        	  NodeList nodelist = element.getElementsByTagName("d:customer_id");
        	  Element element1= (Element) nodelist.item(0);
        	  NodeList fstname = element1.getChildNodes();
        	  //points = (fstname.item(0)).getNodeValue();
        	  map.put("name", (fstname.item(0)).getNodeValue());
          }	      
          if (node.getNodeType() == Node.ELEMENT_NODE)
          {
        	  Element element =(Element) node;
        	  NodeList nodelist = element.getElementsByTagName("d:offer_id");
        	  Element element1= (Element) nodelist.item(0);
        	  NodeList fstname = element1.getChildNodes();
        	  //points = (fstname.item(0)).getNodeValue();
        	  map.put("offer_id", (fstname.item(0)).getNodeValue());
          }
          if (node.getNodeType() == Node.ELEMENT_NODE)
          {
        	  Element element =(Element) node;
        	  NodeList nodelist = element.getElementsByTagName("d:Amount");
        	  Element element1= (Element) nodelist.item(0);
        	  NodeList fstname = element1.getChildNodes();
        	  //points = (fstname.item(0)).getNodeValue();
        	  map.put("amount", (fstname.item(0)).getNodeValue());        	  
          }
          map.put("phone", "09597770247");
          mylist.add(map);
	        }
	    }
	    catch (Exception e) {
	        e.printStackTrace();
	    } 
	  }

}