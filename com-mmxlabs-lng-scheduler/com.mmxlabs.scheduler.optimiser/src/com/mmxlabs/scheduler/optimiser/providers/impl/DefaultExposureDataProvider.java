/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers.impl;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.exposures.ExposuresLookupData;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.providers.IExposureDataProviderEditor;

public final class DefaultExposureDataProvider implements IExposureDataProviderEditor {
	
	private ExposuresLookupData lookupData;

	@NonNull
	private final Map<IPortSlot, String> elements = new HashMap<IPortSlot, String>();

	@Override
	public boolean hasPriceExpression(@NonNull IPortSlot portSlot) {
		return elements.containsKey(portSlot);
	}

	@Override
	public String getPriceExpression(@NonNull IPortSlot portSlot) {
		return elements.get(portSlot);
	}

	@Override
	public void addPriceExpressionForPortSLot(@NonNull IPortSlot portSlot, String priceExpression) {
		if (priceExpression != null && priceExpression.length() > 0) {
			elements.put(portSlot, priceExpression);
		}
	}
	
	@Override
	public void addLookupData(@NonNull ExposuresLookupData lookupData) {
		this.lookupData = lookupData;
	}

	@Override
	public ExposuresLookupData getExposuresLookupData() {
		assert lookupData != null;
		return lookupData;
	}
}
