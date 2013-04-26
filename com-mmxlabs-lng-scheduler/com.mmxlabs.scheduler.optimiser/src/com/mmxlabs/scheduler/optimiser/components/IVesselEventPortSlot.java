/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components;

/**
 * Represents a port slot which is associated with a given vessel event
 * 
 * @author Tom Hinton
 * 
 */
public interface IVesselEventPortSlot extends IHeelOptionsPortSlot {
	IVesselEvent getVesselEvent();
}
