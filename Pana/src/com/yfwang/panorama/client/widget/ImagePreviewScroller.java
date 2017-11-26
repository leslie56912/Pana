package com.yfwang.panorama.client.widget;

import static com.google.gwt.query.client.GQuery.$;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.event.dom.client.LoadEvent;
import com.google.gwt.event.dom.client.LoadHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.event.logical.shared.AttachEvent.Handler;
import com.google.gwt.query.client.Function;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.yfwang.panorama.client.util.JqueryUtil;
import com.yfwang.panorama.client.util.Loger;

public class ImagePreviewScroller extends Composite
{

	private static ImagePreviewScrollerUiBinder uiBinder = GWT.create(ImagePreviewScrollerUiBinder.class);

	interface ImagePreviewScrollerUiBinder extends UiBinder<Widget, ImagePreviewScroller>
	{
	}

	private  int IMG_WIDTH;
	private  int IMG_GAP = 5;
	private  final int CAPACITY = 5;

	public ImagePreviewScroller()
	{
		initWidget(uiBinder.createAndBindUi(this));
		IMG_WIDTH = Window.getClientWidth() / CAPACITY;

	}

	@UiField
	HTMLPanel container;
	public List<MyImage> images = new ArrayList<MyImage>();
	public List<HTMLPanel> imageWrapers = new ArrayList<HTMLPanel>();
	public List<HTMLPanel> slider = new ArrayList<HTMLPanel>();
	public List<Function> functions = new ArrayList<Function>();
	public List<Function> innerFunctions = new ArrayList<Function>();
	private int currentSlider = -1;
	private boolean inEvent = false;
	private int touchStartX;
	private int speedTime = 300;
	private int currentImg = -1;
	private int totalHeight = 0;
	private boolean switching = false;

	@Override
	protected void onAttach()
	{
		super.onAttach();

		DOM.setEventListener(container.getElement(), new EventListener()
		{

			@Override
			public void onBrowserEvent(Event event)
			{

				switch (DOM.eventGetType(event))
				{
					case Event.ONTOUCHSTART:
					{
						onTouchStart(event);
						break;
					}
					case Event.ONTOUCHMOVE:
					{
						onTouchMove(event);
						break;
					}

				}
			}

		});
		DOM.sinkEvents(container.getElement(), Event.TOUCHEVENTS);
	}

	@Override
	protected void onDetach()
	{
		inEvent = false;

		super.onDetach();
	}

