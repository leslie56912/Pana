package com.yfwang.panorama.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;
import com.yfwang.panorama.client.page.MainViewImpl;
import com.yfwang.panorama.client.util.Global;
import com.yfwang.panorama.shared.CommonUtil;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Panorama implements EntryPoint
{
	public void onModuleLoad()
	{
		if (CommonUtil.isDebug)
		{
			RootPanel.get().add(new MainViewImpl());
		}
		else
		{
			if (Global.isWx())
			{
				RootPanel.get().add(new MainViewImpl());
			}
			else
			{
				Window.alert("请在微信客户端打开");
			}
		}

	}

}
