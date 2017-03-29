/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.optimiser.common.components.ITimeWindow;

/**
 * Represents a generated charter out port slot which is associated with a given generated charter out
 * 
 * @author achurchill
 * 
 */
@NonNullByDefault
public interface IGeneratedCharterOutVesselEventPortSlot extends ICharterOutVesselEventPortSlot, IHeelOptionSupplierPortSlot, IHeelOptionConsumerPortSlot {

	@Override
	IGeneratedCharterOutVesselEvent getVesselEvent();

	void setPort(IPort port);

	void setTimeWindow(ITimeWindow port);

	void setVesselEvent(IGeneratedCharterOutVesselEvent event);
}
