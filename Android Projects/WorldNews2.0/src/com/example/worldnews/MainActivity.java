package com.example.worldnews;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.opengl.Visibility;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SlidingDrawer;
import android.widget.SlidingDrawer.OnDrawerOpenListener;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener, OnItemClickListener {

	Button bUpdate;
	Spinner lRegions;
	newsGrabber ng;
	ListView newsListSpace,categoryList;
	String urlpaths[]={"es_ar","au","nl_be","fr_be","en_bw","BR_br","ca","es_cl","es_co","es_cu","de","en_et","fr","en_gh","in","en_ie","en_il","it","en_ke","en_my","en_mx","en_na","nz","en_ng","en_pk","es_pe","en_ph","pl_pl","pt-PT_pt","de_ch","en_sg","en_za","uk","us","es_ve","en_zw"};
	SlidingDrawer sd;
	String categorySection="h",lang="en";
	String categoryValuesString[],langValues[];
	ImageView searchImg;
	String query,loc ;
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		bUpdate = (Button) findViewById(R.id.buttonUpdate);
		lRegions = (Spinner ) findViewById(R.id.RegionSpinner);
		newsListSpace=(ListView)findViewById(R.id.newsListSpace);
		categoryList = (ListView)findViewById(R.id.category);
		sd= (SlidingDrawer)findViewById(R.id.drawer1);
		categoryValuesString = getResources().getStringArray(R.array.category_values);
		searchImg = (ImageView)  findViewById(R.id.search);
		langValues=getResources().getStringArray(R.array.lang_values);
		bUpdate.setOnClickListener(this);
		newsListSpace.setOnItemClickListener(this);
		searchImg.setOnClickListener(this);
		categoryList.setOnItemClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch(item.getItemId())
		{
		case R.id.action_settings:
			Intent i1 = new Intent(this,prefrences.class);
			startActivity(i1);
			break;
		case R.id.action_credits:
			Intent i2 = new Intent(this,credits.class);
			startActivity(i2);
			break;
		case R.id.action_exit:
			finish();
			break;
		case R.id.searchOption:
			Log.d("Starting Search Activity!!","!!!!!!!!!");
			Intent i  = new Intent(this, searchClass.class);
			startActivityForResult(i, 0);
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		// specify region here.. !!
		//this.displayAlert("Touch event !!!!!!!!!!!!");
		switch(arg0.getId())
		{
		case R.id.buttonUpdate:
		ng = new newsGrabber(this);
		Log.d("Selected item code = ","pos = "+lRegions.getSelectedItemPosition());
        setParametersAndAdjustTheValues();
		ng.execute(loc,categorySection,lang,null);
		//this.DisplayNews();
		break;
		// wont work.. :(
		case R.id.handle2:
			this.displayAlert("Touch event !!!!!");
			newsListSpace.setVisibility(View.INVISIBLE);
			break;
		case R.id.search:
			//
			break;
		}
	}
	
	public void setParametersAndAdjustTheValues()
	{
		int pos=0;
		loc=null;
		SharedPreferences spref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		Boolean checkDefaultRegionStatus = spref.getBoolean("DefaultCheckStatus", false);
		
		if(checkDefaultRegionStatus)
		{
		loc= spref.getString("defaultRegion", urlpaths[lRegions.getSelectedItemPosition()]);
		Log.d("Got Loc From Prefrence MAnager"," loc "+loc);
		for(pos = 0;pos < urlpaths.length;pos++)
		{
			if(urlpaths[pos].equals(loc))
				break;
		}
		lRegions.setSelection(pos);
		}
		else
		{
		loc = urlpaths[lRegions.getSelectedItemPosition()];
		}
		if(loc=="in")
		{
			lang= spref.getString("defaultLang_indiaSupport","en");
		}
	}
	
	public void DisplayNews()
	{
		Log.d("Starting to Display News.!!!","!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		newsListSpace.setAdapter(new newsViewAdapter(this.getLayoutInflater(),ng));
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.newsListSpace:
			//this.displayAlert("Touch event !");
			int position = arg2;
			String url = ng.newsList.get(position).getnewsLink();
			Intent i = new Intent(Intent.ACTION_VIEW,Uri.parse(url));
			startActivity(i);
			break;

		case R.id.category:
			categorySection = categoryValuesString[arg2];
			sd.close();
			break;
		}
	}
	
	public void displayAlert(String msg)
	{
		Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		Log.d("Back in the MainActivity class","!!!!!!");
		if(resultCode == RESULT_OK&& requestCode==0)
		{
			Log.d("Result OK !","!!!!");
			Bundle  gotBasket = data.getExtras();
			query = gotBasket.getString("searchKey");
			Log.d("Query Got",query);
			setParametersAndAdjustTheValues();
			Log.d("Now grabbing..","!!!!!!!!");
			ng  = new newsGrabber(this);
			ng.execute(loc,categorySection,lang,query);
		}
	}

}
