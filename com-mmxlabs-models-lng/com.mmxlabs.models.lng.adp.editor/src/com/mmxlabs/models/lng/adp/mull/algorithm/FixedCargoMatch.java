package com.mmxlabs.models.lng.adp.mull.algorithm;

import java.time.LocalDateTime;
import java.time.LocalTime;

import com.mmxlabs.models.lng.adp.MullCargoWrapper;
import com.mmxlabs.models.lng.adp.mull.container.IAllocationTracker;
import com.mmxlabs.models.lng.adp.mull.container.IMudContainer;
import com.mmxlabs.models.lng.adp.mull.container.IMullContainer;

public record FixedCargoMatch(MullCargoWrapper mullCargoWrapper, IMullContainer mullContainer, IMudContainer mudContainer, IAllocationTracker allocationTracker) {
	public LocalDateTime getLiftingTime() {
		return LocalDateTime.of(mullCargoWrapper.getLoadSlot().getWindowStart(), LocalTime.of(mullCargoWrapper.getLoadSlot().getWindowStartTime(), 0));
	}

	
}
