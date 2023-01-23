/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.verifier;

import java.util.function.Function;

import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.scheduler.optimiser.components.IVesselCharter;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;

public class OptimiserResultVerifierUtils {
	public static int getTotalLoadsOnSequences(ISequences sequences, OptimiserDataMapper mapper, boolean filterRoundTrips) {
		return getTotalOnSequences(sequences, mapper, filterRoundTrips, e -> (e instanceof LoadSlot));
	}
	
	public static int getTotalDischargesOnSequences(ISequences sequences, boolean filterRoundTrips, OptimiserDataMapper mapper) {
		return getTotalOnSequences(sequences, mapper, filterRoundTrips, e -> (e instanceof DischargeSlot));
	}

	public static int getTotalOnSequences(ISequences sequences, OptimiserDataMapper mapper, boolean filterRoundTrips, Function<Object, Boolean> checker) {
		int count = 0;
		IVesselProvider vesselProvider = mapper.getDataTransformer().getInjector().getInstance(IVesselProvider.class);
		for (IResource resource : sequences.getResources()) {
			if (filterRoundTrips) {
			IVesselCharter vesselCharter = vesselProvider.getVesselCharter(resource);
				if (vesselCharter.getVesselInstanceType() == VesselInstanceType.ROUND_TRIP) {
					continue;
				}
			}
			ISequence sequence = sequences.getSequence(resource);
			for (ISequenceElement sequenceElement : sequence) {
				if (checker.apply(mapper.getSlotForElement(sequenceElement))) {
					count++;
				}
			}
		}
		return count;
	}


}
