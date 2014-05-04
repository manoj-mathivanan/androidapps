package com.soup.mall.scout;

import java.util.Random;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.support.v4.app.NavUtils;

public class offers extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    	

        setContentView(R.layout.offers);
        int i1,i2,i3,i4,i5,i6,i7,i8,i,prevn,n=0;
	     final ImageView img01 = (ImageView)findViewById(R.id.ImageView01);
	        final ImageView img02 = (ImageView)findViewById(R.id.ImageView02);
	        final ImageView img03 = (ImageView)findViewById(R.id.ImageView03);
	        final ImageView img04 = (ImageView)findViewById(R.id.ImageView04);
	        final ImageView img05 = (ImageView)findViewById(R.id.ImageView05);
	        final ImageView img06 = (ImageView)findViewById(R.id.ImageView06);
	        final ImageView img07 = (ImageView)findViewById(R.id.ImageView07);
	        final ImageView img08 = (ImageView)findViewById(R.id.ImageView08);
    	class MyCount extends CountDownTimer{
    		int i1,i2,i3,i4,i5,i6,i7,i8,i,prevn,n=0;
   	     final ImageView img01 = (ImageView)findViewById(R.id.ImageView01);
   	        final ImageView img02 = (ImageView)findViewById(R.id.ImageView02);
   	        final ImageView img03 = (ImageView)findViewById(R.id.ImageView03);
   	        final ImageView img04 = (ImageView)findViewById(R.id.ImageView04);
   	        final ImageView img05 = (ImageView)findViewById(R.id.ImageView05);
   	        final ImageView img06 = (ImageView)findViewById(R.id.ImageView06);
   	        final ImageView img07 = (ImageView)findViewById(R.id.ImageView07);
   	        final ImageView img08 = (ImageView)findViewById(R.id.ImageView08);
    		public MyCount(long millisInFuture, long countDownInterval) {
    		super(millisInFuture, countDownInterval);
    		
    		}

    		@Override
    		public void onFinish() {

    			
    		}
    		
    		@Override
    		public void onTick(long millisUntilFinished) {
    			try{
    			Random rand = new Random();

    			
    			
    			if(i%2 == 0)
    			{
    				n = rand.nextInt(8) + 1;
        			
        			Log.d("test","n= "+n);
    			}
    			i++;
    			switch (n) {
                case 1:  
                	i1++;
                	if(i1%2 == 0)
                	{
                		img01.setImageResource(R.drawable.logo_lee);
                		img01.startAnimation(new AnimationUtils().loadAnimation(getApplicationContext(), R.anim.fade));  		
                	}
                	else
                	{
                		img01.setImageResource(R.drawable.l1);
                		img01.startAnimation(new AnimationUtils().loadAnimation(getApplicationContext(), R.anim.push_left_in));
                	}
                		break;
                case 2:  
                	i2++;
                	if(i2%2 == 0)
                	{
                		img02.setImageResource(R.drawable.logo_manu);
                		img02.startAnimation(new AnimationUtils().loadAnimation(getApplicationContext(), R.anim.push_up_in));  
                	}
                	else
                	{
                		img02.setImageResource(R.drawable.l2);
                		img02.startAnimation(new AnimationUtils().loadAnimation(getApplicationContext(), R.anim.fade));  
                	}
                break;
                case 3:  
                	i3++;
                	if(i3%2 == 0)
                	{
                		img03.setImageResource(R.drawable.logo_base);
                		img03.startAnimation(new AnimationUtils().loadAnimation(getApplicationContext(), R.anim.slide_right));  
                	}
                	else
                	{
                		img03.setImageResource(R.drawable.l3);
                		img03.startAnimation(new AnimationUtils().loadAnimation(getApplicationContext(), R.anim.fade));  
                	}
                break;
                case 4:  
                	i4++;
                	if(i4%2 == 0)
                	{
                		img04.setImageResource(R.drawable.samsungs_2);
                		img04.startAnimation(new AnimationUtils().loadAnimation(getApplicationContext(), R.anim.push_left_in));  
                	}
                	else
                	{
                		img04.setImageResource(R.drawable.l4);
                		img04.startAnimation(new AnimationUtils().loadAnimation(getApplicationContext(), R.anim.fade));  
                	}
                break;
                case 5:  
                	i5++;
                	if(i5%2 == 0)
                	{
                		img05.setImageResource(R.drawable.logo_woodlands);
                		img05.startAnimation(new AnimationUtils().loadAnimation(getApplicationContext(), R.anim.push_left_in));  
                	}
                	else
                	{
                		img05.setImageResource(R.drawable.l5);
                		img05.startAnimation(new AnimationUtils().loadAnimation(getApplicationContext(), R.anim.fade));  
                	}
                break;
                case 6:  
                	i6++;
                	if(i6%2 == 0)
                	{
                		img06.setImageResource(R.drawable.wrangler);
                		img06.startAnimation(new AnimationUtils().loadAnimation(getApplicationContext(), R.anim.push_left_in));
                	}
                	else
                	{
                		img06.setImageResource(R.drawable.l6);
                		img06.startAnimation(new AnimationUtils().loadAnimation(getApplicationContext(), R.anim.fade));  
                	}
                break;
                case 7:  
                	i7++;
                	if(i7%2 == 0)
                	{
                		img07.setImageResource(R.drawable.pepejeans);
                		img07.startAnimation(new AnimationUtils().loadAnimation(getApplicationContext(), R.anim.push_left_in));  
                	}
                	else
                	{
                		img07.setImageResource(R.drawable.l7);
                		img07.startAnimation(new AnimationUtils().loadAnimation(getApplicationContext(), R.anim.fade));  
                	}
                break;
                case 8: 
                	i8++;
                	if(i8%2 == 0)
                	{
                		img08.setImageResource(R.drawable.logo_tissot);
                		img08.startAnimation(new AnimationUtils().loadAnimation(getApplicationContext(), R.anim.push_left_in));  
                	}
                	else
                	{
                		img08.setImageResource(R.drawable.l8);
                		img08.startAnimation(new AnimationUtils().loadAnimation(getApplicationContext(), R.anim.fade));  
                	}
                break;
    			}
    			}
    			catch(Exception e){
    				e.printStackTrace();
    			}
    		}
    		
    		
    	}
    	
    	MyCount counter = new MyCount(400000,2000);
    	counter.start();
        
		img01.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				helper.shopid=1;
				Intent goToNextActivity = new Intent(offers.this, offer_details.class);
				startActivity(goToNextActivity);			
			}
		});
		img02.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				helper.shopid=2;
				Intent goToNextActivity = new Intent(offers.this, offer_details.class);
				startActivity(goToNextActivity);			
			}
		});
		img03.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				helper.shopid=3;
				Intent goToNextActivity = new Intent(offers.this, offer_details.class);
				startActivity(goToNextActivity);			
			}
		});
		img04.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				helper.shopid=4;
				Intent goToNextActivity = new Intent(offers.this, offer_details.class);
				startActivity(goToNextActivity);			
			}
		});
		img05.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				helper.shopid=5;
				Intent goToNextActivity = new Intent(offers.this, offer_details.class);
				startActivity(goToNextActivity);			
			}
		});
		img06.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				helper.shopid=6;
				Intent goToNextActivity = new Intent(offers.this, offer_details.class);
				startActivity(goToNextActivity);			
			}
		});
		img07.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				helper.shopid=7;
				Intent goToNextActivity = new Intent(offers.this, offer_details.class);
				startActivity(goToNextActivity);			
			}
		});
		img08.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				helper.shopid=8;
				Intent goToNextActivity = new Intent(offers.this, offer_details.class);
				startActivity(goToNextActivity);			
			}
		});
        
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    
}
