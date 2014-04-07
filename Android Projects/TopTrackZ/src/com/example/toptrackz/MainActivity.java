package com.example.toptrackz;

import java.util.ArrayList;

import com.example.toptrackz.R.id;

import android.os.Bundle;
import android.app.Activity;
import android.app.ListActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends Activity implements OnItemSelectedListener,
		OnClickListener {

	Spinner s;
	Button b;
	String metro;
	ListView trackListView;
	String metroValues[] = { "denver", "detroit", "miami", "orlando",
			"phoenix", "sacramento", "seattle" };
	ArrayList<track_data> tracklist;
	LastfmAsync lfa;
	LayoutInflater inflator;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		metro = metroValues[0];
		s = (Spinner) findViewById(R.id.spinner1);
		b = (Button) findViewById(R.id.button1);
		// creating a cusstom inflaor to br used with the track view adapter
		inflator = LayoutInflater.from(this);
		// s.setOnItemSelectedListener(this);

		b.setOnClickListener(this);
		trackListView = (ListView) findViewById(R.id.listView1);
		trackListView.setOnItemSelectedListener(this);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		metro = metroValues[s.getSelectedItemPosition()];
		lfa = new LastfmAsync(this, this);
		lfa.execute(metro);
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View v, int x, long arg3) {
		// TODO Auto-generated method stub
		/*
		 * og.d("onItemSelecteedEvent => : ",v.getId()+"    pos : "+x); switch
		 * (v.getId()) { case R.id.spinner1: int pos =
		 * s.getSelectedItemPosition(); metro=metroValues[pos];
		 * Log.d("metro set as :",metro); break;
		 */
		// case R.id.listView1:

	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		metro = metroValues[0];
	}

	public void setTrackArrayList(ArrayList<track_data> elm) {
		tracklist = elm;
		setTheTrackViewContents();
	}

	// setting the list view for the list view
	public void setTheTrackViewContents() {
		Log.d("Track List SET !!", " Starting to display the list ! ");
		trackListView
				.setAdapter(new trackviewadapter(this, tracklist, inflator));
	}

}
