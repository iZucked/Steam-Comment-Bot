package com.mmxlabs.scheduler.optimiser.constraints.impl;

import java.util.List;

import javax.inject.Inject;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.providers.ICounterPartyWindowProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;

public class CounterPartyWindowChecker extends AbstractPairwiseConstraintChecker {

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
		final IVesselAvailability vesselAvailability = vesselProvider.getVesselAvailability(resource);
		if (firstSlot instanceof @NonNull final ILoadOption loadOption && secondSlot instanceof @NonNull final IDischargeOption dischargeOption) {
			if (vesselAvailability.getVesselInstanceType() == VesselInstanceType.DES_PURCHASE) {
				if (counterPartyWindowProvider.isCounterPartyWindow(dischargeOption)) {
					return false;
				}
			} else if (vesselAvailability.getVesselInstanceType() == VesselInstanceType.FOB_SALE) {
				if (counterPartyWindowProvider.isCounterPartyWindow(loadOption)) {
					return false;
				}
			}
		}
		return true;
	}

	
}
