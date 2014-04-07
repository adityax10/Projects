package com.example.toptrackz;

public class track_data {

	public String artist_name, artist_url, song_name, listeners, duration, link_url,
			image_url;

	public track_data(String an, String au, String sn, String l, String d,
			String lu, String iu) {
		artist_name = an;
		artist_url = au;
		song_name = sn;
		listeners = l;
		duration = d;
		link_url = lu;
		image_url = iu;
	}
	
	public String getSong()
	{
		return song_name;
	}
	
	public String getDuration(){
		return duration;
	}
	public String getArtist()
	{
		return artist_name;
	}
	
	public String getImageurl()
	{
		return image_url;
	}
	public String getSongurl()
	{
		return link_url;
	}

}