	public void setImages(final List<MyImage> w)
	{

		container.clear();
		if (w.isEmpty())
		{
			Label label = new Label("空空如也~~~~");
			label.getElement().setAttribute("style", "color:white;font-size:20px;padding-top:10px;");
			container.add(label);

		}
		slider.clear();
		this.images = w;
		currentSlider = 0;
		currentImg = -1;
		if (!images.isEmpty())
		{

			for (int i = 0; i < w.size(); i++)
			{
				Label label = new Label(w.get(i).getOverLayText());
				label.addStyleName("preview_box_text");
				HTMLPanel imgWrapper = new HTMLPanel("");
				imgWrapper.add(label);
				if (i % CAPACITY == 0)
				{

					imgWrapper.getElement().setAttribute("style",
							"width:" + IMG_WIDTH + "px;height:" + IMG_WIDTH + "px;background:url('" + w.get(i).getUrl() + "')" + ";background-size:100% 100%");
				}
				else
				{
					imgWrapper.getElement().setAttribute(
							"style",
							"margin-left:" + (IMG_GAP) + "px;width:" + IMG_WIDTH + "px;height:" + IMG_WIDTH + "px;background:url('" + w.get(i).getUrl() + "')"
									+ ";background-size:100% 100%");
				}
				imageWrapers.add(imgWrapper);
				imgWrapper.addStyleName("left");
				imgWrapper.addStyleName("preview_box");
			}

			for (int i = 0; i < images.size(); i++)
			{
				final HTMLPanel box = imageWrapers.get(i);
				final int index = i;
				box.addAttachHandler(new Handler()
				{

					@Override
					public void onAttachOrDetach(AttachEvent event)
					{
						if (event.isAttached())
						{
							Function f = new Function()
							{

								@Override
								public void f()
								{
									if (!inEvent)
									{
										currentImg = index;
										$(".chosen_img").removeClass("chosen_img");
										box.getElement().addClassName("chosen_img");
										if ((index + 1) % CAPACITY == 0 && index != 0)
										{
											DomEvent.fireNativeEvent(Document.get().createClickEvent(0, 0, 0, 0, 0, false, false, false, false),
													imageWrapers.get(index));
											onnextBtnCliced();
										}
										functions.get(index).f();
									}
								}

							};
							innerFunctions.add(f);
							JqueryUtil.click(box, f);
						}
					}
				});

				if (i % CAPACITY == 0)
				{
					HTMLPanel singleScroller = new HTMLPanel("");
					singleScroller.addStyleName("absolute");
					if (i == 0)
					{
						singleScroller.getElement().setAttribute("style", "left:" + IMG_GAP + "px;width:" + ((CAPACITY + 1) * (IMG_WIDTH + IMG_GAP)) + "px;");
					}
					else
					{
						singleScroller.getElement().setAttribute("style",
								"text-align:left;left:" + Window.getClientWidth() + "px;width:" + ((CAPACITY + 1) * (IMG_WIDTH + IMG_GAP)) + "px;");
						box.getElement().setAttribute(
								"style",
								"margin-left:" + (IMG_GAP) + "px;width:" + IMG_WIDTH + "px;height:" + IMG_WIDTH + "px;background:url('" + w.get(i).getUrl()
										+ "')" + ";background-size:100% 100%");
					}
					singleScroller.add(box);
					slider.add(singleScroller);
				}
				else
				{
					slider.get(i / CAPACITY).add(box);
				}
			}
		}

		// 添加字体

		if (slider.size() == 1)
		{
			slider.get(0).getElement().setAttribute("style", "left:" + IMG_GAP + "px;width:" + (CAPACITY * (IMG_WIDTH + IMG_GAP)) + "px;");
		}

	}

	public void putData(List<Function> functions)
	{
		for (int i = 0; i < slider.size(); i++)
			container.add(slider.get(i));
		if (!images.isEmpty())
		{
			$(".chosen_img").removeClass("chosen_img");
			imageWrapers.get(0).getElement().addClassName("chosen_img");
			currentImg = 0;
		}
		this.functions = functions;
	}

	private void onTouchStart(Event event)
	{
		touchStartX = event.getTouches().get(0).getPageX();
	}

	public void fireFunction(int index)
	{
		innerFunctions.get(index).f();
	}

	private void onTouchMove(Event event)
	{
		event.preventDefault();
		if (event.getTouches().get(0).getPageX() - touchStartX > 100)
		{
			onpreBtnCliced();
		}
		else if (event.getTouches().get(0).getPageX() - touchStartX < -100)
		{
			onnextBtnCliced();
		}
	}

	private void adjustSlider()
	{
		for (int i = 1; i < slider.size() - 1; i++)
		{
			if (currentSlider > i)
			{
				slider.get(i)
						.getElement()
						.setAttribute("style",
								"text-align:left;left:-" + CAPACITY * (IMG_WIDTH + IMG_GAP) + "px;width:" + (CAPACITY * (IMG_WIDTH + IMG_GAP)) + "px;");
			}
			else if (currentSlider < i)
			{
				slider.get(i)
						.getElement()
						.setAttribute("style",
								"text-align:left;left:" + Window.getClientWidth() + "px;width:" + ((CAPACITY + 1) * (IMG_WIDTH + IMG_GAP)) + "px;");
			}
		}
	}

	private void jumpRightPage(final int jumpSlider)
	{
		if (!inEvent && (jumpSlider >= 0) && jumpSlider < slider.size())
		{
			inEvent = true;
			slider.get(currentSlider).getWidget(CAPACITY).setVisible(false);
			JqueryUtil.animate(slider.get(currentSlider), new Function()
			{

				@Override
				public void f()
				{
					inEvent = false;

					currentSlider = jumpSlider;
					if (currentSlider != slider.size() - 1)
						slider.get(currentSlider).getWidget(CAPACITY).setVisible(true);
					DomEvent.fireNativeEvent(Document.get().createClickEvent(0, 0, 0, 0, 0, false, false, false, false), images.get(currentSlider * CAPACITY));
					adjustSlider();
				}
			}, speedTime, "{left:-" + CAPACITY * (IMG_WIDTH + IMG_GAP) + "px;width:" + (CAPACITY * (IMG_WIDTH + IMG_GAP)) + "px;}");
			JqueryUtil.animate(slider.get(jumpSlider), new Function()
			{

				@Override
				public void f()
				{

				}
			}, speedTime, "{left:" + IMG_GAP + "px;width:" + ((CAPACITY + 1) * (IMG_WIDTH + IMG_GAP)) + "px;}");
		}
	}

