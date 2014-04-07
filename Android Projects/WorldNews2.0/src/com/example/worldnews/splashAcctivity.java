package com.example.worldnews;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class splashAcctivity extends Activity {
	
	Thread timer;
	Intent i ;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splashscreen);
		i = new Intent(this, MainActivity.class);
		timer = new Thread(){
			public void run()
			{
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					startActivity(i);
				}
			}
		};
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		timer.start();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		this.finish();
	}

}
