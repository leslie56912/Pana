package com.yfwang.panorama.client.page;

import static com.google.gwt.query.client.GQuery.$;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.query.client.Function;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.yfwang.panorama.client.GreetingService;
import com.yfwang.panorama.client.GreetingServiceAsync;
import com.yfwang.panorama.client.util.CallbackHandler;
import com.yfwang.panorama.client.util.JqueryUtil;
import com.yfwang.panorama.client.util.PanaUtil;
import com.yfwang.panorama.client.widget.MyImage;
import com.yfwang.panorama.client.widget.PreviewImageScroller;
import com.yfwang.panorama.shared.Area;
import com.yfwang.panorama.shared.Attraction;
import com.yfwang.panorama.shared.Marker;

public class MainViewImpl extends Composite
{
	public static final GreetingServiceAsync greetService = GWT.create(GreetingService.class);
	private static MainViewImplUiBinder uiBinder = GWT.create(MainViewImplUiBinder.class);

	interface MainViewImplUiBinder extends UiBinder<Widget, MainViewImpl>
	{
	}

	public MainViewImpl()
	{
		initWidget(uiBinder.createAndBindUi(this));
		width = Window.getClientWidth() / 4 > 300 ? 300 : Window.getClientWidth() / 4;
	}

	@UiField
	HTMLPanel markersHP;
	@UiField
	FlexTable markersTable;
	@UiField
	Image listMarkersBtn;
	@UiField
	Image menuBtn;
	@UiField
	PreviewImageScroller previewer;
	@UiField
	HTMLPanel detailPanel;
	@UiField
	HTMLPanel mainCover;
	@UiField
	Label detailDetailLb;
	@UiField
	Label attactionLb;
	private boolean isEvent = false;
	private int doingMarkerIndex = -1;
	private String doingMarkerId = "";
	private List<Marker> markers = new ArrayList<Marker>();
	private int currentMarkerIndex = -1;
	private Attraction attraction;
	private Area currentArea;
	private int foundMarker = -1;
	private int width = -1;
	private boolean clickingMarker = false;

	@UiHandler("menuBtn")
	void onmenuBtnClicked(ClickEvent event)
	{
		if (!isEvent)
		{
			isEvent = true;
			if (previewer.isVisible())
			{
				JqueryUtil.animate(previewer, new Function()
				{

					@Override
					public void f()
					{
						isEvent = false;
						previewer.setVisible(false);
						JqueryUtil.css(menuBtn, "-webkit-transform", "rotate(180deg)");
					}
				}, 500, "{bottom:-" + width + "px;}");
			}
			else
			{
				JqueryUtil.animate(previewer, new Function()
				{

					@Override
					public void f()
					{
						isEvent = false;
						previewer.setVisible(true);
						JqueryUtil.css(menuBtn, "-webkit-transform", "rotate(0deg)");
					}
				}, 500, "{bottom:" + 60 + "px;}");
			}
		}
	}

	@UiHandler("listMarkersBtn")
	void onListMarkesClicked(ClickEvent event)
	{
		if (detailPanel.isVisible())
			detailPanel.setVisible(false);
		if (!isEvent)
		{
			isEvent = true;
			if (markersHP.isVisible())
			{

				JqueryUtil.animate(markersHP, new Function()
				{

					@Override
					public void f()
					{
						markersHP.setVisible(false);
						isEvent = false;
					}
				}, 500, "{left:-50%}");
			}
			else
			{
				markersHP.setVisible(true);
				JqueryUtil.animate(markersHP, new Function()
				{

					@Override
					public void f()
					{
						isEvent = false;
					}
				}, 500, "{left:0}");
			}
		}
	}

	@UiHandler("geoBtn")
	void ongeoBtnClicked(ClickEvent event)
	{
		PanaUtil.toggleGeo();
	}

	@Override
	protected void onAttach()
	{
		detailPanel.setVisible(false);
		super.onAttach();
		settle();
		greetService.findAttraction(1L, new CallbackHandler<Attraction>(greetService)
		{

			@Override
			public void success(Attraction result)
			{
				attraction = result;
				attactionLb.setText(attraction.getName());
				loadPanas();
				setbottomMenu();
				setMarkers(attraction.getAreas().get(0));
				currentArea = attraction.getAreas().get(0);
			}

		});

		new Timer()
		{

			@Override
			public void run()
			{
				if (PanaUtil.isViewReady())
				{
					cancel();
					Element view = DOM.getElementById("photosphere");
					addTouchEvent(view);
					for (int i = 1; i < attraction.getAreas().size(); i++)
					{
						PanaUtil.preload(attraction.getAreas().get(i).getPanaUrl());
					}
				}
			}
		}.scheduleRepeating(100);

		DOM.setEventListener(mainCover.getElement(), new EventListener()
		{

			@Override
			public void onBrowserEvent(Event event)
			{

				switch (DOM.eventGetType(event))
				{
					case Event.ONTOUCHMOVE:
						event.preventDefault();
						break;
				}
			}
		});
		DOM.sinkEvents(mainCover.getElement(), Event.TOUCHEVENTS);
	}

	protected void loadPanas()
	{
		PanaUtil.initPana(attraction.getAreas().get(0).getPanaUrl());
	}

