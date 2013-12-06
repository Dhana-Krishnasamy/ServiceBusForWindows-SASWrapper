package com.dhana.servicebus.saswrapper;

import javax.inject.Inject;

import com.microsoft.windowsazure.services.core.ServiceException;
import com.microsoft.windowsazure.services.core.ServiceFilter;
import com.microsoft.windowsazure.services.core.UserAgentFilter;
import com.microsoft.windowsazure.services.serviceBus.ServiceBusContract;
import com.microsoft.windowsazure.services.serviceBus.implementation.BrokerPropertiesMapper;
import com.microsoft.windowsazure.services.serviceBus.implementation.ServiceBusRestProxy;
import com.microsoft.windowsazure.services.serviceBus.models.BrokeredMessage;
import com.microsoft.windowsazure.services.serviceBus.models.CreateQueueResult;
import com.microsoft.windowsazure.services.serviceBus.models.CreateRuleResult;
import com.microsoft.windowsazure.services.serviceBus.models.CreateSubscriptionResult;
import com.microsoft.windowsazure.services.serviceBus.models.CreateTopicResult;
import com.microsoft.windowsazure.services.serviceBus.models.GetQueueResult;
import com.microsoft.windowsazure.services.serviceBus.models.GetRuleResult;
import com.microsoft.windowsazure.services.serviceBus.models.GetSubscriptionResult;
import com.microsoft.windowsazure.services.serviceBus.models.GetTopicResult;
import com.microsoft.windowsazure.services.serviceBus.models.ListQueuesOptions;
import com.microsoft.windowsazure.services.serviceBus.models.ListQueuesResult;
import com.microsoft.windowsazure.services.serviceBus.models.ListRulesOptions;
import com.microsoft.windowsazure.services.serviceBus.models.ListRulesResult;
import com.microsoft.windowsazure.services.serviceBus.models.ListSubscriptionsOptions;
import com.microsoft.windowsazure.services.serviceBus.models.ListSubscriptionsResult;
import com.microsoft.windowsazure.services.serviceBus.models.ListTopicsOptions;
import com.microsoft.windowsazure.services.serviceBus.models.ListTopicsResult;
import com.microsoft.windowsazure.services.serviceBus.models.QueueInfo;
import com.microsoft.windowsazure.services.serviceBus.models.ReceiveMessageOptions;
import com.microsoft.windowsazure.services.serviceBus.models.ReceiveMessageResult;
import com.microsoft.windowsazure.services.serviceBus.models.ReceiveQueueMessageResult;
import com.microsoft.windowsazure.services.serviceBus.models.ReceiveSubscriptionMessageResult;
import com.microsoft.windowsazure.services.serviceBus.models.RuleInfo;
import com.microsoft.windowsazure.services.serviceBus.models.SubscriptionInfo;
import com.microsoft.windowsazure.services.serviceBus.models.TopicInfo;
import com.sun.jersey.api.client.Client;

public class SASWrapperServiceContract implements ServiceBusContract {

	ServiceBusRestProxy contractProxy;

	@Inject
	public SASWrapperServiceContract(Client channel, SASFilter authFilter,SASWrapperServiceBusConnectionSettings connectionSettings,
			UserAgentFilter userAgentFilter, BrokerPropertiesMapper mapper) {
		channel.addFilter(authFilter);
		channel.addFilter(userAgentFilter);
		contractProxy = new ServiceBusRestProxy(channel, new ServiceFilter[0],connectionSettings.getUri(), mapper);
	}

	public ServiceBusContract withFilter(ServiceFilter filter) {
		return contractProxy.withFilter(filter);
	}

	public void sendQueueMessage(String queuePath, BrokeredMessage message)
			throws ServiceException {
		contractProxy.sendQueueMessage(queuePath, message);

	}

	public ReceiveQueueMessageResult receiveQueueMessage(String queuePath)
			throws ServiceException {
		return contractProxy.receiveQueueMessage(queuePath);
	}

	public ReceiveQueueMessageResult receiveQueueMessage(String queuePath,
			ReceiveMessageOptions options) throws ServiceException {
		return contractProxy.receiveQueueMessage(queuePath, options);
	}

	public void sendTopicMessage(String topicPath, BrokeredMessage message)
			throws ServiceException {
		contractProxy.sendTopicMessage(topicPath, message);
	}

	public ReceiveSubscriptionMessageResult receiveSubscriptionMessage(
			String topicPath, String subscriptionName) throws ServiceException {
		return contractProxy.receiveSubscriptionMessage(topicPath,
				subscriptionName);
	}

	public ReceiveSubscriptionMessageResult receiveSubscriptionMessage(
			String topicPath, String subscriptionName,
			ReceiveMessageOptions options) throws ServiceException {
		return contractProxy.receiveSubscriptionMessage(topicPath,
				subscriptionName, options);
	}

