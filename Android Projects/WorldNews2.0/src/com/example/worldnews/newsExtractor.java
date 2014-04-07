package com.example.worldnews;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.util.Log;

public class newsExtractor {

	private String gotXmlFile = null;
	public ArrayList<newsDataObject> NewsList;

	public newsExtractor(String result) {
		// TODO Auto-generated constructor stub
		gotXmlFile = result;
		NewsList = new ArrayList<newsDataObject>();
		Log.d("News Extractor Object Made..!!", "!!!!!!!!");
	}

	public  synchronized ArrayList<newsDataObject> extractNews() {
		Log.d("Extracting News !", "!!!!!!!!!");
		// using xmlpullpraser !
		try {
			XmlPullParserFactory xppf = XmlPullParserFactory.newInstance();

			// ////////////////////////////////////////////////////
			// ================= Significance ?

			xppf.setNamespaceAware(true);

			XmlPullParser parser = xppf.newPullParser();

			parser.setInput(new StringReader(gotXmlFile));
			int eventTYPE = parser.getEventType();
			String title,link,img_link,pub_date,newsSnippet,source;
			while (eventTYPE != XmlPullParser.END_DOCUMENT) {

				if (eventTYPE == XmlPullParser.START_TAG) {
					if (parser.getName().equals("item")) {
						title=link=img_link=pub_date=newsSnippet=source=null;
						int insideStatus = parser.next();
						//while(((parser.getName()!="item")||(insideStatus!=XmlPullParser.END_TAG))){
						while(true){
							Log.d("Looping","!!!!!");
							
							if(insideStatus == XmlPullParser.START_TAG)
							{
							if(parser.getName().equals("title"))
							{
								parser.next();
								title = parser.getText();
								Log.d("Got Title ! ",title);
								parser.next();
								
							}
							else if(parser.getName().equals("link"))
							{
								parser.next();
								link = parser.getText();
								Log
								.d("Got Link ! ",link);
								parser.next();
							}
							else if(parser.getName().equals("pubDate"))
							{
								parser.next();
								pub_date = parser.getText();
								Log.d("Got pubDate ! ",pub_date);
								parser.next();
								//break;
							}
							else if(parser.getName().equals("description"))
							{
								
								parser.next();//to to text content
								Document doc = Jsoup.parse(parser.getText());
								// Refer to http://jsoup.org/cookbook/extracting-data/selector-syntax //
								
								Element sourceElement  = doc.select("font[size=-1]").get(0);
								//headingElement = headingElement.nextElementSibling();
								source = sourceElement.text();
								Log.d("Got source..!! ",source);
								
								Element headingElement  = doc.select("font[size=-1]").get(1);
								//headingElement = headingElement.nextElementSibling();
								newsSnippet = headingElement.text();
								Log.d("Got snippet..!! ",newsSnippet);
								
								Element imgElement = doc.select("img").first();
								if(imgElement.hasAttr("src"))
								{
								img_link=imgElement.attr("src");
								}
								break;
							}
							}
							
							insideStatus=parser.next();
							if(parser.getName()!=null)
								Log.d("New Get NAme = ",parser.getName());
							
							//Thread.sleep(1000);
						}
						NewsList.add(new newsDataObject(title, link, pub_date, img_link,newsSnippet,source));
					}
					}
				eventTYPE  = parser.next();
			}
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Log.d("All News Extracted>!!!","!!!!!!!!!!!!!!!!!!!!!");
		
		return NewsList;

	}

}
