/*
 *  Copyright (c) Dhana Krishnasamy. All rights reserved.
 *  License : Apache 2.0
 * @author Dhana Krishnasamy
 * 
 */
package com.dhana.servicebus.saswrapper;

import java.net.URI;
import java.net.URISyntaxException;

import com.microsoft.windowsazure.services.core.ServiceException;
import com.microsoft.windowsazure.services.serviceBus.implementation.WrapTokenManager;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientRequest;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.filter.ClientFilter;

public class SASFilter extends ClientFilter {

	WrapTokenManager tokenManager;

	public SASFilter(WrapTokenManager manager, SASRestProxy contract) {
		manager.setContract(contract);
		tokenManager = manager;
	}

	@Override
	public ClientResponse handle(ClientRequest cr) throws ClientHandlerException {
		String accessToken = getWrapToken(cr.getURI());
		cr.getHeaders().add("Authorization", accessToken);

		String secondaryAuthorizationUri = (String) cr.getHeaders().getFirst(
				"ServiceBusSupplementaryAuthorization");
		if ((secondaryAuthorizationUri != null)
				&& (!secondaryAuthorizationUri.isEmpty())) {
			String secondaryAccessToken = getWrapToken(URI
					.create(secondaryAuthorizationUri));
			cr.getHeaders().remove("ServiceBusSupplementaryAuthorization");
			cr.getHeaders().add("ServiceBusSupplementaryAuthorization",
					secondaryAccessToken);
		}

		return this.getNext().handle(cr);
	}

	private String getWrapToken(URI uri) {
		String result;
		try {
			result = tokenManager.getAccessToken(uri);
		} catch (ServiceException e) {
			// must wrap exception because of base class signature
			throw new ClientHandlerException(e);
		} catch (URISyntaxException e) {
			// must wrap exception because of base class signature
			throw new ClientHandlerException(e);
		}

		return "WRAP access_token=\"" + result + "\"";
	}
}
