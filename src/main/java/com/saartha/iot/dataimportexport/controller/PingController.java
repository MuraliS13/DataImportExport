package com.saartha.iot.dataimportexport.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.saartha.iot.dataimportexport.messaging.impl.MQTTSubscriber;

@RestController
@RequestMapping("/api/v1/ping")
public class PingController {

	/**
	 * 
	 */
	@Autowired
	private MQTTSubscriber subscriber;

	/**
	 * 
	 * @return String
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String ping() {

		if (subscriber.getMqttClient() == null) {
			subscriber.config();
		}
		return "pong";
	}
}
