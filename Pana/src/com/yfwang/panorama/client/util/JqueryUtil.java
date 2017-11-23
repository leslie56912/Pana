package com.yfwang.panorama.client.util;

import static com.google.gwt.query.client.GQuery.$;

import com.google.gwt.dom.client.Element;
import com.google.gwt.query.client.Function;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

public class JqueryUtil
{
	private static GQuery currentAnimation = null;
	private static boolean singleEvnetHappening = false;

	public static void slideUp(Widget w, final Function function, int millSec)
	{
		currentAnimation = $("#" + getElementId(w)).slideUp(millSec, function);

	}

	public static void slideDown(Widget w, final Function function, int millSec)
	{
		currentAnimation = $("#" + getElementId(w)).slideDown(function);
	}

	public static void fadeIn(Widget w, final Function function, int millSec)
	{
		currentAnimation = $("#" + getElementId(w)).fadeIn(millSec, function);

	}

	public static int getHeight(Widget w)
	{
		return $("#" + getElementId(w)).height();
	}

	public static void click(Widget e, Function f)
	{
		$("#" + getElementId(e)).click(f);
	}

	/**
	 * prevent next animation from happening, but allow current animation
	 * continue
	 */
	public static void stopAnimation()
	{
		if (currentAnimation != null)
			currentAnimation.dequeue();
	}

	public static void fadeOut(Widget w, final Function function, int millSec)
	{
		currentAnimation = $("#" + getElementId(w)).fadeOut(millSec, function);
	}

	public static void animate(Widget w, Function f, int millSec, Object stringOrProperties, int delay, Function delayF)
	{
		currentAnimation = $("#" + getElementId(w)).animate(stringOrProperties, millSec, f).delay(delay, delayF);
	}

	public static void css(Widget w, String cssName, String cssPro)
	{
		currentAnimation = $("#" + getElementId(w)).css(cssName, cssPro);
	}

	public static void animate(Widget w, final Function function, int millSec, Object stringOrProperties)
	{
		currentAnimation = $("#" + getElementId(w)).animate(stringOrProperties, millSec, function);
	}

	public static void animateSingleOnly(Widget w, final Function function, int millSec, Object stringOrProperties)
	{
		if (!singleEvnetHappening)
		{
			singleEvnetHappening = true;
			currentAnimation = $("#" + getElementId(w)).animate(stringOrProperties, millSec, new Function()
			{

				@Override
				public void f()
				{
					function.f();
					singleEvnetHappening = false;
				}
			});
		}
	}

	public static String getElementId(Widget w)
	{
		String id = "";
		if (w.getElement().getId().equals(""))
		{
			id = HTMLPanel.createUniqueId();
			w.getElement().setId(id);
		}
		else
		{
			id = w.getElement().getId();
		}
		return id;
	}

}
