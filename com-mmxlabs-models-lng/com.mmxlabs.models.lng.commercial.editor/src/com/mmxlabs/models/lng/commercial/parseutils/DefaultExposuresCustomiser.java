/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.commercial.parseutils;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.schedule.SlotAllocation;

public class DefaultExposuresCustomiser implements IExposuresCustomiser{

	@Override
	public @Nullable String provideExposedPriceExpression(@NonNull Slot slot, SlotAllocation slotAllocation) {
		return null;
	}

	@Override
	public SlotAllocation getExposed(final SlotAllocation slotAllocation) {
		return slotAllocation;
	}

}
