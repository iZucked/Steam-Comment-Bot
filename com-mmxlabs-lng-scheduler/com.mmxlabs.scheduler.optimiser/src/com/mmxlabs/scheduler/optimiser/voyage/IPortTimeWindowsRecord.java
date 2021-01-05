/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.voyage;

import java.util.List;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.optimiser.core.IElementAnnotation;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IRouteOptionBooking;
import com.mmxlabs.scheduler.optimiser.voyage.impl.AvailableRouteChoices;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PanamaPeriod;

/**
 * A similar class to {@link IPortTimesRecord}, created before arrival times have been scheduled. TODO: Replace {@link IPortTimesRecord} with this
 * 
 * @author achurchill
 *
 */
@NonNullByDefault
public interface IPortTimeWindowsRecord extends IElementAnnotation {

	/**
	 * Returns all slots excluding the return slot
	 * 
	 * @return
	 */

	List<IPortSlot> getSlots();

	ITimeWindow getSlotFeasibleTimeWindow(IPortSlot slot);

	void setSlotFeasibleTimeWindow(IPortSlot slot, ITimeWindow timeWindow);

	int getSlotDuration(IPortSlot slot);

	void setSlotDuration(IPortSlot slot, int duration);

	int getSlotExtraIdleTime(IPortSlot slot);

	void setSlotExtraIdleTime(IPortSlot slot, int extraIdleTime);

	/**
	 * Should be expected to do equivalent of "ptr.getSlotTime(ptr.getFirstSlot())"
	 * 
	 * @return
	 */
	ITimeWindow getFirstSlotFeasibleTimeWindow();

	IPortSlot getFirstSlot();

	/**
	 * Returns the final slot in the slots list
	 * 
	 * @return
	 */
	IPortSlot getReturnSlot();

	void setSlot(IPortSlot slot, ITimeWindow timeWindow, int duration, int index);

	void setReturnSlot(IPortSlot slot, ITimeWindow timeWindow, int duration, int index);

	int getIndex(IPortSlot slot);

	AvailableRouteChoices getSlotNextVoyageOptions(IPortSlot slot);

	void setSlotNextVoyageOptions(IPortSlot slot, AvailableRouteChoices nextVoyageRoute, PanamaPeriod panamaPeriod);

	void setRouteOptionBooking(IPortSlot slot, IRouteOptionBooking routeOptionSlot);

	void setSlotAdditionalPanamaDetails(IPortSlot slot, boolean isConstrainedPanamaJourney, int additionalPanamaIdleTimeInHours);

	IRouteOptionBooking getRouteOptionBooking(IPortSlot slot);

	PanamaPeriod getSlotNextVoyagePanamaPeriod(IPortSlot slot);

	boolean getSlotIsNextVoyageConstrainedPanama(IPortSlot slot);

	int getSlotAdditionalPanamaIdleHours(IPortSlot slot);

	void setSlotNextVoyagePanamaPeriod(IPortSlot slot, PanamaPeriod panamaPeriod);

}
