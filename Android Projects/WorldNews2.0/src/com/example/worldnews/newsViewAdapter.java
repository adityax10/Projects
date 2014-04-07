package com.example.worldnews;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.zip.Inflater;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class newsViewAdapter extends BaseAdapter {
	
	LayoutInflater inflater;
	newsElementObject news;
	ArrayList<newsDataObject> newsList;
    ImgChecker imgChecker;
    HashMap<String,Bitmap> ImageCache;
	
	public class newsElementObject
	{
		TextView newsHeading,newsDuration,newsSource,newsSnippet;
		String newsLink,imgLink;
		ImageView img ;
	}
	
	public newsViewAdapter(LayoutInflater inflater,newsGrabber ng)
	{
		this.inflater = inflater;
		newsList = ng.getNewsList();
		imgChecker = new ImgChecker(this);
		ImageCache = new HashMap<String, Bitmap>();
		Log.d("News Adapter Made....!!","!!!!!!!!!!!!!!!!!!!!!");
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		Log.d("Getting Total count", " !!!!!!!!!!!!!!!!!! ");
		return newsList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		Log.d("Getting item", " !!!!!!!!!!!!!!!!!! ");
		return arg0;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		Log.d("Getting item id", " !!!!!!!!!!!!!!!!!! ");
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		news = new newsElementObject();
		
		Log.d("News Adapter Getting View....!!","!!!!!!!!!!!!!!!!!!!!!");
		if(convertView == null)
		{
			Log.d("ConvertView Null..","!!Building!!");
			convertView = inflater.inflate(R.layout.newsview,null);
			
			news.newsHeading =(TextView) convertView.findViewById(R.id.newsHeading);
			news.newsDuration=(TextView)convertView.findViewById(R.id.newsDuration);
			news.newsSource=(TextView)convertView.findViewById(R.id.newsSource);
			news.img=(ImageView)convertView.findViewById(R.id.newsImage);
			news.newsSnippet=(TextView)convertView.findViewById(R.id.newsSnippet);
			
			convertView.setTag(news);
			Log.d("ConvertView Made Sucessfully","!!!!!!!!!!!!!!!!!!!!!");
		}
		else
		{
			Log.d("Convert view Not Null...","assigning news from the convertview Tag");
			news = (newsElementObject)convertView.getTag();
		}
		
		Log.d("Assigning VAlues to the Elements.","!!!!!!!");
		
		news.newsHeading.setText(newsList.get(position).getnewsHeading());
		news.newsSource.setText(newsList.get(position).getnewsSource());
		news.newsDuration.setText(newsList.get(position).getnewsDuration());
		news.newsSnippet.setText(newsList.get(position).getnewsSnippet());
		news.imgLink = newsList.get(position).getimgLink();
		news.newsLink = newsList.get(position).getnewsLink();
		
		if(newsList.get(position).getimgLink()!=null)
		{
			Log.d("Fetching IMage,,...","!!!!!!!");
			Bitmap bmp = imgChecker.loadUrlImage(newsList.get(position).getimgLink());
			Log.d("Setting Bitmap ...!"," !!!!!!");
			if(bmp!=null)
			news.img.setImageBitmap(bmp);
		}
		
		Log.d("Values Assigned!","Returning ConvertView...");
		return convertView;
	}
	
	public class ImgChecker
	{
		BaseAdapter bAdapter;
		
		public ImgChecker(BaseAdapter ba)
		{
			bAdapter = ba;
		}
		public synchronized Bitmap loadUrlImage(String getimgLink) {
			// TODO Auto-generated method stub
			if(ImageCache.containsKey(getimgLink))
				return ImageCache.get(getimgLink);
			else
			{
			new ImgFetcher().execute(getimgLink);
			return ImageCache.get(getimgLink);
			}
		}

	public class ImgFetcher extends AsyncTask<String,Void,Bitmap>{

		Bitmap bmp=null;
		String url;
		
	
		@Override
		protected Bitmap doInBackground(String... params) {
			// TODO Auto-generated method stub
			URL imgurl;
			url=params[0];
			String httpimgUrl = new String("http:").concat(url);
			try {
				imgurl = new URL(httpimgUrl);
				InputStream is = imgurl.openStream();
				Bitmap img = BitmapFactory.decodeStream(is);
				return img;
				
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
		@Override
		protected void onPostExecute(Bitmap result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			bmp=result;
			updateImageCache();
		}
		public synchronized void updateImageCache()
		{
			Bitmap scaledBmp = Bitmap.createScaledBitmap(bmp, 100, 100, true);
			bmp = null;
			ImageCache.put(url,scaledBmp);
			bAdapter.notifyDataSetChanged();
		}
	}
	}
}
