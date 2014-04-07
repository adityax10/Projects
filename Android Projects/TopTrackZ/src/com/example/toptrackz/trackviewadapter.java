package com.example.toptrackz;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.zip.Inflater;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.sax.StartElementListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebBackForwardList;
import android.webkit.WebView;
import android.webkit.WebView.FindListener;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class trackviewadapter extends BaseAdapter implements
		android.view.View.OnClickListener {

	MainActivity main_activity;
	ArrayList<track_data> trackDataArray;
	myTracksDisplayFormat mtds;
	LayoutInflater inflator;
	// to make an image cache.
	HashMap<String, Drawable> imagecache;
	//ImageFetcherClass ImageFetcherObject;

	public trackviewadapter(MainActivity a, ArrayList<track_data> tracklist,
			LayoutInflater inflator2) {
		// TODO Auto-generated constructor stub
		Log.d("Initialising the trackviewadapter", "!!!!!!!!!!!!!!!!!! ");
		main_activity = a;
		trackDataArray = tracklist;
		inflator = inflator2;
		imagecache = new HashMap<String, Drawable>();
		//ImageFetcherObject = new ImageFetcherClass();
	}

	public class myTracksDisplayFormat {
		TextView artist_name, song_name, duration;
		ImageView img;
		Button art_button;
		String art_url=null;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		Log.d("Getting view", "!!!!!!!!!!!!!!!!!! ");

		mtds = new myTracksDisplayFormat();

		if (convertView == null) {
			// /since its our custom view.. we need to manually inflate it !!
			convertView = inflator.inflate(R.layout.tracksrow, null);
			// setting the references
			mtds.song_name = (TextView) convertView
					.findViewById(R.id.song_name);
			mtds.duration = (TextView) convertView.findViewById(R.id.duration);
			mtds.artist_name = (TextView) convertView
					.findViewById(R.id.artist_name);
			mtds.art_button = (Button) convertView.findViewById(R.id.button1);
			mtds.art_button.setOnClickListener(this);
			//mtds.art_button.setTag(position);
			mtds.img = (ImageView) convertView.findViewById(R.id.image);

			// setting tag !
			mtds.art_button.setTag(mtds);
			convertView.setTag(mtds);
		} else {
			mtds = (myTracksDisplayFormat) convertView.getTag();
		}

		// complete this....................................!!
		if (trackDataArray.get(position).getImageurl() != null) {
			//mtds.img.setImageBitmap(BitmapFactory.decodeResource(null,R.drawable.fillerimg2));
			mtds.img.setImageDrawable(this.ImageCachechecker((trackDataArray
					.get(position).getImageurl())));
		}
		
		// Setting Values
		mtds.song_name.setText(trackDataArray.get(position).getSong());
		int t = Integer.parseInt(trackDataArray.get(position).getDuration());
		int m = t / 60;
		int s = t % 60;
		mtds.duration.setText(String.format("%d:%2d", m, s));
		mtds.artist_name.setText(trackDataArray.get(position).getArtist());
		mtds.art_url=trackDataArray.get(position).getSongurl();

		return convertView;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		Log.d("Getting count", "!!!!!!!!!!!!!!!!!! ");
		return trackDataArray.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		Log.d("Getting item", " !!!!!!!!!!!!!!!!!! ");
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		Log.d("Getting item postion", "!!!!!!!!!!!!!!!!!! ");
		return position;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
       
	   Intent i = new Intent(Intent.ACTION_VIEW,Uri.parse(mtds.art_url));
	   this.main_activity.startActivity(i);
	}
//////////////////////////////////////////////////////////////////////////////////////////////////
	// checker for image in cache.............
	public Drawable ImageCachechecker(String url) {
		Log.d("Checking for image in cache..", "!!!!!!!!!!!!!!!!!! ");
		if (imagecache.containsKey(url)) {
			Log.d("Image Found IN Cache", "!!!!!!!!!!!!!!!!!! ");
			return imagecache.get(url);
		} else {
			Log.d("IMage not in cache...", "Fetching From the INternet.....");
			new ImageFetcherClass().execute(url);
		}
			return imagecache.get(url);
		 
	}

	public class ImageFetcherClass extends AsyncTask<String, Integer, Drawable> {

		// Importing Photos IF not present........
		public synchronized Drawable imageFetcher(String path) {
			Log.d("Fetching from internet in progress..",
					"!!!!!!!!!!!!!!!!!!!!!!");
			try {
				URL url_image = new URL(path);
				InputStream instrm = url_image.openStream();

				Drawable image = Drawable.createFromStream(instrm, null);
				// saving the image......
				imagecache.put(path, image);
				return image;

			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block

				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return (new BitmapDrawable(BitmapFactory.decodeResource(null,R.drawable.fillerimg2)));
		}

		@Override
		protected Drawable doInBackground(String... params) {
			// TODO Auto-generated method stub
			return imageFetcher(params[0]);
		}

		@Override
		protected void onPostExecute(Drawable result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			Log.d("IMage Fetched !!",
					"!!!!!!!!!!!!!!!!!!!!!!");
		}

	}

}
