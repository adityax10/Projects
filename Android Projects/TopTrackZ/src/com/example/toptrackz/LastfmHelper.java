package com.example.toptrackz;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.ByteArrayBuffer;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class LastfmHelper {
	
	final String last_fm_url_start = "http://ws.audioscrobbler.com/2.0/?method=geo.getmetrotrackchart&country=united+states&metro=";
	final String url_part = "&format=json&api_key=495635bdb0f5e1dd9f86c26e40c71829";
	int  RESPONSE_POSITIVE =200 ;
	String recieved_json_data;
	Context con ;
	
	public LastfmHelper(MainActivity m)
	{
		con = m;
	}
	
	
	public String getTracks(String metro)
	{
		Log.d("LastfmHelper","Attempting to get Data !");
		HttpClient hc = new DefaultHttpClient();
		
		HttpGet request = new HttpGet();
		try {
			String last_fm_url = last_fm_url_start.concat(metro).concat(url_part);
			request.setURI(new URI(last_fm_url));
			Log.d("LastfmHelper","Sending request and Waiting for response!");
			HttpResponse response = hc.execute(request);
			Log.d("LastfmHelper","Got Reponse !");
			if(response.getStatusLine().getStatusCode()!=RESPONSE_POSITIVE)
			{
			        alertMsg();
			}
			else{
				Log.d("LastfmHelper","Response Code Positive !");
				HttpEntity e = response.getEntity();
				recieved_json_data = EntityUtils.toString(e);
				Log.d("LastfmHelper","Jason String Data  Received ! : "+recieved_json_data.toString());
				return recieved_json_data.toString();
			}
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			alertMsg();
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			alertMsg();
			e.printStackTrace();
		} catch (IOException e) {
			alertMsg();
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch(Exception e)
		{
			alertMsg();
			e.printStackTrace();
		}
		
		return null;
	}
	
	public void alertMsg()
	{
		Toast t = Toast.makeText(con,"Error while Connecting..!",Toast.LENGTH_LONG);
		t.show();
	}

}
