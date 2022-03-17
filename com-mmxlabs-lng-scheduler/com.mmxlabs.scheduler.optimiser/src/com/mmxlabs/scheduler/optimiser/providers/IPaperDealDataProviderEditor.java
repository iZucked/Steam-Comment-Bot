/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.paperdeals.PaperDealsLookupData;
import com.mmxlabs.optimiser.core.scenario.IDataComponentProvider;

/**
 * {@link IDataComponentProvider} interface to provide {@link PaperDealsLookupData} information for the given sequence elements.
 * 
 * @author FM
 */
public interface IPaperDealDataProviderEditor extends IPaperDealDataProvider {
	
	/**
	 * Records PaperDealsLookupData
	 * @param lookupData
	 */
	public void addLookupData(@NonNull PaperDealsLookupData lookupData);
}
