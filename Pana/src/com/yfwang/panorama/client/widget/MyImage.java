package com.yfwang.panorama.client.widget;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ErrorEvent;
import com.google.gwt.event.dom.client.ErrorHandler;
import com.google.gwt.event.dom.client.LoadEvent;
import com.google.gwt.event.dom.client.LoadHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Image;

public class MyImage extends Image
{
	private String newUrl;
	private HandlerRegistration addLoadHandler;
	private HandlerRegistration clickHandler;
	private boolean auto = false;
	private static final int ATTEMPT_TIME = 3;
	private int attemptTime = 0;
	private boolean needRefresh = false;
	private String detailUrl;
	private String overLayText;

	public String getOverLayText()
	{
		return overLayText;
	}

	public void setOverLayText(String overLayText)
	{
		this.overLayText = overLayText;
	}

	public String getDetailUrl()
	{
		return detailUrl;
	}

	public void setDetailUrl(String detailUrl)
	{
		this.detailUrl = detailUrl;
	}

	public boolean isNeedRefresh()
	{
		return needRefresh;
	}

	public void setNeedRefresh(boolean needRefresh)
	{
		this.needRefresh = needRefresh;
	}

	public MyImage()
	{
		super();
	}

	public MyImage(String url)
	{
		super("img/loading.gif");
		newUrl = url;
		bindLoadHandler();

	}

	public MyImage(String url, boolean auto)
	{
		super("img/loading.gif");
		this.auto = auto;
		newUrl = url;
		bindLoadHandler();

	}

	private void bindLoadHandler()
	{
		addLoadHandler = this.addLoadHandler(new LoadHandler()
		{

			@Override
			public void onLoad(LoadEvent event)
			{
				bindErrorHandler();
				setUrl(newUrl);
				addLoadHandler.removeHandler();
			}

		});
	}

	private void bindErrorHandler()
	{
		String tempUrlString = (newUrl == null ? getUrl() : newUrl);
		if (!tempUrlString.startsWith("data"))
		{
			this.addErrorHandler(new ErrorHandler()
			{

				@Override
				public void onError(ErrorEvent event)
				{
					setUrl("img/error.gif");
					if (auto && attemptTime < ATTEMPT_TIME)
					{
						setUrl(newUrl);
						attemptTime++;
					}
					else
					{
						addClickHandler();
					}
				}
			});
		}
	}

	private void addClickHandler()
	{
		clickHandler = this.addClickHandler(new ClickHandler()
		{

			@Override
			public void onClick(ClickEvent event)
			{
				clickHandler.removeHandler();
				setUrl(newUrl);
			}
		});
	}

	@Override
	public void setUrl(String url)
	{
		super.setUrl(url);

	}

	public void setNewUrl(String url, boolean auto)
	{
		this.auto = auto;
		newUrl = url;
		setUrl(url);
		bindErrorHandler();
	}

	@Override
	public String getUrl()
	{
		return newUrl == null ? super.getUrl() : newUrl;
	}

}
