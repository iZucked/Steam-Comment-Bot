/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
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

	public void setPort(@NonNull IPort port);

	public void setTimeWindow(@NonNull ITimeWindow port);

	public void setVesselEvent(@NonNull IGeneratedCharterOutVesselEvent event);

	public void setId(@NonNull String id);

}
