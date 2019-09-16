package com.saartha.iot.dataimportexport;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import com.saartha.iot.dataimportexport.messaging.impl.MQTTSubscriber;

@EnableDiscoveryClient
@SpringBootApplication
@ComponentScan("com.saartha.iot.*")
public class DataImportExportApplication {

	public static void main(String[] args) {

		ConfigurableApplicationContext ctx =
				SpringApplication.run(DataImportExportApplication.class, args);
		MQTTSubscriber subscriber = ctx.getBean(MQTTSubscriber.class);
		subscriber.config();
	}

}
