/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components;

import com.mmxlabs.optimiser.common.components.ITimeWindow;

/**
 * Represents a generated charter out port slot which is associated with a given generated charter out
 * 
 * @author achurchill
 * 
 */
public interface IGeneratedCharterOutVesselEventPortSlot extends IVesselEventPortSlot {
	
	@Override
	IGeneratedCharterOutVesselEvent getVesselEvent();

	public void setPort(IPort port);

	public void setTimeWindow(ITimeWindow port);
	
	public void setVesselEvent(IGeneratedCharterOutVesselEvent event);
	
	public void setId(String id);

}
