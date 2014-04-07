package com.example.worldnews;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

public class newsGrabber extends AsyncTask<String, Integer, String>{
	
	private String loc,category,lang,query;
	private String urlP1;
	public ArrayList<newsDataObject> newsList;
	private MainActivity ma;
	private ProgressDialog progDialog;

	public newsGrabber(MainActivity ma)
	{
		this.ma=ma;
	}
	
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		loc=null;
		progDialog = ProgressDialog.show(ma,"Loading","Please Wait..");
	}
	
	@Override
	protected String doInBackground(String... params){
		// TODO Auto-generated method stub
		loc=params[0];
		category=params[1];
		if(loc=="in")
		lang=params[2];
		else
		lang="en";
		query=params[3];
		HttpClient client ;
		try{
			Log.d("!!!", "MAking HttpClient..");
			client= new DefaultHttpClient();
			
		    Log.d("!!!!","Client made Sucessfully !,Now Setting the Request");
			HttpGet request = new HttpGet();
			if(query==null)
			{
			urlP1="https://news.google.com/news/feeds?pz=1&cf=all&ned="+loc+"&hl="+lang+"&topic="+category+"&output=rss";
			Log.d("Sent url !","https://news.google.com/news/feeds?pz=1&cf=all&ned="+loc+"&hl="+lang+"&topic="+category+"&output=rss");
			}
			else
			{
				urlP1="https://news.google.com/news/feeds?pz=1&cf=all&q="+query+"&ned="+loc+"&hl="+lang+"&output=rss";
				Log.d("Sent url !","https://news.google.com/news/feeds?pz=1&cf=all&q="+query+"&ned="+loc+"&hl="+lang+"&output=rss");
			}
			request.setURI(new URI(urlP1));
			
			Log.d("!!!!!","Request Set and Trying to get a response");
			HttpResponse response = client.execute(request);
			
			Log.d("!!!!!!","Got Response !");
			
			if(response.getStatusLine().getStatusCode()!= 200)
			{
				this.ma.displayAlert("Unable To Connect to Server ! Check Internet Settings ");
				return null;
			}
			HttpEntity e = response.getEntity();
			String gotxmlData = EntityUtils.toString(e);
			Log.d("!!!!!!","Response Code Positive.... data got = "+gotxmlData);
			return gotxmlData;
			
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			//this.ma.displayAlert("Unable To Connect to Server ! Check Internet Settings");
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//this.ma.displayAlert("Unable To Connect to Server !");
			e.printStackTrace();
			return null;
		} catch (Exception e) {
			// TODO Auto-generated catch block
		   //this.ma.displayAlert("Error Occured ! Please Restart The App");
			e.printStackTrace();
		}
		
		return null;
		
	}
	

	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		//Log.d("onPOstExecute","Result = "+result);
		progDialog.dismiss();
		if(result != null)
		{
			Log.d("Starting the Extractor","!!!!!!");
			this.newsList=new newsExtractor(result).extractNews();
			Log.d("Got the News LIst From Extractor...!!","!!!!!!!!!!!!!!!!!!!!!");
			ma.DisplayNews();
		}else
		{
			return;
		}
	}
	
	public ArrayList<newsDataObject> getNewsList()
	{
		Log.d("REturning News List From Grabber","!!!!!!!!!!!!!!!!!!!!!");
		return newsList;
	}


}
