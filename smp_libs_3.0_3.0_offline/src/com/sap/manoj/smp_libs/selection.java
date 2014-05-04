package com.sap.manoj.smp_libs;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class selection extends Activity {
	public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
    setContentView(R.layout.selection);
//    if (helper.urlrewrite == false)
//    	{
//    	Toast.makeText(this, "no url rewrite", Toast.LENGTH_SHORT).show();
//    	}
    final Button button = (Button)findViewById(R.id.collection);
    button.setOnClickListener(new OnClickListener() {           
    	@Override 
        public void onClick(View v) { 
    		Intent goToNextActivity = new Intent(selection.this, listcollection.class);
			startActivity(goToNextActivity);
        }
    });
    final Button button2 = (Button)findViewById(R.id.image);
    button2.setOnClickListener(new OnClickListener() {           
    	@Override 
        public void onClick(View v) { 
    		Intent goToNextActivity = new Intent(selection.this, ImageUploadDownload.class);
			startActivity(goToNextActivity);
        }
    });
}
}
