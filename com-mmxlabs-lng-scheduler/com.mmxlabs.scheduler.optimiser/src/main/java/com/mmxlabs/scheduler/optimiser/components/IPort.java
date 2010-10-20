/**
 * Copyright (C) Minimax Labs Ltd., 2010
 * All rights reserved.
 */

package com.mmxlabs.scheduler.optimiser.components;

import com.mmxlabs.common.indexedobjects.IIndexedObject;

/**
 * This interface defines a Port, a physical location that can be used as a
 * source or destination point for travel.
 * 
 * @author Simon Goodall
 * 
 */
public interface IPort extends IIndexedObject {

	/**
	 * The name of the port.
	 * 
	 * @return
	 */
	String getName();
}
