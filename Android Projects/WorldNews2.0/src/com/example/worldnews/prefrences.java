package com.example.worldnews;

import com.example.worldnews.R;

import android.os.Bundle;
import android.preference.PreferenceActivity;


public class prefrences extends PreferenceActivity {

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.prefrencelayout);
	}
}
