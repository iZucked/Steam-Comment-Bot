/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.reports.longshort;

import com.mmxlabs.models.lng.cargo.Slot;

public interface ISlotFilter {
	boolean includeSlot(final Slot<?> slot);
}
