/*
 *  Copyright (c) Dhana Krishnasamy. All rights reserved.
 *  License : Apache 2.0
 * @author Dhana Krishnasamy
 * 
 */
package com.dhana.servicebus.saswrapper;

import java.net.URI;
import java.net.URISyntaxException;

import com.microsoft.windowsazure.services.core.utils.ConnectionStringSyntaxException;
import com.microsoft.windowsazure.services.serviceBus.implementation.ServiceBusConnectionString;

public class SASWrapperServiceBusConnectionSettings {
    private String uri;
    private String wrapUri;
    private String wrapName;
    private String wrapPassword;

    public SASWrapperServiceBusConnectionSettings(String connectionString, String uri, String wrapUri, String wrapName,
            String wrapPassword) throws ConnectionStringSyntaxException, URISyntaxException {
        if (connectionString != null) {
            parseConnectionString(connectionString);
        }
        else {
            this.uri = uri;
            this.wrapUri = wrapUri;
            this.wrapName = wrapName;
            this.wrapPassword = wrapPassword;
        }
    }

    public String getUri() {
        return uri;
    }

    public String getWrapUri() {
        return wrapUri;
    }

    public String getWrapName() {
        return wrapName;
    }

    public String getWrapPassword() {
        return wrapPassword;
    }

    private boolean parseConnectionString(String connectionString) throws URISyntaxException,
            ConnectionStringSyntaxException {
        ServiceBusConnectionString cs = new ServiceBusConnectionString(connectionString);
        setUri(cs);
        setWrapUri(cs);
        wrapName = cs.getSharedSecretIssuer();
        wrapPassword = cs.getSharedSecretValue();
        return true;
    }

    private void setUri(ServiceBusConnectionString connectionString) {
        uri = connectionString.getEndpoint().replaceFirst("^sb://", "https://");
    }

    private void setWrapUri(ServiceBusConnectionString connectionString) throws URISyntaxException {
        if (connectionString.getStsEndpoint() == null || connectionString.getStsEndpoint().isEmpty()) {
            URI hostUri = new URI(uri);
            String namespace = hostUri.getHost().split("\\.")[0];
            wrapUri = "https://" + namespace + "-sb.accesscontrol.windows.net/WRAPv0.9";
        }
        else {
            wrapUri = connectionString.getStsEndpoint().replaceAll("\\/$", "") + "/WRAPv0.9";
        }
    }
}
