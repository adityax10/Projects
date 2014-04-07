package com.example.xpoject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

public class wavPost extends AsyncTask<String, Integer, String> {

	String voiceText;
	String mode;

	// CONSTRUCTOR
	public wavPost(String x) {
		mode = x;
	}

	// -------------------------------------------- AFTER EXECUTING SAVING THE
	// RESPONSE
	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		if (result != null) {
			//Checking Request Mode and Saving Response Accordingly
			if(mode=="U")
			MainActivity.voiceTextResponses_UPLINK.add(result);
			else if(mode=="D")
				MainActivity.voiceTextResponses_DOWNLINK.add(result);	
		}
		// ------------------- UPDATE VIEW MODULE HERE <<<<<<<<<<<<<<<<<<<<
		
		return;
	}

	// -------------------------------------------- BEFORE EXECTUTING
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		voiceText = null;
	}

	// -------------------------------------------- DURING UPDATING
	@Override
	protected void onProgressUpdate(Integer... values) {
		// TODO Auto-generated method stub
		super.onProgressUpdate(values);
	}

	// ------------------------------------------- IN BACKGROUND
	@Override
	protected String doInBackground(String... params) {

		HttpURLConnection conn = null;
		DataOutputStream dos = null;
		DataInputStream inStream = null;
		// String existingFileName =
		// Environment.getExternalStorageDirectory().getAbsolutePath() +
		// "/1.png";
		String existingFileName = params[0];
		String address=params[1];
		String lineEnd = "\r\n";
		String twoHyphens = "--";
		String boundary = "*****";
		int bytesRead, bytesAvailable, bufferSize;
		byte[] buffer;
		int maxBufferSize = 1 * 1024 * 1024;
		String responseFromServer = "";
		// ----------------------------------------- SERVER URL TO BE ENTERED
		// !!!
		String urlString = "http://"+address+"/script/upload.php";

		try {
			Log.d("Finding File...", "1");
			// ------------------ CLIENT REQUEST
			FileInputStream fileInputStream = new FileInputStream(new File(
					existingFileName));
			Log.d("File. Found.. :"+existingFileName, "2");
			// open a URL connection to the Servlet
			Log.d("Making Connnection", "3");
			URL url = new URL(urlString);
			// Open a HTTP connection to the URL

			conn = (HttpURLConnection) url.openConnection();

			// Allow Inputs
			conn.setDoInput(true);

			// Allow Outputs
			conn.setDoOutput(true);

			// Don't use a cached copy.
			conn.setUseCaches(false);

			// Use a post method.
			Log.d("Setting POST property", "4");
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("Content-Type",
					"multipart/form-data;boundary=" + boundary);

			Log.d("Creating Output Stream ", "5");
			dos = new DataOutputStream(conn.getOutputStream());
			Log.d("Writing to outputStream", "5");
			dos.writeBytes(twoHyphens + boundary + lineEnd);
			dos.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\""
					+ existingFileName + "\"" + lineEnd);
			dos.writeBytes(lineEnd);

			// create a buffer of maximum size
			Log.d("Creating A Buffer", "6");
			bytesAvailable = fileInputStream.available();
			bufferSize = Math.min(bytesAvailable, maxBufferSize);
			buffer = new byte[bufferSize];

			// read file and write it into form...
			Log.d("Reading File into buffer", "7");
			bytesRead = fileInputStream.read(buffer, 0, bufferSize);

			Log.d("Writing buffer to outputStream", "8");
			while (bytesRead > 0) {
				Log.d("Writing ....", "8");
				dos.write(buffer, 0, bufferSize);
				bytesAvailable = fileInputStream.available();
				bufferSize = Math.min(bytesAvailable, maxBufferSize);
				bytesRead = fileInputStream.read(buffer, 0, bufferSize);

			}
			Log.d("Finishing Writing to outputStream ", "9");

			// send multipart form data necesssary after file data...
			dos.writeBytes(lineEnd);
			dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
			// close streams
			Log.e("Debug", "File is written");
			fileInputStream.close();
			dos.flush();
			dos.close();

		} catch (MalformedURLException ex) {
			Log.e("Debug", "error: " + ex.getMessage(), ex);
		} catch (IOException ioe) {
			Log.e("Debug", "error: " + ioe.getMessage(), ioe);
		}

		// ------------------ read the SERVER RESPONSE
		try {

			inStream = new DataInputStream(conn.getInputStream());
			String str;

			while ((str = inStream.readLine()) != null) {

				Log.e("Debug", "Server Response " + str);

			}
			
			inStream.close();

		} catch (IOException ioex) {
			Log.e("Debug", "error: " + ioex.getMessage(), ioex);
		}

		// Update VoiceText HERE <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
		//

		return voiceText;
	}

}
