package com.hanyu.mobilesafe.engine;

import java.io.IOException;
import java.io.InputStream;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import com.hanyu.mobilesafe.domain.UpdateInfo;

public class UpdateInfoParser {
	public static UpdateInfo getUpdateInfo(InputStream is) throws XmlPullParserException, IOException
	{
		UpdateInfo info = null;
		XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
		XmlPullParser parser = factory.newPullParser();
		parser.setInput(is, "UTF-8");
		info = new UpdateInfo();
		int type = parser.getEventType();
		while(type != XmlPullParser.END_DOCUMENT)
		{
			if (type == XmlPullParser.START_TAG)
			{
				if ("version".equals(parser.getName()))
				{
					String version = parser.nextText();
					info.setVersion(version);
				}
				else if ("description".equals(parser.getName()))
				{
					String description = parser.nextText();
					info.setDescription(description);
				}
				else if ("apkurl".equals(parser.getName()))
				{
					String apkurl = parser.nextText();
					info.setApkurl(apkurl);
				}					
			}				
			type = parser.next();
		}			
		return info;
	}
	
}
