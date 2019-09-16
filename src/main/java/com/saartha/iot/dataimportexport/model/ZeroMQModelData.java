/**
 * 
 */
package com.saartha.iot.dataimportexport.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author MuraliKumar
 *
 */

@Setter
@Getter
@NoArgsConstructor
public class ZeroMQModelData {

	/**
	 * 
	 */
	private String Checksum = "123";

	/**
	 * 
	 */
	private String CorrelationID = "123";
	/**
	 * 
	 */
	private String ContentType = "application/json";
	/**
	 * 
	 */
	private String Payload;

}
