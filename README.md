ServiceBusForWindows-SASWrapper
===============================

A wrapper library to work with SAS endpoint and thus enable Java clients to talk to SB for Windows

Introduction
============
At the moment Windows Azure Java API only supports getting Wrap tokens from ACS (WRAP v0.9). 
However there is no ACS availabe for ServiceBus for Windows Server (On Premise). So this library is to fill the gap, so that
Java guys can still use their OnPremise Service Bus to develop/test their applications.  

How It Works
============
This library sings-along with existing Windows Azure APIs,so it doesnt make you code using different APIs than one provided by MS Windows Azure API.
So how it works? well, Microsoft Azure API follows IOC/DI patterns to wire the things like connection management, 
authentication etc. So this library hookes itself into the Azure API and takes care of getting the auth tokens from SAS endpoint.

Main Bits
=========
SASRestProxy.java - Here is where the logic to send correct parameters to SAS endpoint and handling the respose is done. This guy shadows WrapRestProxy.java (only deals with ACS Wrap tokens) in the Azure API.

SASWrapperServiceContract.java - implements ServiceBusContract. This is the class which brings SASRestProxy in to the flow.
This class decorates the ServiceBusRestProxy from the API, so that SASRestProxy is used for getting the access tokens. Except wiring SASRestProxy,
this class doent do any work and delegates everything to ServiceBusContract which the Azure API will create and inject at runtime.

SASWrapperBuilder - This the DI guy, who registers all this library classes in to the IOC container (ServiceLoaded)

SASFilter - shadows WrapFilter- only purpose to get the SASRestProxy into WrapFilter, rest of the code is same as WrapFilter.

SASWrapperServiceBusConnectionSettings - shadows ServiceBusConnectionSettings, this is required as ServiceBusConnectionSettings is not public. Code looks sam as ServiceBusConnectionSettings.

SASTokenManager - shadows WrapTokenManager - only purpose to get the SASRestProxy into WrapTokenManager and use SASWrapperServiceBusConnectionSettings, rest of the code is same as WrapTokenManager. This class could have been avoided if only ServiceBusConnectionSettings was public.



This is for you
===============
Here is what you need to do, if you have been following the java examples from Microsoft site, this is what you will do normally

      1. Configuration config = ServiceBusConfiguration.configureWithWrapAuthentication(<paramaters>);
    	2. ServiceBusContract service = ServiceBusService.create(config);
    	3. for (QueueInfo queue : service.listQueues().getItems()){
    	    ..
    	   }
    	
if you want to use this library, you just change the line 2 like below in your code

      1. Configuration config = ServiceBusConfiguration.configureWithWrapAuthentication(<paramaters>);
    	2. ServiceBusContract service = config.create(SASWrapperServiceContract.class);
    	3. for (QueueInfo queue : service.listQueues().getItems()){
    	    ..
    	   }
    	   
and ofcouse include this lib as dependence in your pom file.

P.S:
====
This library is useful only if you want to deal with SB APIs directly / or want to use REST without JMS interface/AMQP. However on SB for Windows , 
I couldnt get the Qpid to connect to SB endpoints (AMQP) (while the authentication works). 
    	
    	

