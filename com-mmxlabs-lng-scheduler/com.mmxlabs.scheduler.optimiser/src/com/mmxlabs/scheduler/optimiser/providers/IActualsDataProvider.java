package com.mmxlabs.scheduler.optimiser.providers;

import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.optimiser.core.scenario.IDataComponentProvider;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;

/**
 * A {@link IDataComponentProvider} for "Actual" data fed in from the user, rather than calculated by the model.
 * 
 * @author Simon Goodall
 * 
 */
public interface IActualsDataProvider extends IDataComponentProvider {

	/**
	 * Returns true if actuals data is present for the given slot. This implies all of the getter method has a valid actuals value.
	 * 
	 * @param slot
	 * @return
	 */
	boolean hasActuals(IPortSlot slot);

	/**
	 * Returns the actual arrival time at the port.
	 * 
	 * @param slot
	 * @return
	 */
	int getArrivalTime(IPortSlot slot);

	/**
	 * Returns the actual arrival time at the port as a {@link ITimeWindow}.
	 * 
	 * @param slot
	 * @return
	 */
	ITimeWindow getArrivalTimeWindow(IPortSlot slot);

	/**
	 * Return the actual duration of the visit at this port slot
	 * 
	 * @param slot
	 * @return
	 */
	int getVisitDuration(IPortSlot slot);

	/**
	 * Returns the actual CV for a load or discharge
	 * 
	 * @param slot
	 * @return
	 */
	int getCVValue(IPortSlot slot);

	/**
	 * Returns the actual port costs for the port slot
	 * 
	 * @param slot
	 * @return
	 */
	long getPortCosts(IPortSlot slot);

	/**
	 * Return the actual purchase or sales volume in m3 at this load or discharge
	 * 
	 * @param slot
	 * @return
	 */
	long getVolumeInM3(IPortSlot slot);

	/**
	 * Return the actual purchase or sales volume in mmBtu at this load or discharge
	 * 
	 * @param slot
	 * @return
	 */
	long getVolumeInMMBtu(IPortSlot slot);

	/**
	 * Gets the actual heel at the start of the event in m3 (currently only valid for ILoadOptions)
	 * 
	 * @param slot
	 * @return
	 */
	long getStartHeelInM3(IPortSlot slot);

	/**
	 * Returns the actual heel at the end of the event in m3 (currently only valid for IDischargeOptions)
	 * 
	 * @param slot
	 * @return
	 */
	long getEndHeelInM3(IPortSlot slot);

	/**
	 * Base fuel price per MT
	 */
	int getBaseFuelPrice(IPortSlot slot);

	/**
	 * Returns the actual base fuel used in the next voyage. This includes and load and discharge port fuel consumptions.
	 * 
	 * @param fromPortSlot
	 * @return
	 */
	long getNextVoyageBaseFuelConsumptionInMT(IPortSlot slot);

	int getLNGPricePerMMBTu(IPortSlot slot);

}
