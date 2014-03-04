/*
 *  Copyright (c) Dhana Krishnasamy. All rights reserved.
 *  License : Apache 2.0
 * @author Dhana Krishnasamy
 * 
 */
package com.dhana.servicebus.saswrapper;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import javax.inject.Inject;
import javax.management.timer.Timer;

import com.microsoft.windowsazure.services.core.ServiceException;
import com.microsoft.windowsazure.services.core.utils.DateFactory;

import com.microsoft.windowsazure.services.serviceBus.implementation.WrapAccessTokenResult;
import com.microsoft.windowsazure.services.serviceBus.implementation.WrapContract;

public class SASTokenManager {
	WrapContract contract;
	private final DateFactory dateFactory;
	private final String uri;
	private final String name;
	private final String password;

	private final Map<String, ActiveToken> activeTokens;

	@Inject
	public SASTokenManager(SASRestProxy contract, SASWrapperServiceBusConnectionSettings connectionSettings, DateFactory dateFactory) {
		this.contract = contract;
		this.dateFactory = dateFactory;
		this.uri = connectionSettings.getWrapUri();
		this.name = connectionSettings.getWrapName();
		this.password = connectionSettings.getWrapPassword();
		activeTokens = new ConcurrentHashMap<String, ActiveToken>();
	}

	/**
	 * @return the contract
	 */
	public WrapContract getContract() {
		return contract;
	}

	/**
	 * @param contract
	 *            the contract to set
	 */
	public void setContract(WrapContract contract) {
		this.contract = contract;
	}

	public String getAccessToken(URI targetUri) throws ServiceException,
			URISyntaxException {
		Date now = dateFactory.getDate();

		URI scopeUri = new URI("http", targetUri.getAuthority(),
				targetUri.getPath(), null, null);
		String scope = scopeUri.toString();

		ActiveToken active = this.activeTokens.get(scope);

		if (active != null && now.before(active.getExpiresUtc())) {
			return active.getWrapResponse().getAccessToken();
		}

		// sweep expired tokens out of collection
		Iterator<Entry<String, ActiveToken>> iterator = activeTokens.entrySet()
				.iterator();
		while (iterator.hasNext()) {
			Entry<String, ActiveToken> entry = iterator.next();
			if (!now.before(entry.getValue().getExpiresUtc())) {
				iterator.remove();
			}
		}

		WrapAccessTokenResult wrapResponse = getContract().wrapAccessToken(uri,
				name, password, scope);
		Date expiresUtc = new Date(now.getTime() + wrapResponse.getExpiresIn()
				* Timer.ONE_SECOND / 2);

		ActiveToken acquired = new ActiveToken();
		acquired.setWrapResponse(wrapResponse);
		acquired.setExpiresUtc(expiresUtc);
		this.activeTokens.put(scope, acquired);

		return wrapResponse.getAccessToken();
	}

	class ActiveToken {
		Date expiresUtc;
		WrapAccessTokenResult wrapResponse;

		/**
		 * @return the expiresUtc
		 */
		public Date getExpiresUtc() {
			return expiresUtc;
		}

		/**
		 * @param expiresUtc
		 *            the expiresUtc to set
		 */
		public void setExpiresUtc(Date expiresUtc) {
			this.expiresUtc = expiresUtc;
		}

		/**
		 * @return the wrapResponse
		 */
		public WrapAccessTokenResult getWrapResponse() {
			return wrapResponse;
		}

		/**
		 * @param wrapResponse
		 *            the wrapResponse to set
		 */
		public void setWrapResponse(WrapAccessTokenResult wrapResponse) {
			this.wrapResponse = wrapResponse;
		}
	}

}
