/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import com.mmxlabs.common.paperdeals.PaperDealsLookupData;
import com.mmxlabs.optimiser.core.scenario.IDataComponentProvider;

/**
 * {@link IDataComponentProvider} interface to provide Calendar, Instrument and PaperDeals data information.
 * 
 * @author FM
 */
public interface IPaperDealDataProvider extends IDataComponentProvider {
	
	/**
	 * Returns PaperDealsLookupData to search for a Holiday and Pricing calendars
	 * @return
	 */
	public PaperDealsLookupData getPaperDealsLookupData();
}
