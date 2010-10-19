/**
 * Copyright (C) Minimaxlabs, 2010
 * All rights reserved.
 */

package com.mmxlabs.scheduler.optimiser.components;


/**
 * A cargo ties together a load and discharge pairing.
 * 
 * @author Simon Goodall
 * 
 */
public interface ICargo {

	/**
	 * The ID of this cargo.
	 * 
	 * @return
	 */
	String getId();

	ILoadSlot getLoadSlot();
	
	IDischargeSlot getDischargeSlot();
}
