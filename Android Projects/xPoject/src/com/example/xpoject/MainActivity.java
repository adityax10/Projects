package com.example.xpoject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.Menu;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {
	// helpers
	static int recordnum = 0;
	boolean isRecording_up = false, isRecording_down = false;
	String number;
	public static Boolean callActive = false;
	static String address;
	ArrayList<String> processingQueue = new ArrayList<String>();

	// File Paths
	// String CURRENT_PATH_NAME_UP = "/mnt/sdcard/V"+recordnum_up+".amr";
	// String CURRENT_PATH_NAME_DOWN = "/mnt/sdcard/V"+recordnum_down+".amr";

	// ResponseStrings
	public static ArrayList<String> voiceTextResponses_UPLINK = new ArrayList<String>();
	public static ArrayList<String> voiceTextResponses_DOWNLINK = new ArrayList<String>();

	// FUNCTIONS

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Getting the Number
		Bundle x = this.getIntent().getExtras();
		if(x!=null)
		{
		number = x.getString("NUMBER");
		address = x.getString("ADDRESS");
		}

		displayToast("NUmber =" + number);

		//address = "169.254.3.150";
		displayToast("Address : " + address);
		new callRecorders("U").execute();
		// new callRecorders("D").execute(); :C :C :C :c :c:c

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	// ----------------------------------- TOAST DISPLAYER
	public void displayToast(String msg) {
		Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
	}

	/*
	 * // --------------------------------- STARTING RECORDING //
	 * __________________________________ FOR UPSTREAM public void
	 * startRecordingUp() { if (!isRecording_up) {
	 * 
	 * recorder_up = new MediaRecorder(); recordnum_up++;
	 * recorder_up.setAudioSource(MediaRecorder.AudioSource.VOICE_UPLINK);
	 * recorder_up.setOutputFormat(MediaRecorder.OutputFormat.RAW_AMR);
	 * recorder_up.setAudioEncodingBitRate(16);
	 * recorder_up.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
	 * recorder_up.setOutputFile(CURRENT_PATH_NAME_UP); isRecording_up = true;
	 * try { recorder_up.prepare(); } catch (Exception e) { // TODO
	 * Auto-generated catch block e.printStackTrace(); } recorder_up.start();
	 * displayToast("Starting Recording Up...."); Log.d("F1",
	 * "Recording Started !"); } else { displayToast("Already Recording...."); }
	 * } //_______________________________________ FOR DOWNSTREAM public void
	 * startRecordingDown() { if (!isRecording_down) {
	 * 
	 * recorder_down = new MediaRecorder(); recordnum_down++;
	 * recorder_down.setAudioSource(MediaRecorder.AudioSource.VOICE_DOWNLINK);
	 * recorder_down.setOutputFormat(MediaRecorder.OutputFormat.RAW_AMR);
	 * recorder_down.setAudioEncodingBitRate(16);
	 * recorder_down.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
	 * recorder_down.setOutputFile(CURRENT_PATH_NAME_DOWN); isRecording_down =
	 * true; try { recorder_down.prepare(); } catch (Exception e) { // TODO
	 * Auto-generated catch block e.printStackTrace(); } recorder_down.start();
	 * displayToast("Starting Recording...."); Log.d("F1",
	 * "Recording Started !"); } else { displayToast("Already Recording...."); }
	 * 
	 * }
	 * 
	 * 
	 * // ----------------------------------- STOPPING RECORDING //
	 * ____________________________________ FOR UPSTREAM public void
	 * stopRecordingUp() { if (isRecording_up) { recorder_up.stop();
	 * recorder_up.reset(); //recorder.release(); isRecording_up = false;
	 * displayToast("Stopping Recording Up..."); Log.d("F3", "Saving File"); }
	 * else { displayToast("No Present Recording Avaliable To Stop"); } }
	 * 
	 * // ____________________________________ FOR DOWNSTREAM public void
	 * stopRecordingDown() { if (isRecording_down) { recorder_down.stop();
	 * recorder_down.reset(); //recorder.release(); isRecording_down = false;
	 * displayToast("Stopping Recording Down..."); Log.d("F3", "Saving File"); }
	 * else { displayToast("No Present Recording Avaliable To Stop"); } }
	 */
	// ------------------------------------ POSTING THE RECORD

	// x=> U for upstream requests
	// x=> D for downstream requests
	public void postAndGet(String path, String x) {
		ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected()) {
			
			new wavPost(x).execute(path, address);
			
		} else {
			displayToast("No Network Connection Available.Please Check Your Internet Settings.");
		}
	}

	public class callRecorders extends AsyncTask<Void, Void, Void> {

		MediaRecorder audioRecorder;
		String Mode;

		public callRecorders(String mode) {
			Mode = mode;
			Log.d("Object Initialising..", "!!!!!!!!");
		}

		public void startIntelligentRecording() {

			Log.d("In startIntelligentMethod", "!!!!!!!!!");

			String filepath = Environment.getExternalStorageDirectory()
					.getPath();
			File file = new File(filepath, "AudioRecorder");
			if (!file.exists())
				file.mkdirs();
			String fn = null;

			Log.d("Initailising AudioRecorder", "!!!!!!!!!");
			
			audioRecorder = new MediaRecorder();
			
			while (MainActivity.callActive == true) {

				
				if (Mode == "U") {
					audioRecorder
							.setAudioSource(MediaRecorder.AudioSource.MIC);
				} else if (Mode == "D") {
					audioRecorder
							.setAudioSource(MediaRecorder.AudioSource.MIC);
				}

				audioRecorder
						.setOutputFormat(MediaRecorder.OutputFormat.AMR_WB);
				audioRecorder.setAudioEncodingBitRate(16);
				audioRecorder
						.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

				Log.d("Setting Path Directories", "!!!!!!!!!");
				if (Mode == "U") {
					fn = file.getAbsolutePath() + "//" + Mode
							+ MainActivity.recordnum + ".amr";
					MainActivity.recordnum++;
				} else if (Mode == "D") {
					fn = file.getAbsolutePath() + "//" + Mode
							+ MainActivity.recordnum + ".amr";
					MainActivity.recordnum++;
				}
				audioRecorder.setOutputFile(fn);

				Log.d("Preparing AudioRecorder", "!!!!!!!!!");
				try {
					audioRecorder.prepare();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				Log.d("Starting Recording", "!!!!!!!!!");
				
				audioRecorder.start();

				try{
				Thread.sleep(8000);
				}
				 catch(Exception  e)
				 {
					 e.printStackTrace();
				 }
;				
				Log.d("Waiting For Record to be over", "!!!!!!!!!");
				

				audioRecorder.stop();
				audioRecorder.reset();
				
				Log.d("Record MAde", fn);

				if (Mode == "U") {
					MainActivity.voiceTextResponses_UPLINK.add(fn);
					postAndGet(fn, "U");
				} else if (Mode == "D") {
					MainActivity.voiceTextResponses_DOWNLINK.add(fn);
					postAndGet(fn, "D");
				}
			}
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				Log.d("In Async Task !", "Starting REcording IN Async Task");

				startIntelligentRecording();
			} catch (Exception ex) {

				Log.d("In Recording Async Task Error", "Error !!!!!!!!");
				ex.printStackTrace();
			}
			return null;
		}

	}
}
