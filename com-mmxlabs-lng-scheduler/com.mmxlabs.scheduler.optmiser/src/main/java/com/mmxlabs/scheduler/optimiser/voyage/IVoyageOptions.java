package com.mmxlabs.scheduler.optimiser.voyage;

import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.VesselState;

/**
 * Data structure defining a set of options to use in a
 * {@link IVoyageCalculator} when calculating voyages.
 * 
 * @author Simon Goodall
 * 
 */
public interface IVoyageOptions {

	/**
	 * The vessel assigned to this voyage
	 * 
	 * @return
	 */
	IVessel getVessel();

	/**
	 * The originating port slot
	 * 
	 * @return
	 */
	IPortSlot getFromPortSlot();

	/**
	 * The destination port slot.
	 * 
	 * @return
	 */
	IPortSlot getToPortSlot();

/**
	 * Time allocated to this journey. This may be exceeded should then vessel
	 * be unable to travel fast enough. Check the sum of {@link IVoyageDetails#getTravelTime()} and {@link IVoyageDetails#getIdleTime());
	 * 
	 * @return
	 */
	int getAvailableTime();

	/**
	 * Returns the state of the vessel for this voyage. I.e. laden or ballast
	 * 
	 * @return
	 */
	VesselState getVesselState();

	/**
	 * Flag indicating that NBO should be used for travelling.
	 * 
	 * @return
	 */
	boolean useNBOForTravel();

	/**
	 * Flag indicating that NBO should be used for idling. If true, then
	 * typically {@link #useNBOForTravel()} should also be true.
	 * 
	 * @return
	 */
	boolean useNBOForIdle();

	/**
	 * Flag indicating that the supplemental fuel should be FBO. If true, then
	 * {@link #useNBOForTravel()} should also be true.
	 * 
	 * @return
	 */
	boolean useFBOForSupplement();

	/**
	 * Returns the route option selected for this voyage.
	 * 
	 * @return
	 */
	String getRoute();

	/**
	 * Returns the distance for the selected route
	 * 
	 * @return
	 */
	int getDistance();

	/**
	 * Returns the speed the vessel must travel at to use up NBO. TODO: Can we
	 * move this to the vessel, or does it even need to be calculated each time
	 * due to cargo CV?
	 * 
	 * @return
	 */
	int getNBOSpeed();
}
