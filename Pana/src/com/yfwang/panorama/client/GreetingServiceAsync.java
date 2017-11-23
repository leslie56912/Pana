package com.yfwang.panorama.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.yfwang.panorama.shared.Attraction;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface GreetingServiceAsync
{

	void findAttraction(Long attractionID, AsyncCallback<Attraction> callback);
}
