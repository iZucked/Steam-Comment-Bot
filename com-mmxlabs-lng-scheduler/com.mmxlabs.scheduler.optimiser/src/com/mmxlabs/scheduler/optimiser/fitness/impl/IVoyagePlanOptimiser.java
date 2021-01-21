/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.impl;

import java.util.List;
import java.util.function.Consumer;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.contracts.ICharterCostCalculator;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimesRecord;
import com.mmxlabs.scheduler.optimiser.voyage.impl.IOptionsSequenceElement;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

/**
 * The {@link IVoyagePlanOptimiser} optimises the choices in a {@link VoyagePlan} based on {@link IVoyagePlanChoice} implementations. These are provided in a set order and they can edit the voyage
 * plan objects.
 * 
 * @author Simon Goodall
 * 
 */
@NonNullByDefault
public interface IVoyagePlanOptimiser {

	/**
	 * Optimise the voyage plan
	 * 
	 * @return
	 */
	@Nullable
	VoyagePlan optimise(@Nullable IResource resource, IVessel vessel, long[] heelVolumeRangeInM3, int[] baseFuelPricesPerMT, ICharterCostCalculator charterCostCalculator,
			IPortTimesRecord portTimesRecord, List<IOptionsSequenceElement> basicSequence, List<IVoyagePlanChoice> choices);

	void iterate(@Nullable IResource resource, IVessel vessel, long[] heelVolumeRangeInM3, int[] baseFuelPricesPerMT, ICharterCostCalculator charterCostCalculator, IPortTimesRecord portTimesRecord,
			List<IOptionsSequenceElement> basicSequence, List<IVoyagePlanChoice> choices, Consumer<VoyagePlan> hook);

}