/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.util;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.pricing.util.ModelMarketCurveProvider;

public interface IExposuresCustomiser {

	/**
	 * For slots with complex contracts we may still be able to provide a simple price expression for the exposure calculation. If so return it, otherwise return null.
	 * 
	 * @param slot
	 * @return
	 */
	@Nullable
	String provideExposedPriceExpression(@NonNull Slot<?> slot);
	
	@Nullable
	String provideExposedPriceExpression(@NonNull Slot<?> slot, final ModelMarketCurveProvider mmCurveProvider);
	
	Slot<?> getExposed(final Slot<?> slot);
}
