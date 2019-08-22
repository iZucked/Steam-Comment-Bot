package com.mmxlabs.lngdataserver.integration.reports.longshort;

import com.mmxlabs.models.lng.cargo.Slot;

public class DefaultSlotFilter implements ISlotFilter {

	@Override
	public boolean includeSlot(final Slot<?> slot) {
		return true;
	}
}
