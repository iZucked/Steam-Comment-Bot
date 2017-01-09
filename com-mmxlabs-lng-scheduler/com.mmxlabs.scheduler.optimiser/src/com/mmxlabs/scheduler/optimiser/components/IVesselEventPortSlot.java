/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components;

import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.optimiser.core.ISequenceElement;

/**
 * Represents a port slot which is associated with a given vessel event
 * 
 * @author Tom Hinton
 * 
 */
public interface IVesselEventPortSlot extends IHeelOptionsPortSlot {
	IVesselEvent getVesselEvent();

	@NonNull
	List<@NonNull ISequenceElement> getEventSequenceElements();

	@NonNull
	List<@NonNull IPortSlot> getEventPortSlots();
}
