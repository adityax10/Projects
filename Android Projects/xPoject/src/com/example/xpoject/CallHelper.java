package com.example.xpoject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

/**
 * Helper class to detect incoming and outgoing calls.
 * 
 */
public class CallHelper {

	/**
	 * Listener to detect incoming calls.
	 */
	
	
	
	private class CallStateListener extends PhoneStateListener {

		String number;
		
		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			switch (state) {
			case TelephonyManager.CALL_STATE_RINGING:
				// called when someone is ringing to this phone
				number=incomingNumber;
				Toast.makeText(ctx, "Incoming: " + incomingNumber,
						Toast.LENGTH_SHORT).show();
				Log.d("Detected Incoming Call", "!!!!!!!!!!!");

				break;
			case TelephonyManager.CALL_STATE_OFFHOOK:
				Log.i("State changed", "OFFHOOK");

				
					//start the activity
					long tim = System.currentTimeMillis();
					while(System.currentTimeMillis()-tim <=1000)
					{
						//wait tym
					}
					MainActivity.callActive=true;
					
					Intent  il = new Intent(ctx,MainActivity.class);
					il.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					il.putExtra("NUMBER",number);
					il.putExtra("ADDRESS",MainActivity.address);
	                ctx.startActivity(il);
	                //
				break;
			case TelephonyManager.CALL_STATE_IDLE:
				Log.i("State Changed", "IDLE");
				MainActivity.callActive=false;
				break;
			}
		}
	}

	/**
	 * Broadcast receiver to detect the outgoing calls.
	 */
	public class OutgoingReceiver extends BroadcastReceiver {
		public OutgoingReceiver() {
		}

		@Override
		public void onReceive(Context context, Intent intent) {
			String number = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);

			Toast.makeText(ctx, "Outgoing: " + number, Toast.LENGTH_LONG)
					.show();
			Log.d("Detected Outgoing Call", "!!!!!!!!!!!");
			
		}

	}

	private Context ctx;
	private TelephonyManager tm;
	private CallStateListener callStateListener;

	private OutgoingReceiver outgoingReceiver;

	public CallHelper(Context ctx) {
		this.ctx = ctx;

		callStateListener = new CallStateListener();
		outgoingReceiver = new OutgoingReceiver();
	}

	/**
	 * Start calls detection.
	 */
	public void start() {
		tm = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
		tm.listen(callStateListener, PhoneStateListener.LISTEN_CALL_STATE);

		IntentFilter intentFilter = new IntentFilter(
				Intent.ACTION_NEW_OUTGOING_CALL);
		ctx.registerReceiver(outgoingReceiver, intentFilter);
	}

	/**
	 * Stop calls detection.
	 */
	public void stop() {
		tm.listen(callStateListener, PhoneStateListener.LISTEN_NONE);
		ctx.unregisterReceiver(outgoingReceiver);
	}

}
