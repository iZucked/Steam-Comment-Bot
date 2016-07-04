/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.contracts;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.transformer.ITransformerExtension;
import com.mmxlabs.scheduler.optimiser.components.IVesselEventPortSlot;

public interface IVesselEventTransformer extends ITransformerExtension {

	public void vesselEventTransformed(@NonNull VesselEvent modelEvent, @NonNull IVesselEventPortSlot optimiserEventSlot);
	
	public void vesselEventTransformed(@NonNull IVesselEventPortSlot optimiserEventSlot);

}
