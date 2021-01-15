/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.exposures.ExposuresLookupData;
import com.mmxlabs.optimiser.core.scenario.IDataComponentProvider;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;

/**
 * {@link IDataComponentProvider} interface to provide {@link IPortSlot} information for the given sequence elements.
 * 
 * @author FM
 */
public interface IExposureDataProviderEditor extends IExposureDataProvider {

	/**
	 * Records the price expression for the given portSlot
	 * 
	 * @param portSlot
	 * @return
	 */
	public void addPriceExpressionForPortSLot(@NonNull IPortSlot portSlot, String priceExpression);
	
	public void addLookupData(@NonNull ExposuresLookupData lookupData);
}
