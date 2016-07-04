/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.impl;

import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
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
public interface IVoyagePlanOptimiser {

	/**
	 * Optimise the voyage plan
	 * 
	 * @return
	 */
	@Nullable
	VoyagePlan optimise(@Nullable IResource resource, @NonNull IVessel vessel, long startHeelInM3, int baseFuelPricePerMT, long vesselCharterInRatePerDay, @NonNull IPortTimesRecord portTimesRecord,
			@NonNull List<@NonNull IOptionsSequenceElement> basicSequence, @NonNull List<@NonNull IVoyagePlanChoice> choices);

}