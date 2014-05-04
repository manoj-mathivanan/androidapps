package com.soup.mall.scout;

import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

public class EndCallListener extends PhoneStateListener {
	private boolean callStartedAndEnded;
	public boolean callEnded;

	public EndCallListener() {
		this.callStartedAndEnded = false;
		this.callEnded = false;
	}

	@Override
	public void onCallStateChanged(int state, String incomingNumber) {
		if (TelephonyManager.CALL_STATE_RINGING == state) {
			Log.i("ShopScoutBuddyActivity", "RINGING, number: "
					+ incomingNumber);
		}
		if (TelephonyManager.CALL_STATE_OFFHOOK == state) {
			// wait for phone to go offhook (probably set a boolean flag) so you
			// know your app initiated the call.
			Log.i("ShopScoutBuddyActivity", "OFFHOOK");
			this.callStartedAndEnded = true;
		}
		if (TelephonyManager.CALL_STATE_IDLE == state) {
			// when this state occurs, and your flag is set, restart your app
			Log.i("ShopScoutBuddyActivity", "IDLE");
			if (this.callStartedAndEnded == true) {
				// restart app
				/*Intent i = getBaseContext().getPackageManager()
						.getLaunchIntentForPackage(
								getBaseContext().getPackageName());
				i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(i);*/
			}
			// Context context = Context.this;
			this.callStartedAndEnded = false;

		}
	}
}