	public void unlockMessage(BrokeredMessage message) throws ServiceException {
		contractProxy.unlockMessage(message);

	}

	public void sendMessage(String path, BrokeredMessage message)
			throws ServiceException {
		contractProxy.sendMessage(path, message);

	}

	public ReceiveMessageResult receiveMessage(String path)
			throws ServiceException {
		return contractProxy.receiveMessage(path);
	}

	public ReceiveMessageResult receiveMessage(String path,
			ReceiveMessageOptions options) throws ServiceException {
		return contractProxy.receiveMessage(path,options);
	}

	public void deleteMessage(BrokeredMessage message) throws ServiceException {
		contractProxy.deleteMessage(message);
	}

	public CreateQueueResult createQueue(QueueInfo queueInfo)
			throws ServiceException {
		return contractProxy.createQueue(queueInfo);
	}

	public void deleteQueue(String queuePath) throws ServiceException {
		contractProxy.deleteQueue(queuePath);
	}

	public GetQueueResult getQueue(String queuePath) throws ServiceException {
		return contractProxy.getQueue(queuePath);
	}

	public ListQueuesResult listQueues() throws ServiceException {
		return contractProxy.listQueues();
	}

	public ListQueuesResult listQueues(ListQueuesOptions options)
			throws ServiceException {
		return contractProxy.listQueues(options);
	}

	public QueueInfo updateQueue(QueueInfo queueInfo) throws ServiceException {
		return contractProxy.updateQueue(queueInfo);
	}

	public CreateTopicResult createTopic(TopicInfo topic)
			throws ServiceException {
		
		return contractProxy.createTopic(topic);
	}

	public void deleteTopic(String topicPath) throws ServiceException {
		contractProxy.deleteTopic(topicPath);

	}

	public GetTopicResult getTopic(String topicPath) throws ServiceException {
		return contractProxy.getTopic(topicPath);
	}

	public ListTopicsResult listTopics() throws ServiceException {
		return contractProxy.listTopics();
	}

	public ListTopicsResult listTopics(ListTopicsOptions options)
			throws ServiceException {
		return contractProxy.listTopics(options);
	}

	public TopicInfo updateTopic(TopicInfo topicInfo) throws ServiceException {
		return contractProxy.updateTopic(topicInfo);
	}

	public CreateSubscriptionResult createSubscription(String topicPath,
			SubscriptionInfo subscription) throws ServiceException {
		return contractProxy.createSubscription(topicPath, subscription);
	}

	public void deleteSubscription(String topicPath, String subscriptionName)
			throws ServiceException {
		contractProxy.deleteSubscription(topicPath, subscriptionName);

	}

	public GetSubscriptionResult getSubscription(String topicPath,
			String subscriptionName) throws ServiceException {
		return contractProxy.getSubscription(topicPath, subscriptionName);
	}

	public ListSubscriptionsResult listSubscriptions(String topicPath)
			throws ServiceException {
		return contractProxy.listSubscriptions(topicPath);
	}

	public ListSubscriptionsResult listSubscriptions(String topicPath,
			ListSubscriptionsOptions options) throws ServiceException {
		return contractProxy.listSubscriptions(topicPath, options);
	}

	public SubscriptionInfo updateSubscription(String topicName,
			SubscriptionInfo subscriptionInfo) throws ServiceException {
		return contractProxy.updateSubscription(topicName, subscriptionInfo);
	}

	public CreateRuleResult createRule(String topicPath,
			String subscriptionName, RuleInfo rule) throws ServiceException {
		return contractProxy.createRule(topicPath, subscriptionName, rule);
	}

	public void deleteRule(String topicPath, String subscriptionName,
			String ruleName) throws ServiceException {
		contractProxy.deleteRule(topicPath, subscriptionName, ruleName);

	}

	public GetRuleResult getRule(String topicPath, String subscriptionName,
			String ruleName) throws ServiceException {
		return contractProxy.getRule(topicPath, subscriptionName, ruleName);
	}

	public ListRulesResult listRules(String topicPath, String subscriptionName)
			throws ServiceException {
		return contractProxy.listRules(topicPath, subscriptionName);
	}

	public ListRulesResult listRules(String topicPath, String subscriptionName,
			ListRulesOptions options) throws ServiceException {
		return contractProxy.listRules(topicPath, subscriptionName, options);
	}

	public void renewQueueLock(String queueName, String messageId,
			String lockToken) throws ServiceException {
		contractProxy.renewQueueLock(queueName, messageId, lockToken);
	}

	public void renewSubscriptionLock(String topicName,
			String subscriptionName, String messageId, String lockToken)
			throws ServiceException {
		contractProxy.renewSubscriptionLock(topicName, subscriptionName, messageId, lockToken);
	}

}