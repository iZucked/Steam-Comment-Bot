/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.voyage;

import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.optimiser.core.IElementAnnotation;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IRouteOptionBooking;
import com.mmxlabs.scheduler.optimiser.voyage.impl.AvailableRouteChoices;

/**
 * A similar class to {@link IPortTimesRecord}, created before arrival times have been scheduled. TODO: Replace {@link IPortTimesRecord} with this
 * 
 * @author achurchill
 *
 */
public interface IPortTimeWindowsRecord extends IElementAnnotation {

	/**
	 * Returns all slots excluding the return slot
	 * 
	 * @return
	 */
	@NonNull
	List<@NonNull IPortSlot> getSlots();

	ITimeWindow getSlotFeasibleTimeWindow(@NonNull IPortSlot slot);

	void setSlotFeasibleTimeWindow(@NonNull IPortSlot slot, @NonNull ITimeWindow timeWindow);

	int getSlotDuration(@NonNull IPortSlot slot);

	void setSlotDuration(@NonNull IPortSlot slot, int duration);

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

	void setSlot(@NonNull IPortSlot slot, ITimeWindow timeWindow, int duration, int index);

	void setReturnSlot(@NonNull IPortSlot slot, ITimeWindow timeWindow, int duration, int index);

	int getIndex(@NonNull IPortSlot slot);

	IResource getResource();

	AvailableRouteChoices getSlotNextVoyageOptions(IPortSlot slot);

	void setSlotNextVoyageOptions(IPortSlot slot, AvailableRouteChoices nextVoyageRoute);

	void setRouteOptionBooking(IPortSlot slot, IRouteOptionBooking routeOptionSlot);

	IRouteOptionBooking getRouteOptionBooking(IPortSlot slot);
}
