package com.yfwang.panorama.client.util;

import com.google.gwt.query.client.impl.ConsoleBrowser;

public class Loger
{
	private static ConsoleBrowser consoleBrowser = new ConsoleBrowser();

	public static void log(String text)
	{
		consoleBrowser.log(text);
	}
}
