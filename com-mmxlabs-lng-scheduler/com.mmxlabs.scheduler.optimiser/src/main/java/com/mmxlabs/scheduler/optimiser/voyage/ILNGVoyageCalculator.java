package com.mmxlabs.scheduler.optimiser.voyage;

import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyageDetails;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyageOptions;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

/**
 * Calculate voyage details for LNG vessels. Typically LNG vessels are dual fuel
 * vessels, burning their own base fuel (such as heavy-fuel oil (HFO) or marine
 * diesel oil (MDO)) and are capable of burning LNG boil-off as an alternative.
 * LNG is always boiling off and the alternative is to vent it. On laden
 * voyages, LNG vessels will almost always be running on LNG boil-off (NBO) but
 * this does not provide enough energy to travel at full speed and in such cases
 * the additional energy requirements will come from base fuel or by force
 * boiling off (FBO) more LNG. LNG is often cheaper than base fuel and so it is
 * usually desirable to use LNG over base fuel.
 * 
 * @author Simon Goodall
 * 
 * @param <T>
 *            Sequence element type
 */
public interface ILNGVoyageCalculator<T> {

	/**
	 * Calculate the fuel requirements, speed and times for the port to port
	 * journey options defined in {@link VoyageOptions} and store the results
	 * in {@link IVoyageDetails}.
	 * 
	 * @param options
	 * @param output
	 */
	void calculateVoyageFuelRequirements(VoyageOptions options,
			VoyageDetails<T> output);

	/**
	 * Given an alternating sequence of {@link IPortDetails} and
	 * {@link IVoyageDetails}, populate a {@link VoyagePlan} object with the
	 * group voyage details. E.g. calculate load and discharge quantities.
	 * 
	 * @param voyagePlan
	 * @param vessel
	 * @param sequence
	 */
	void calculateVoyagePlan(VoyagePlan voyagePlan, IVessel vessel,
			Object... sequence);

}
