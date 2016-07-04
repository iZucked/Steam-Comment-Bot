/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.voyage;

import java.util.List;

import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.voyage.impl.IDetailsSequenceElement;
import com.mmxlabs.scheduler.optimiser.voyage.impl.IOptionsSequenceElement;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyageDetails;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyageOptions;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

/**
 * Calculate voyage details for LNG vessels. Typically LNG vessels are dual fuel vessels, burning their own base fuel (such as heavy-fuel oil (HFO) or marine diesel oil (MDO)) and are capable of
 * burning LNG boil-off as an alternative. LNG is always boiling off and the alternative is to vent it. On laden voyages, LNG vessels will almost always be running on LNG boil-off (NBO) but this does
 * not provide enough energy to travel at full speed and in such cases the additional energy requirements will come from base fuel or by force boiling off (FBO) more LNG. LNG is often cheaper than
 * base fuel and so it is usually desirable to use LNG over base fuel.
 * 
 * @author Simon Goodall
 * 
 */
public interface ILNGVoyageCalculator {

	/**
	 * Calculate the fuel requirements, speed and times for the port to port journey options defined in {@link VoyageOptions} and store the results in {@link VoyageDetails}.
	 * 
	 * @param options
	 * @param output
	 */
	void calculateVoyageFuelRequirements(VoyageOptions options, VoyageDetails output);

	/**
	 * Given an alternating sequence of {@link IPortDetails} and {@link IVoyageDetails}, populate a {@link VoyagePlan} object with the group voyage details. E.g. calculate load and discharge
	 * quantities.
	 * 
	 * @param voyagePlan
	 * @param vessel
	 * @param startHeelInM3 
	 * @param arrivalTimes
	 *            an array of arrival times at each slot in the sequence
	 * 
	 *            <pre>
	 *            arrivalTimes[0] <=> sequence[0]
	 *            arrivalTimes[1] <=> sequence[2]
	 *            ...
	 * </pre>
	 * @param sequence
	 * @return Returns zero for a feasible journey, or a positive integer indicating a relative ranking of problems due to e.g. capacity violations. Returns a negative for a infeasible journey.
	 */
	int calculateVoyagePlan(VoyagePlan voyagePlan, IVessel vessel, long startHeelInM3, int baseFuelPricePerMT, IPortTimesRecord voyageRecord, IDetailsSequenceElement... sequence);

	/**
	 * Given a sequence containing {@link IPortDetails}, {@link IVoyageOptions} and other objects, create and return a new sequence with fuel cost information attached to corresponding appropriate
	 * objects.
	 * 
	 * @author Simon McGregor
	 * @param sequence
	 * @return Returns a new sequence of appropriate Details objects with fuel cost information attached
	 */
	List<IDetailsSequenceElement> generateFuelCostCalculatedSequence(IOptionsSequenceElement... sequence);
}
