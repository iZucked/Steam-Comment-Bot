/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.impl;

import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.voyage.ILNGVoyageCalculator;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimesRecord;
import com.mmxlabs.scheduler.optimiser.voyage.impl.IOptionsSequenceElement;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyageOptions;
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
	 * Check internal state is valid (i.e. all setters have been called).
	 */
	void init();

	/**
	 * Reset the state of this object ready for a new optimisation.
	 */
	void reset();

	/**
	 * Clean up all references.
	 */
	void dispose();

	/**
	 * Optimise the voyage plan
	 * 
	 * @return
	 */
	VoyagePlan optimise();

	/**
	 * Returns the basic sequence that is being optimised over.
	 * 
	 * @return
	 */
	List<@NonNull IOptionsSequenceElement> getBasicSequence();

	/**
	 * Sets the basic voyage plan sequence. This should be {@link IPortSlot} instances separated by {@link VoyageOptions} instances implementing {@link Cloneable}. The {@link VoyageOptions} objects
	 * will be modified during optimisation.
	 * 
	 * @param basicSequence
	 */
	void setBasicSequence(@NonNull final List<IOptionsSequenceElement> basicSequence);

	void setPortTimesRecord(@NonNull final IPortTimesRecord portTimesRecord);

	void setVesselCharterInRatePerDay(int charterInRatePerDay);

	/**
	 * Get the {@link IVessel} to evaluate voyages against.
	 * 
	 * @return
	 */
	IVessel getVessel();

	/**
	 * Set the {@link IVessel} to evaluate voyages against.
	 * 
	 * @param vessel
	 */
	void setVessel(@NonNull IVessel vessel, @Nullable IResource resource, int baseFuelPricePerMT);

	/**
	 * Set the base fuel price for the voyage
	 * 
	 * @param baseFuelPricePerMT
	 */
	void setBaseFuelPricePerMT(int baseFuelPricePerMT);

	/**
	 * Once optimised, returns the best {@link VoyagePlan} cost.
	 * 
	 * @return
	 */
	long getBestCost();

	/**
	 * Once optimised, returns the best {@link VoyagePlan}.
	 * 
	 * @return
	 */
	VoyagePlan getBestPlan();

	/**
	 * Returns the {@link ILNGVoyageCalculator} used in the {@link VoyagePlanOptimiser}.
	 * 
	 * @return
	 */
	ILNGVoyageCalculator getVoyageCalculator();

	/**
	 * Add a new choice to the ordered stack of choices. If this choice depends upon the choice of another {@link IVoyagePlanChoice}, then that object should have already been added.
	 * 
	 * @param choice
	 */
	void addChoice(final IVoyagePlanChoice choice);

	/**
	 * The start heel for this particular voyage.
	 * 
	 * @param heelVolumeInM3
	 */
	void setStartHeel(long heelVolumeInM3);

	long getStartHeel();

}