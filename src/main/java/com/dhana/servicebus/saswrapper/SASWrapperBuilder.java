/*
 *  Copyright (c) Dhana Krishnasamy. All rights reserved.
 *  License : Apache 2.0
 * @author Dhana Krishnasamy
 * 
 */
package com.dhana.servicebus.saswrapper;

import static com.microsoft.windowsazure.services.core.utils.ExportUtils.getPropertyIfExists;

import java.net.URISyntaxException;
import java.util.Map;

import com.microsoft.windowsazure.services.core.Builder;
import com.microsoft.windowsazure.services.core.Builder.Registry;
import com.microsoft.windowsazure.services.core.utils.ConnectionStringSyntaxException;
import com.microsoft.windowsazure.services.serviceBus.ServiceBusConfiguration;

public class SASWrapperBuilder implements Builder.Exports {

	public void register(Registry registry) {
		registry.add(SASWrapperServiceContract.class);
		registry.add(SASFilter.class);
		registry.add(SASRestProxy.class);

		registry.add(new Builder.Factory<SASWrapperServiceBusConnectionSettings>() {

			public <S> SASWrapperServiceBusConnectionSettings create(String profile,
					Class<S> service, Builder builder,
					Map<String, Object> properties) {
				try {
					return new SASWrapperServiceBusConnectionSettings((String) getPropertyIfExists(profile, properties,	ServiceBusConfiguration.CONNECTION_STRING),
							(String) getPropertyIfExists(profile, properties,ServiceBusConfiguration.URI),
							(String) getPropertyIfExists(profile, properties,ServiceBusConfiguration.WRAP_URI),
							(String) getPropertyIfExists(profile, properties,ServiceBusConfiguration.WRAP_NAME),
							(String) getPropertyIfExists(profile, properties,ServiceBusConfiguration.WRAP_PASSWORD));
				} catch (ConnectionStringSyntaxException e) {
					throw new RuntimeException(e.getMessage(), e);
				} catch (URISyntaxException e) {
					throw new RuntimeException(e.getMessage(), e);
				}
			}

		});

	}

}
