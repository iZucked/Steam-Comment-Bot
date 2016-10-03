package com.mmxlabs.models.lng.commercial.parseutils;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.schedule.SlotAllocation;

public interface IExposuredExpressionProvider {

	/**
	 * For slots with complex contracts we may still be able to provide a simple price expression for the exposure calculation. If so return it, otherwise return null.
	 * 
	 * @param slot
	 * @return
	 */
	@Nullable
	String provideExposedPriceExpression(@NonNull Slot slot, @NonNull SlotAllocation slotAllocation);
}
