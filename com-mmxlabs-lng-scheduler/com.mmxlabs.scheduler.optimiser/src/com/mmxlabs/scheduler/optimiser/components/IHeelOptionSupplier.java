/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components;

import org.eclipse.jdt.annotation.NonNull;

/**
 * Stores data which a slot with heel options should provide.
 * 
 * @author hinton
 * 
 */
public interface IHeelOptionSupplier {
	@NonNull
	IHeelPriceCalculator getHeelPriceCalculator();

	long getMinimumHeelAvailableInM3();

	long getMaximumHeelAvailableInM3();

	int getHeelCVValue();
}
