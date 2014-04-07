package com.example.toptrackz;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class splashActivity extends Activity {

	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		timer.start();
	}

	Thread timer;

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		this.finish();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_layout);

		timer = new Thread() {
			public void run() {
				try {
					timer.sleep(2000);
				} catch (Exception e) {
					//
				} finally {
					Intent i = new Intent(splashActivity.this,
							MainActivity.class);
					startActivity(i);
				}
			}
		};

	}
	
	
}
