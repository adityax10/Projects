package com.example.worldnews;

import android.util.Log;

public class newsDataObject {
	
	public String title,link,img_link,pub_date,newsSnippet,source;
	
	public newsDataObject(String title,String link,String pub_date,String img_link,String newsSnippet,String source)
	{
		this.title=title;
		this.link=link;
		this.pub_date=pub_date;
		this.img_link=img_link;
		this.newsSnippet=newsSnippet;
		this.source=source;
		Log.d("News Item Created !",title+link+pub_date+img_link+newsSnippet);
	}
	
	public String getnewsHeading()
	{
		return this.title;
	}
	
	public String getnewsLink()
	{
		return this.link;
	}
	public String getnewsDuration()
	{
		return this.pub_date;
	}
	public String getimgLink()
	{
		return this.img_link;
	}
	public String getnewsSnippet()
	{
		return this.newsSnippet;
	}
	public String getnewsSource()
	{
		return this.source;
	}






}
