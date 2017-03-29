/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.impl;

import java.util.List;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
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
	VoyagePlan optimise(@Nullable IResource resource, IVessel vessel, long[] heelVolumeRangeInM3, int baseFuelPricePerMT, long vesselCharterInRatePerDay, IPortTimesRecord portTimesRecord,
			List<IOptionsSequenceElement> basicSequence, List<IVoyagePlanChoice> choices);

}