package com.yfwang.panorama.client.util;

import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestTimeoutException;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.rpc.StatusCodeException;
import com.yfwang.panorama.client.widget.Toast;
import com.yfwang.panorama.shared.CommonUtil;

public abstract class CallbackHandler<E> implements AsyncCallback<E>
{

	public CallbackHandler(Object serviceAsync, boolean lock)
	{
		startAsyncCall(serviceAsync, true);
	}

	public CallbackHandler(Object serviceAsync)
	{
		startAsyncCall(serviceAsync, true);
	}

	private void startAsyncCall(Object serviceAsync, final boolean retryAble)
	{
		final ServiceDefTarget serviceDefTarget = (ServiceDefTarget) serviceAsync;
		serviceDefTarget.setRpcRequestBuilder(new CustomRequestBuilder()
		{
			@Override
			protected void doSetCallback(final RequestBuilder rb, RequestCallback callback)
			{
				super.doSetCallback(rb, callback);
			}
		});
	}

	@Override
	public void onFailure(Throwable caught)
	{
		if (caught instanceof RequestTimeoutException)
		{
			Toast.showMsg("网络好像出问题了");
		}
		else if (caught instanceof StatusCodeException)
		{
			// back to home page
			Toast.showMsg("业务繁忙,请稍后");
		}
		else
		{
			fail(caught);
		}
	}

	@Override
	public void onSuccess(E result)
	{
		success(result);
	}

	public abstract void success(E result);

	public void fail(Throwable caught)
	{
		if (CommonUtil.isDebug)
			Loger.log(caught.getMessage());
	}

}
