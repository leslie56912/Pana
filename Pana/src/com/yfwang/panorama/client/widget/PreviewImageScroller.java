package com.yfwang.panorama.client.widget;

import static com.google.gwt.query.client.GQuery.$;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.event.logical.shared.AttachEvent.Handler;
import com.google.gwt.query.client.Function;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.yfwang.panorama.client.util.JqueryUtil;

public class PreviewImageScroller extends Composite {

	private static PreviewImageScrollerUiBinder uiBinder = GWT
			.create(PreviewImageScrollerUiBinder.class);

	interface PreviewImageScrollerUiBinder extends
			UiBinder<Widget, PreviewImageScroller> {
	}

	public PreviewImageScroller() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@UiField
	HTMLPanel container;
	@UiField
	HTMLPanel imgContainer;
	private boolean inEvent = false;
	public List<MyImage> images = new ArrayList<MyImage>();
	public List<Function> functions = new ArrayList<Function>();
	public List<Function> innerFunctions = new ArrayList<Function>();

	public void setImages(ArrayList<MyImage> arrayList,
			ArrayList<Function> functionss) {
		int width = Window.getClientWidth() / 4 > 300 ? 300 : Window
				.getClientWidth() / 4;
		imgContainer.clear();
		FlexTable flexTable = new FlexTable();
		int i = 0;
		this.functions = functionss;
		for (MyImage img : arrayList) {
			final int index = i;
			Label label = new Label(img.getOverLayText());
			label.addStyleName("preview_box_text");
			final HTMLPanel imgWrapper = new HTMLPanel("");
			imgWrapper.add(label);
			imgWrapper.addStyleName("left");
			imgWrapper.addStyleName("preview_box");
			imgWrapper.getElement().setAttribute(
					"style",
					"width:" + width + "px;height:" + width
							+ "px;background:url('" + img.getUrl() + "')"
							+ ";background-size:100% 100%");
			img.addStyleName("left");
			flexTable.setWidget(0, i, imgWrapper);
			imgWrapper.addAttachHandler(new Handler() {

				@Override
				public void onAttachOrDetach(AttachEvent event) {
					if (event.isAttached()) {
						Function f = new Function() {

							@Override
							public void f() {
								if (!inEvent) {
									$(".chosen_img").removeClass("chosen_img");
									imgWrapper.getElement().addClassName(
											"chosen_img");
									inEvent = true;
									functions.get(index).f();
									if(index==0){
										
									}
								}
							}

						};
						innerFunctions.add(f);
						JqueryUtil.click(imgWrapper, f);
					}
				}
			});
			if(index==0){
				$(".chosen_img").removeClass("chosen_img");
				imgWrapper.getElement().addClassName(
						"chosen_img");
			}
			i++;
		}
		imgContainer.add(flexTable);
		
	}

	public void fireFunction(int index) {
		innerFunctions.get(index).f();
	}

	public void resetInEvent() {
		inEvent = false;
	}
}
