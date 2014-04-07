package com.example.toptrackz;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ProgressBar;

public class LastfmAsync extends AsyncTask<String, Integer, String> {

	Context lfa_context;
	ProgressDialog prog_dailog;
	ArrayList<track_data> trackslist;
	MainActivity main_activity;
	int n = 0;

	public LastfmAsync(Context c, MainActivity mainactivity) {
		super();
		lfa_context = c;
		main_activity = mainactivity;
		trackslist = new ArrayList<track_data>();
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		prog_dailog = ProgressDialog.show(lfa_context, "Fetching Charts...",
				"Loading...");
	}

	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
		return (new LastfmHelper(main_activity).getTracks(params[0]));
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		// TODO Auto-generated method stub
		super.onProgressUpdate(values);
	}

	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		prog_dailog.dismiss();
		if (result.length() == 0) {
			Log.d("Got Nothing..", "In onPostExecute()");
			return;
		}
		try {
			JSONObject main_json = new JSONObject(result);
			JSONObject track_obj = main_json.getJSONObject("toptracks");
			JSONArray tracks = track_obj.getJSONArray("track");

			for (int i = 0; i < tracks.length(); i++) {
				JSONObject track = tracks.getJSONObject(i);

				String artist_name = track.getJSONObject("artist").getString(
						"name");
				String artist_url = track.getJSONObject("artist").getString(
						"url");

				String song_name = track.getString("name");
				String listeners = track.getString("listeners");
				String duration = track.getString("duration");
				String link_url = track.getString("url");
				String image_url = null;
				try {
					JSONArray intrack = track.getJSONArray("image");
					image_url = intrack.getJSONObject(1).getString("#text");
				} catch (Exception e) {
					Log.d("No Image Found ! ", "No IMAGE !!!!!");
				}

				trackslist.add(new track_data(artist_name, artist_url,
						song_name, listeners, duration, link_url, image_url));
				Log.d("Adding Object !" + (++n), artist_name + artist_url
						+ song_name + listeners + duration + link_url
						+ image_url);
			}
			// giving the results back to Main ACtivity...
			this.main_activity.setTrackArrayList(trackslist);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.d("JsonException !!!", e.toString());
		}

	}

}