	private void jumpLeftPage(final int jumpSlider)
	{
		if (!inEvent && (jumpSlider >= 0) && jumpSlider < slider.size())
		{
			inEvent = true;
			JqueryUtil.animate(slider.get(jumpSlider), new Function()
			{

				@Override
				public void f()
				{

				}
			}, speedTime, "{left:" + IMG_GAP + "px;width:" + ((CAPACITY + 1) * (IMG_WIDTH + IMG_GAP)) + "px;}");

			JqueryUtil.animate(slider.get(currentSlider), new Function()
			{

				@Override
				public void f()
				{

					inEvent = false;
					if (currentSlider != slider.size() - 1)
						slider.get(currentSlider).getWidget(CAPACITY).setVisible(false);
					currentSlider = jumpSlider;
					slider.get(currentSlider).getWidget(CAPACITY).setVisible(true);
					DomEvent.fireNativeEvent(Document.get().createClickEvent(0, 0, 0, 0, 0, false, false, false, false), images.get(currentSlider * CAPACITY));
					adjustSlider();

				}
			}, speedTime, "{left:" + Window.getClientWidth() + "px;}");
		}

	}

	private void onnextBtnCliced()
	{
		Loger.log(slider.get(currentSlider).getWidgetCount() + " " + currentSlider);
		if (!inEvent && (slider.size() - currentSlider > 1))
		{
			inEvent = true;
			slider.get(currentSlider).getWidget(CAPACITY - 1).setVisible(false);
			JqueryUtil.animate(slider.get(currentSlider), new Function()
			{

				@Override
				public void f()
				{
					inEvent = false;
					currentSlider++;
					slider.get(currentSlider - 1).getWidget(CAPACITY - 1).setVisible(false);
					if (currentSlider != slider.size() - 1)
						slider.get(currentSlider).getWidget(CAPACITY - 1).setVisible(true);
				}
			}, speedTime, "{left:-" + CAPACITY * (IMG_WIDTH + IMG_GAP) + "px;width:" + (CAPACITY * (IMG_WIDTH + IMG_GAP)) + "px;}");
			JqueryUtil.animate(slider.get(currentSlider + 1), new Function()
			{

				@Override
				public void f()
				{

				}
			}, speedTime, "{left:" + IMG_GAP + "px;}");
		}
	}

	private void onpreBtnCliced()
	{
		if (!inEvent && (currentSlider > 0))
		{
			inEvent = true;
			JqueryUtil.animate(slider.get(currentSlider - 1), new Function()
			{

				@Override
				public void f()
				{

				}
			}, speedTime, "{left:" + IMG_GAP + "px;width:" + ((CAPACITY + 1) * (IMG_WIDTH + IMG_GAP)) + "px;}");

			JqueryUtil.animate(slider.get(currentSlider), new Function()
			{

				@Override
				public void f()
				{
					inEvent = false;
					currentSlider--;
					slider.get(currentSlider).getWidget(CAPACITY - 1).setVisible(true);

				}
			}, speedTime, "{left:" + Window.getClientWidth() + "px;}");
		}
	}

	public void slideNext()
	{
		currentImg++;
		if (currentImg % CAPACITY == 0)
		{
			onnextBtnCliced();
		}
		$(".chosen_img").removeClass("chosen_img");
		images.get(currentImg).getElement().addClassName("chosen_img");
	}

	public void slideBack()
	{
		currentImg--;
		if (currentImg % CAPACITY == 2 || currentImg == 2)
		{
			onpreBtnCliced();
		}
		$(".chosen_img").removeClass("chosen_img");
		images.get(currentImg).getElement().addClassName("chosen_img");
	}

}
