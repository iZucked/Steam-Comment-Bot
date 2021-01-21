/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.indexedobjects.IIndexedObject;

/**
 * This interface defines a Port, a physical location that can be used as a source or destination point for travel.
 * 
 * @author Simon Goodall
 * 
 */
public interface IPort extends IIndexedObject {

	/**
	 * The name of the port.
	 * 
	 * @return the port's name
	 */
	@NonNull
	String getName();

	/**
	 * The time zone id of the port
	 * 
	 * @return the port's time zone id
	 */
	@NonNull
	String getTimeZoneId();
}
