/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.ui.views.valuematrix;

import org.eclipse.jdt.annotation.NonNull;

public class SwapSummaryRow extends AbstractUnitSummaryRow {

	private final double swapValue;
	private final boolean highlight;
	
	public SwapSummaryRow(@NonNull final String rowHeader, @NonNull final String units, final double swapValue, final boolean highlight) {
		super(rowHeader, units);
		this.swapValue = swapValue;
		this.highlight = highlight;
	}

	public double getSwapValue() {
		return this.swapValue;
	}

	public boolean shouldHighlight() {
		return highlight;
	}
}
