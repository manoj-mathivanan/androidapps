package com.soup.mall.scout;

import java.util.Calendar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class park extends Activity {
	Calendar c = Calendar.getInstance();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.park);

		class MyTimerTask extends CountDownTimer {
			public MyTimerTask(long millisInFuture, long countDownInterval) {
				super(millisInFuture, countDownInterval);
				// TODO Auto-generated constructor stub
			}

			final TextView textView1 = (TextView) findViewById(R.id.ttextView1);
			int secondsElapsed = 0;
			int minutesElapsed = 0;
			int hoursElapsed = 0;

			@Override
			public void onFinish() {
				// TODO Auto-generated method stub

			}

			@Override
			public void onTick(long millisUntilFinished) {
				secondsElapsed += 1;
				if (secondsElapsed == 60) {
					minutesElapsed += 1;
					secondsElapsed = 0;
				}
				if (minutesElapsed == 60) {
					hoursElapsed += 1;
					minutesElapsed = 0;
				}
				
				if ((secondsElapsed - 45) % 60 == 0) {

					Toast.makeText(
							getBaseContext(),
							"Almost " + (secondsElapsed + 1)
									+ " minutes at the mall.", Toast.LENGTH_LONG)
							.show();
					// Get instance of Vibrator from current Context
					Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

					v.vibrate(3000);

					Uri notification = RingtoneManager
							.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
					Ringtone r = RingtoneManager.getRingtone(
							getApplicationContext(), notification);
					r.play();
				}
				
				TextView textView1 = (TextView) findViewById(R.id.ttextView1);
				String h = ((hoursElapsed < 10) ? "0" : "")
						+ (hoursElapsed + "");
				String m = ((minutesElapsed < 10) ? "0" : "")
						+ (minutesElapsed + "");
				String s = ((secondsElapsed < 10) ? "0" : "")
						+ (secondsElapsed + "");
				textView1.setText(h + ":" + m + ":" + s);
				Log.i("ShopScoutTimer", secondsElapsed + "");
			}
		}
MyTimerTask counter2 = null;
			Button alloffers = (Button) findViewById(R.id.bbutton1);

		alloffers.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(getBaseContext(), offers.class);
				startActivity(intent);
			}
		});

		Button myoffers = (Button) findViewById(R.id.bbutton2);

		myoffers.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(getBaseContext(), myoffers.class);
				startActivity(intent);
			}
		});

		Button mallventure = (Button) findViewById(R.id.bbutton3);

		mallventure.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(getBaseContext(), play.class);
				startActivity(intent);
			}
		});

		Button catalog = (Button) findViewById(R.id.bbutton4);

		catalog.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(getBaseContext(), catalog.class);
				startActivity(intent);
			}
		});
		
		

		final ImageButton parkStatus = (ImageButton) findViewById(R.id.iimageButton1);
		parkStatus.setOnClickListener(new OnClickListener() {

			MyTimerTask counter = null;
			
			boolean firstImageSeen = true;
			
			@Override
			public void onClick(View v) {

				TextView txtTimer = (TextView) findViewById(R.id.ttextView1);
				if (firstImageSeen) {
					parkStatus.setImageResource(R.drawable.logo_car_parked);
					counter = new MyTimerTask(Long.MAX_VALUE, 1000);
					counter.start();
				} else {
					parkStatus.setImageResource(R.drawable.logo_car_arrival);
					txtTimer.setText("Parked your car? Click on image");
					counter.cancel();
				}
				firstImageSeen = !firstImageSeen;
			}


		}		);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		return true;
	}

	public void showTime(int hoursElapsed, int minutesElapsed,
			int secondsElapsed) {

}

public void onBackPressed()
{
	System.exit(1);
}

}
