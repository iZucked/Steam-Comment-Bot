/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.extensions.fcl;

import javax.inject.Inject;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.transformer.contracts.ISlotTransformer;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.providers.IFullCargoLotProviderEditor;

/**
 */
public class FullCargoLotTransformer implements ISlotTransformer {

	@Inject
	private IFullCargoLotProviderEditor fullCargoLotProviderEditor;

	@Override
	public void slotTransformed(@NonNull Slot<?> modelSlot, @NonNull IPortSlot optimiserSlot) {

		if (modelSlot.getSlotOrDelegateFullCargoLot()) {
			fullCargoLotProviderEditor.addFCLSlot(optimiserSlot);
		}
	}
}
