/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.voyage;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.google.common.collect.ImmutableList;
import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IRouteOptionBooking;
import com.mmxlabs.scheduler.optimiser.voyage.impl.AvailableRouteChoices;

/**
 * A similar class to {@link IPortTimesRecord}, created before arrival times
 * have been scheduled. TODO: Replace {@link IPortTimesRecord} with this
 * 
 * @author achurchill
 *
 */

public interface IPortTimeWindowsRecord {

	/**
	 * Returns all slots excluding the return slot
	 * 
	 * @return
	 */

	ImmutableList<IPortSlot> getSlots();

	@Nullable
	ITimeWindow getSlotFeasibleTimeWindow(IPortSlot slot);

	void setSlotFeasibleTimeWindow(IPortSlot slot, ITimeWindow timeWindow);

	int getSlotDuration(IPortSlot slot);

	void setSlotDuration(IPortSlot slot, int duration);

	int getSlotExtraIdleTime(IPortSlot slot, ExplicitIdleTime type);
	
	int getSlotTotalExtraIdleTime(IPortSlot slot);

	void setSlotExtraIdleTime(IPortSlot slot, ExplicitIdleTime type, int extraIdleTime);

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
	@Nullable
	IPortSlot getReturnSlot();

	void setSlot(IPortSlot slot, ITimeWindow timeWindow, int duration, int index);

	void setReturnSlot(IPortSlot slot, ITimeWindow timeWindow, int duration, int index);

	int getIndex(IPortSlot slot);

	@NonNull AvailableRouteChoices getSlotNextVoyageOptions(IPortSlot slot);

	void setSlotNextVoyageOptions(IPortSlot slot, AvailableRouteChoices nextVoyageRoute);

	void setRouteOptionBooking(IPortSlot slot, @Nullable IRouteOptionBooking routeOptionSlot);

	void setSlotAdditionalPanamaDetails(IPortSlot slot, boolean isConstrainedPanamaJourney, int additionalPanamaIdleTimeInHours);

	@Nullable
	IRouteOptionBooking getRouteOptionBooking(IPortSlot slot);

	boolean getSlotIsNextVoyageConstrainedPanama(IPortSlot slot);

	int getSlotAdditionalPanamaIdleHours(IPortSlot slot);
}
