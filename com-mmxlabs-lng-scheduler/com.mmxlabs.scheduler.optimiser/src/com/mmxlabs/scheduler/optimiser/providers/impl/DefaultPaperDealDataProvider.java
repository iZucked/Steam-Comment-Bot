/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers.impl;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.paperdeals.PaperDealsLookupData;
import com.mmxlabs.scheduler.optimiser.providers.IPaperDealDataProviderEditor;

public final class DefaultPaperDealDataProvider implements IPaperDealDataProviderEditor {
	
	private PaperDealsLookupData lookupData;

	@Override
	public PaperDealsLookupData getPaperDealsLookupData() {
		assert lookupData != null;
		return lookupData;
	}

	@Override
	public void addLookupData(@NonNull PaperDealsLookupData lookupData) {
		this.lookupData = lookupData;
	}
}
