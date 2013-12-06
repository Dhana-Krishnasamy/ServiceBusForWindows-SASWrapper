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
This library sings-along with existing Windows Azure APIs,so it doesnt make you write using different APIs.
So how it works? well, Microsoft Azure API follows IOC/DI patterns to wire the things like connection management, 
authentication etc. So this library creates a minimal required functionality, which is to pass correct parameters to SAS endpoint 
and extract the required values from the reponse and pass it back to the caller, and extends few Azure API interfaces to 
hook the new functionality in the flow. Thats all to it. 

Main Bits
=========
SASRestProxy.java - Here is where the logic to send correct parameters to SAS endpoint and handling the respose id done. 
This guy shadows WrapRestProxy.java in the Azure API.

SASWrapperServiceContract.java - implements ServiceBusContract. This is the class which brings SASRestProxy in to the flow.
This class decorates the ServiceBusRestProxy from the API, so that SASRestProxy is used for getting the access tokens. Except wiring SASRestProxy,
this class doent do any work and delegates everything to ServiceBusContract which the Azure API will create and inject at runtime.


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
    	
    	

