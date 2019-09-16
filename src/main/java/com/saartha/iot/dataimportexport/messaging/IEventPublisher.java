/**
 * 
 */
package com.saartha.iot.dataimportexport.messaging;

/**
 * @author MuraliKumar
 *
 */
@FunctionalInterface
public interface IEventPublisher {

	void sendMessage(String encodeToString);

}
