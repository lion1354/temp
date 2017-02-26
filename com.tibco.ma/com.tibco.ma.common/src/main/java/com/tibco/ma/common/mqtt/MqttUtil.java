package com.tibco.ma.common.mqtt;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MqttUtil {
	private static Logger log = LoggerFactory.getLogger(MqttUtil.class);

	private static int qos = 2;
	private static String broker = "tcp://127.0.0.1:1883";
	private static String clientId = "BrokerServer1";

	private static MqttUtil mqttUtil;

	public static synchronized MqttUtil instance() {
		if (mqttUtil == null)
			mqttUtil = new MqttUtil();
		return mqttUtil;
	}

	public void publish(String topic, String content, Boolean isRetained)
			throws Exception {
		MemoryPersistence persistence = new MemoryPersistence();

		try {
			MqttClient sampleClient = new MqttClient(broker, clientId,
					persistence);
			MqttConnectOptions connOpts = new MqttConnectOptions();
			connOpts.setCleanSession(true);

			log.info("Connecting to broker: " + broker);
			sampleClient.connect(connOpts);

			MqttMessage message = new MqttMessage(content.getBytes());
			message.setQos(qos);
			message.setRetained(isRetained);
			sampleClient.publish(topic, message);
			log.info("Message published");

			sampleClient.disconnect();
			log.info("Disconnected");
		} catch (MqttException me) {
			log.info("reason " + me.getReasonCode());
			log.info("msg " + me.getMessage());
			log.info("loc " + me.getLocalizedMessage());
			log.info("cause " + me.getCause());
			log.info("excep " + me);
			throw new Exception(me.getMessage());
		}
	}
}
