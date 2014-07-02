/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.voyage;

import java.util.List;

import com.mmxlabs.optimiser.core.IElementAnnotation;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

/**
 * A small class storing the port arrival time and visit duration for a {@link VoyagePlan}. This class may or may not include the times for the last element in the plan depending on how it is created.
 * For scheduling purposes the end element should be included. Often for pricing purposes the end element can be ignored.
 * 
 * Note, the order slots are added is important. They should be added in scheduled order. The calls to {@link #getFirstSlotTime()} and {@link #getFirstSlot()} are expected to be bound the first slot
 * added to the instance.
 * 
 * @author Simon Goodall
 * 
 */
public interface IPortTimesRecord extends IElementAnnotation {
	List<IPortSlot> getSlots();

	int getSlotTime(IPortSlot slot);

	int getSlotDuration(IPortSlot slot);

	/**
	 * Should be expected to do equivalent of "ptr.getSlotTime(ptr.getFirstSlot())"
	 * @return
	 */
	int getFirstSlotTime();

	IPortSlot getFirstSlot();
	
	IPortSlot getReturnSlot();
	
}
