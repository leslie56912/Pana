package com.yfwang.panorama.client.util;

import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.user.client.rpc.RpcRequestBuilder;

public class CustomRequestBuilder extends RpcRequestBuilder
{

	public CustomRequestBuilder()
	{
	}

	@Override
	protected RequestBuilder doCreate(String serviceEntryPoint)
	{
		RequestBuilder builder = super.doCreate(serviceEntryPoint);
		builder.setTimeoutMillis(6 * 1000);
		return builder;
	}
}
