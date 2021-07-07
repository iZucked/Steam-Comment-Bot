/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.extensions.counterpartyvolume;

import javax.inject.Inject;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.SpotSlot;
import com.mmxlabs.models.lng.transformer.contracts.ISlotTransformer;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.providers.ICounterPartyVolumeProviderEditor;

/**
 */
public class CounterPartyVolumeDataTransformer implements ISlotTransformer {

	@Inject
	private ICounterPartyVolumeProviderEditor counterPartyVolumeDataProviderEditor;

	@Override
	public void slotTransformed(@NonNull Slot<?> modelSlot, @NonNull IPortSlot optimiserSlot) {
		if (modelSlot instanceof LoadSlot) {
			final LoadSlot ls = (LoadSlot) modelSlot;
			// Only applies to "real" DES purchases
			if (ls.isDESPurchase() && ls.isVolumeCounterParty() && !(modelSlot instanceof SpotSlot)) {
				counterPartyVolumeDataProviderEditor.setCounterPartyVolume(optimiserSlot);
			}
		}
	}
}
