package com.saartha.iot.dataimportexport.messaging.impl;

import org.edgexfoundry.support.logging.client.EdgeXLogger;
import org.edgexfoundry.support.logging.client.EdgeXLoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.zeromq.SocketType;
import org.zeromq.ZMQ;
import org.zeromq.ZMQException;

import com.google.gson.Gson;
import com.saartha.iot.dataimportexport.messaging.IEventPublisher;
import com.saartha.iot.dataimportexport.model.ZeroMQModelData;

import lombok.Getter;
import lombok.Setter;
import zmq.ZError.CtxTerminatedException;

/**
 * This Class is Mainly Responsible to Publish the data on ZeroMQ.
 * 
 * @author MuraliKumar
 *
 */
@Component
@ConfigurationProperties(prefix = "zeromq")
public class ZeroMQEventPublisher implements IEventPublisher {

	/**
	 * LOGGER. variable to Log the Messages
	 */
	private static final EdgeXLogger LOGGER =
			EdgeXLoggerFactory.getEdgeXLogger(ZeroMQEventPublisher.class);

	/**
	 * Sleep Time to allow subscribers to connect.
	 */
	private static final long PUB_UP_SLEEP = 1000;

	/**
	 * port of ZeroMQ Connection.
	 */
	@Setter
	@Getter
	private String addressPort;

	/**
	 * publisher for ZeroMQ Protocol.
	 */
	private ZMQ.Socket publisher;

	/**
	 * Container for Sockets.
	 */
	private ZMQ.Context context;

	{
		context = ZMQ.context(1);
	}

	/**
	 * synchronized because zeroMQ sockets are not thread-safe.
	 */
	@Override
	public synchronized void sendMessage(String encodeToString) {

		try {

			if (publisher == null) {

				getPublisher();
			}

			if (publisher != null) {

				publisher.sendMore("events");
				ZeroMQModelData modelData = new ZeroMQModelData();

				modelData.setPayload(encodeToString);

				Gson gson = new Gson();
				publisher.send(gson.toJson(modelData));

				LOGGER.info("Sent event to export distro from dataexport");

			} else {

				LOGGER.error("Event not sent to export distro");
			}

		} catch (Exception e) {

			LOGGER.error("Unable to send message via ZMQ with Error :" + e.getMessage());
		}
	}

	/**
	 * Getting Publisher Connection Instance.
	 */
	private void getPublisher() {

		try {

			if (publisher == null) {

				publisher = context.socket(SocketType.PUB);
				publisher.bind(addressPort);
				Thread.sleep(PUB_UP_SLEEP);
			}

		} catch (CtxTerminatedException | InterruptedException | IllegalStateException
				| ZMQException e) {

			LOGGER.error("Unable to get a publisher.  Error:  " + e.getMessage());
			publisher = null;

		}

	}

}
