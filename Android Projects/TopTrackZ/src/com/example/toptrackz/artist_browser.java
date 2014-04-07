package com.example.toptrackz;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

public class artist_browser extends Activity {

	WebView page;
	String url;
	
	public artist_browser(String url)
	{
		this.url = url;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.artist_browser_layout);
		page = (WebView) findViewById(R.id.webView1);
		page.loadUrl(url);
	}

}
