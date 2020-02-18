/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.exposures.ExposuresLookupData;
import com.mmxlabs.optimiser.core.scenario.IDataComponentProvider;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;

/**
 * {@link IDataComponentProvider} interface to provide {@link IPortSlot} information for the given sequence elements.
 * 
 * @author FM
 */
public interface IExposureDataProvider extends IDataComponentProvider {

	/**
	 * Returns the boolean flag for the given element.
	 * 
	 * @param portSlot
	 * @return
	 */
	boolean hasPriceExpression(@NonNull IPortSlot portSlot);
	
	/**
	 * Returns PriceExpression for the given element
	 * @param portSlot
	 * @return
	 */
	@Nullable String getPriceExpression(@NonNull IPortSlot portSlot);
	
	/**
	 * Returns entire exposures look-up data
	 * @return
	 */
	ExposuresLookupData getExposuresLookupData();
}
