/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.constraints.impl;

import java.util.List;

import javax.inject.Inject;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.constraints.IResourceElementConstraintChecker;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselCharter;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.providers.ICounterPartyWindowProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;

public class CounterPartyWindowChecker extends AbstractPairwiseConstraintChecker implements IResourceElementConstraintChecker {

	@Inject
	private IVesselProvider vesselProvider;

	@Inject
	private IPortSlotProvider portSlotProvider;

	@Inject
	private ICounterPartyWindowProvider counterPartyWindowProvider;

	public CounterPartyWindowChecker(@NonNull final String name) {
		super(name);
	}

	@Override
	public boolean checkPairwiseConstraint(@NonNull ISequenceElement first, @NonNull ISequenceElement second, @NonNull IResource resource, @Nullable List<@NonNull String> messages) {
		final IPortSlot firstSlot = portSlotProvider.getPortSlot(first);
		final IPortSlot secondSlot = portSlotProvider.getPortSlot(second);
		final IVesselCharter vesselCharter = vesselProvider.getVesselCharter(resource);
		if (firstSlot instanceof @NonNull final ILoadOption loadOption && secondSlot instanceof @NonNull final IDischargeOption dischargeOption) {
			if (vesselCharter.getVesselInstanceType() == VesselInstanceType.DES_PURCHASE) {
				return !counterPartyWindowProvider.isCounterPartyWindow(dischargeOption);
			} else if (vesselCharter.getVesselInstanceType() == VesselInstanceType.FOB_SALE) {
				return !counterPartyWindowProvider.isCounterPartyWindow(loadOption);
			}
		}
		return true;
	}

	@Override
	public boolean checkElement(@NonNull final ISequenceElement element, @NonNull final IResource resource, @Nullable List<@NonNull String> messages) {
		@NonNull
		final IVesselCharter vesselCharter = vesselProvider.getVesselCharter(resource);
		@NonNull
		final IPortSlot slot = portSlotProvider.getPortSlot(element);
		if ((vesselCharter.getVesselInstanceType() == VesselInstanceType.DES_PURCHASE && slot instanceof IDischargeOption) //
				|| (vesselCharter.getVesselInstanceType() == VesselInstanceType.FOB_SALE && slot instanceof ILoadOption)) {
			return !counterPartyWindowProvider.isCounterPartyWindow(slot);
		}
		return true;

	}

}
