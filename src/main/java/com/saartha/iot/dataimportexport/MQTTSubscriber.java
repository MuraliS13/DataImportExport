package com.saartha.iot.dataimportexport.messaging.impl;

import java.util.Base64;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.edgexfoundry.support.logging.client.EdgeXLogger;
import org.edgexfoundry.support.logging.client.EdgeXLoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.saartha.iot.dataimportexport.messaging.IEventPublisher;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * @author MuraliKumar
 *
 */
@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "mqtt")
public class MQTTSubscriber implements MqttCallback {

	/**
	 * Logger.
	 */
	private static final EdgeXLogger LOGGER =
			EdgeXLoggerFactory.getEdgeXLogger(MQTTSubscriber.class);

	/**
	 * MQTT Broker.
	 */
	private String broker;
	/**
	 * MQTT Broker port.
	 */
	private int port;
	/**
	 * MQTT Broker user name.
	 */
	private String userName;
	/**
	 * MQTT Broker password.
	 */

	private String password;
	/**
	 * MQTT topic.
	 */

	private String topic;
	/**
	 * MQTT client.
	 */
	private MqttClient mqttClient;

	/**
	 * 
	 */
	@Autowired
	private IEventPublisher publisher;

	/**
	 * Creating MQTT Connection and Subscribing to Topic.
	 */
	public void config() {

		if (mqttClient == null) {

			String brokerUrl = "tcp://" + this.getBroker() + ":" + this.getPort();

			MqttConnectOptions connectionOptions = new MqttConnectOptions();

			System.out.println(brokerUrl);

			try {
				mqttClient = new MqttClient(brokerUrl, MqttClient.generateClientId());
				connectionOptions.setCleanSession(true);

				if (this.getPassword() != null) {
					connectionOptions.setPassword(this.getPassword().toCharArray());
				}
				if (this.getUserName() != null) {
					connectionOptions.setUserName(this.getUserName());
				}

				mqttClient.connect(connectionOptions);
				mqttClient.setCallback(this);

				mqttClient.subscribe(this.getTopic(), 2);

				LOGGER.info("MQTT Connection Successful.");

			} catch (MqttException e) {

				LOGGER.error("Unable to get a MQTT subscriber.  Error:  " + e.getMessage());
				mqttClient = null;

			}
		}
	}

	@Override
	public void connectionLost(Throwable cause) {
		LOGGER.error("\"Connection to MQTT broker lost!.  Error:  " + cause.getMessage());
		this.config();
	}

	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {

		LOGGER.info("Message received:\t" + new String(message.getPayload()));
		publisher.sendMessage(Base64.getEncoder().encodeToString(message.getPayload()));

	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken token) {
		// Not do anything currently...

	}

}
