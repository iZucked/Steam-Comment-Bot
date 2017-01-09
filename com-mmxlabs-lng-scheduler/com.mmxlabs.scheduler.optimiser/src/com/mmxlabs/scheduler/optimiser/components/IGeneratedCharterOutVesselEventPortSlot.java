/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components;

import org.eclipse.jdt.annotation.NonNull;

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

	void setPort(@NonNull IPort port);

	void setTimeWindow(@NonNull ITimeWindow port);

	void setVesselEvent(@NonNull IGeneratedCharterOutVesselEvent event);

}
