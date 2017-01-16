/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.utils;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.OpenSlotAllocation;
import com.mmxlabs.models.lng.schedule.Schedule;

/**
 * Interface defining a way to customise the related slot collection. This allows slots to be grouped together which are not part of the same cargo.
 * 
 * @author Simon Goodall
 * 
 */
public interface ICustomRelatedSlotHandler {

	void addRelatedSlots(@NonNull RelatedSlotAllocations relatedSlotAllocations, @NonNull Schedule schedule, @NonNull CargoAllocation cargoAllocation);

	void addRelatedSlots(@NonNull RelatedSlotAllocations relatedSlotAllocations, @NonNull Schedule schedule, @NonNull OpenSlotAllocation openSlotAllocation);
}
