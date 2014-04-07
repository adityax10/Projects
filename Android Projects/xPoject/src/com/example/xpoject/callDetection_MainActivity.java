package com.example.xpoject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Main activity, with button to toggle phone calls detection on and off.
 *
 */
public class callDetection_MainActivity extends Activity {

	private boolean detectEnabled;
	
	private TextView textViewDetectState;
	private Button buttonToggleDetect;
	private Button buttonExit;
	static String address;
	Button send;
	EditText ip_address;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.call_detection_service);
        
        textViewDetectState = (TextView) findViewById(R.id.textViewDetectState);
    	ip_address=(EditText)findViewById(R.id.ip);
		
        
        buttonToggleDetect = (Button) findViewById(R.id.buttonDetectToggle);
        buttonToggleDetect.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setDetectEnabled(!detectEnabled,ip_address.getText().toString());
			}
		});
        
        buttonExit = (Button) findViewById(R.id.buttonExit);
        buttonExit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setDetectEnabled(false,ip_address.getText().toString());
				callDetection_MainActivity.this.finish();
			}
		});
        send = (Button)findViewById(R.id.button1);
        send.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			
			}
        });
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    private void setDetectEnabled(boolean enable,String address) {
    	detectEnabled = enable;
    	
        Intent intent = new Intent(this, CallDetectService.class);
    	if (enable) {
    		 // start detect service 
    		MainActivity.address = address;
            startService(intent);
           
            buttonToggleDetect.setText("Turn off");
    		textViewDetectState.setText("Detecting");
    	}
    	else {
    		// stop detect service
    		stopService(intent);
    		
    		buttonToggleDetect.setText("Turn on");
    		textViewDetectState.setText("Not detecting");
    	}
    }

}
