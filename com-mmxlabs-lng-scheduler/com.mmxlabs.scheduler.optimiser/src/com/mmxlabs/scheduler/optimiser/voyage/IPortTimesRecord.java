/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.voyage;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.google.common.collect.ImmutableList;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IRouteOptionBooking;
import com.mmxlabs.scheduler.optimiser.voyage.impl.AvailableRouteChoices;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

/**
 * A small class storing the port arrival time and visit duration for a
 * {@link VoyagePlan}. This class may or may not include the times for the last
 * element in the plan depending on how it is created. For scheduling purposes
 * the end element should be included. Often for pricing purposes the end
 * element can be ignored.
 * 
 * Note, the order slots are added is important. They should be added in
 * scheduled order. The calls to {@link #getFirstSlotTime()} and
 * {@link #getFirstSlot()} are expected to be bound the first slot added to the
 * instance.
 * 
 * @author Simon Goodall
 * 
 */
@NonNullByDefault
public interface IPortTimesRecord {

	/**
	 * Returns all slots excluding the return slot
	 * 
	 * @return
	 */

	ImmutableList<IPortSlot> getSlots();

	int getSlotTime(IPortSlot slot);

	int getSlotExtraIdleTime(IPortSlot slot, ExplicitIdleTime type);

	int getSlotTotalExtraIdleTime(IPortSlot slot);

	void setSlotTime(IPortSlot slot, int time);

	void setSlotExtraIdleTime(IPortSlot slot, ExplicitIdleTime type, int time);

	int getSlotDuration(IPortSlot slot);

	void setSlotDuration(IPortSlot slot, int duration);

	/**
	 * Should be expected to do equivalent of "ptr.getSlotTime(ptr.getFirstSlot())"
	 * 
	 * @return
	 */
	int getFirstSlotTime();

	IPortSlot getFirstSlot();

	/**
	 * Returns the final slot in the slots list
	 * 
	 * @return
	 */
	@Nullable
	IPortSlot getReturnSlot();

	@Nullable
	IRouteOptionBooking getRouteOptionBooking(IPortSlot slot);

	void setRouteOptionBooking(IPortSlot slot, @Nullable IRouteOptionBooking routeOptionSlot);

	void setSlotNextVoyageOptions(IPortSlot slot, AvailableRouteChoices nextVoyageRoute);

	AvailableRouteChoices getSlotNextVoyageOptions(IPortSlot slot);

	int getSlotAdditionaPanamaIdleHours(IPortSlot slot);

	int getSlotMaxAdditionaPanamaIdleHours(IPortSlot slot);

	void setSlotMaxAvailablePanamaIdleHours(IPortSlot from, int maxIdleTimeAvailable);

	void setSlotAdditionalPanamaIdleHours(IPortSlot from, int additionalPanamaTime);
}
