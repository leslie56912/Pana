package com.yfwang.panorama.client.widget;

import com.google.gwt.query.client.Function;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.yfwang.panorama.client.util.JqueryUtil;

public class Toast
{
	private static Label msgLabel = new Label("");

	static
	{
		msgLabel.addStyleName("toast");
		RootPanel.get().add(msgLabel);
	}

	public static void showMsg(String text)
	{
		msgLabel.getElement().setAttribute("style", "");
		msgLabel.setVisible(true);
		msgLabel.setText(text);
		JqueryUtil.fadeOut(msgLabel, new Function()
		{

			@Override
			public void f()
			{
				msgLabel.setVisible(false);
			}
		}, 2500);
	}
	public static void showMsgAtTop(String text)
	{
		msgLabel.getElement().setAttribute("style", "top:10%;");
		msgLabel.setVisible(true);
		msgLabel.setText(text);
		JqueryUtil.fadeOut(msgLabel, new Function()
		{
			
			@Override
			public void f()
			{
				msgLabel.setVisible(false);
			}
		}, 2500);
	}

	public static void showMsg(String text, int time)
	{
		msgLabel.setVisible(true);
		msgLabel.setText(text);
		JqueryUtil.fadeOut(msgLabel, new Function()
		{

			@Override
			public void f()
			{
				msgLabel.setVisible(false);
			}
		}, time);
	}
}
