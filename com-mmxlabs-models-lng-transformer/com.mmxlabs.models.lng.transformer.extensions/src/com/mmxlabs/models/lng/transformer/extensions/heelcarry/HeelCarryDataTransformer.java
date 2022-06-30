/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.extensions.heelcarry;

import javax.inject.Inject;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.SpotSlot;
import com.mmxlabs.models.lng.transformer.contracts.ISlotTransformer;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.providers.IHeelCarrySlotProviderEditor;

/**
 */
public class HeelCarryDataTransformer implements ISlotTransformer {

	@Inject
	private IHeelCarrySlotProviderEditor heelCarrySlotProviderEditor;

	@Override
	public void slotTransformed(@NonNull Slot<?> modelSlot, @NonNull IPortSlot optimiserSlot) {
		if (modelSlot instanceof DischargeSlot discharge) {
			// Only applies to "real" DES purchases
			if (discharge.isHeelCarry() && !(discharge instanceof SpotSlot)) {
				heelCarrySlotProviderEditor.setHeelCarrySlot(optimiserSlot);
			}
		}
	}
}
