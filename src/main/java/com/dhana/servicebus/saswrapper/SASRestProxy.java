package com.dhana.servicebus.saswrapper;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;
import javax.ws.rs.core.MediaType;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.microsoft.windowsazure.services.core.ServiceException;
import com.microsoft.windowsazure.services.core.UserAgentFilter;
import com.microsoft.windowsazure.services.core.utils.ServiceExceptionFactory;
import com.microsoft.windowsazure.services.serviceBus.implementation.WrapAccessTokenResult;
import com.microsoft.windowsazure.services.serviceBus.implementation.WrapContract;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.representation.Form;

public class SASRestProxy implements WrapContract {

	Client channel;

	static Log log = LogFactory.getLog(WrapContract.class);
	private Pattern pattern = Pattern.compile("ExpiresOn=(\\d+)");
	
	
	@Inject
	public SASRestProxy(Client channel, UserAgentFilter userAgentFilter) {
		this.channel = channel;
		this.channel.addFilter(userAgentFilter);
	}

	public WrapAccessTokenResult wrapAccessToken(String uri, String name,
			String password, String scope) throws ServiceException {
		Form requestForm = new Form();
		requestForm.add("grant_type", "authorization_code");
		requestForm.add("client_id", name);
		requestForm.add("client_secret", password);
		requestForm.add("scope", scope);

		//System.out.println("client Id:" + name);
		//System.out.println("scope :" + scope);

		ClientResponse cResponse;
		StringWriter writer = new StringWriter();
		try {

			cResponse = channel.resource(uri)
					.accept(MediaType.APPLICATION_FORM_URLENCODED)
					.type(MediaType.APPLICATION_FORM_URLENCODED)
					.post(ClientResponse.class, requestForm);

			IOUtils.copy(cResponse.getEntityInputStream(), writer, Charset
					.forName("UTF-8").toString());
		} catch (UniformInterfaceException e) {
			log.warn("WRAP server returned error acquiring access_token", e);
			throw ServiceExceptionFactory.process("SASWrapper", new ServiceException(
					"WRAP server returned error acquiring access_token", e));
		} catch (IOException e) {
			log.warn("Error while reading access_token", e);
			throw ServiceExceptionFactory.process("SASWrapper", new ServiceException(
					"Error while reading access_token", e));
		}

		WrapAccessTokenResult response = new WrapAccessTokenResult();
		String tokenString = writer.toString();
		//System.out.println("responseForm " + tokenString);

		Matcher m = pattern.matcher(tokenString);
		String expiresOn = "";
		while (m.find()) {
			expiresOn = m.group().split("=")[1];
		}
		long expiresInMillis = Long.parseLong(expiresOn) * 1000
				- System.currentTimeMillis();
		
		response.setAccessToken(tokenString);
		//In Seconds
		response.setExpiresIn(expiresInMillis / 1000);

		return response;
	}

}
