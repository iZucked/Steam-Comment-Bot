package com.mmxlabs.lingo.reports.utils;

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

	void addRelatedSlots(RelatedSlotAllocations relatedSlotAllocations, Schedule schedule, CargoAllocation cargoAllocation);

	void addRelatedSlots(RelatedSlotAllocations relatedSlotAllocations, Schedule schedule, OpenSlotAllocation openSlotAllocation);
}