	private void setMarkers(Area area)
	{
		PanaUtil.clearMakers();
		markers.clear();
		markersTable.clear();
		for (int i = 0; i < area.getMarkers().size(); i++)
		{
			final int index = i;
			final Marker marker = area.getMarkers().get(i);
			if (marker.isShowMsg())
			{
				marker.setIconPath("<img src='img/content_arrow.png' id='location" + i + "' style='width:30px'/>");
			}
			else
			{
				marker.setIconPath("<img src='img/pointer_arrow.png' id='location" + i + "' style='width:30px'/>");
			}
			marker.setTooltip(marker.getName());
			addMarker(marker);
			markers.add(marker);
			final Image image = new Image("img/location.png");
			Label label = new Label(marker.getName());
			ClickHandler clickHandler = new ClickHandler()
			{

				@Override
				public void onClick(ClickEvent event)
				{
					currentMarkerIndex = index;
					putMarkerInfo(index);
					detailDetailLb.setText(marker.getIntroduction());
				}
			};
			image.addClickHandler(clickHandler);
			label.addClickHandler(clickHandler);
			markersTable.setWidget(i, 0, image);
			markersTable.setWidget(i, 1, label);
		}
	}

	private void setbottomMenu()
	{
		ArrayList<MyImage> arrayList = new ArrayList<MyImage>();
		ArrayList<Function> functions = new ArrayList<Function>();
		for (int i = 0; i < attraction.getAreas().size(); i++)
		{
			final int index = i;
			MyImage myImage = new MyImage(attraction.getAreas().get(i).getThumUrl());
			myImage.setOverLayText(attraction.getAreas().get(i).getShortName());
			arrayList.add(myImage);
			Function function = new Function()
			{

				@Override
				public void f()
				{
					jumpToArea(attraction.getAreas().get(index));
				}

			};
			functions.add(function);
		}
		previewer.setImages(arrayList, functions);
	}

	private void jumpToArea(Area a)
	{
		if (currentArea != null && a != null && a.getID() != currentArea.getID())
		{
			mainCover.setVisible(true);
			currentArea = a;
			if (markersHP.isVisible())
			{
				onListMarkesClicked(null);
			}
			new Timer()
			{

				@Override
				public void run()
				{
					if (PanaUtil.isViewReady())
					{
						mainCover.setVisible(false);
						previewer.resetInEvent();
						cancel();
					}
				}
			}.scheduleRepeating(100);
			setMarkers(a);
			PanaUtil.addMarkers();
			PanaUtil.swithPana(a.getPanaUrl());
			detailPanel.setVisible(false);
		}

	}

	private void settle()
	{
		$("#photosphere").click(new Function()
		{

			@Override
			public void f()
			{
				if (markersHP.isVisible())
				{
					onListMarkesClicked(null);
				}
			}

		});

		// 监听器

		new Timer()
		{

			@Override
			public void run()
			{
				String id = PanaUtil.getChosenMarkerId();
				if (id.equals(""))
					// || (doingMarkerId.equals(id) && detailPanel.isVisible()))
					return;
				int index = 0;
				for (Marker m : markers)
				{
					if ((m.getID() + "").equals(id))
					{
						foundMarker = index;
						break;
					}
					index++;
				}

				if (PanaUtil.isViewReady() && foundMarker != -1)
				{
					clickingMarker = true;
					if (!markers.get(foundMarker).isShowMsg())
					{

						for (Area a : attraction.getAreas())
						{
							if (a.getID() == markers.get(foundMarker).getGoingAreaID())
							{
								previewer.fireFunction(attraction.getAreas().indexOf(a));
								break;
							}
						}
					}
					else
					{
						doingMarkerId = markers.get(index).getID() + "";
						putMarkerInfo(index);
						detailDetailLb.setText(markers.get(index).getIntroduction());
					}
					foundMarker = -1;
					PanaUtil.resetCurrentMarker();
				}
			}
		}.scheduleRepeating(200);

	}

	private void addTouchEvent(final Element element)
	{
		DOM.setEventListener(element, new EventListener()
		{

			@Override
			public void onBrowserEvent(Event event)
			{

				switch (DOM.eventGetType(event))
				{
					case Event.ONCLICK:
						if (!PanaUtil.isPC())
						{
							new Timer()
							{

								@Override
								public void run()
								{
									if (!clickingMarker)
									{
										onmenuBtnClicked(null);
									}
									else
									{
										clickingMarker = false;
									}
								}
							}.schedule(300);
						}
						else
						{
							if (markersHP.isVisible())
								onListMarkesClicked(null);
							if (detailPanel.isVisible())
								detailPanel.setVisible(false);
						}
						break;
					case Event.ONTOUCHSTART:
						foundMarker = -1;
						if (markersHP.isVisible())
						{
							onListMarkesClicked(null);
						}
						break;
					case Event.ONTOUCHEND:
					{
						break;
					}
					case Event.ONTOUCHMOVE:
					{
						if (detailPanel.isVisible())
						{
							doingMarkerId = "";
							detailPanel.setVisible(false);
						}
						break;
					}

				}
			}

		});
		DOM.sinkEvents(element, Event.TOUCHEVENTS | Event.ONCLICK);
	}

	protected void putMarkerInfo(final int index)
	{
		if (markersHP.isVisible())
		{
			onListMarkesClicked(null);
		}
		PanaUtil.goToMaker(index);
		if (!detailPanel.isVisible())
		{
			detailPanel.setVisible(true);
		}
	}

	@Override
	protected void onDetach()
	{
		super.onDetach();
	}

	private void addMarker(Marker marker)
	{
		PanaUtil.addMarker(marker.getID() + "", marker.getName(), marker.getIconPath(), marker.getLontitude() + "", marker.getLatitude() + "",
				marker.getTooltip());
	}

}
