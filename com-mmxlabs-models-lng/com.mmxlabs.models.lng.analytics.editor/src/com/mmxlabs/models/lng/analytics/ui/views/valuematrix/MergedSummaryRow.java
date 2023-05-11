/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.ui.views.valuematrix;

import org.eclipse.jdt.annotation.NonNull;

public class MergedSummaryRow extends AbstractUnitSummaryRow {

	private final double value;
	private final boolean highlight;

	public MergedSummaryRow(@NonNull final String rowHeader, @NonNull final String units, final double value, final boolean highlight) {
		super(rowHeader, units);
		this.value = value;
		this.highlight = highlight;
	}

	public double getValue() {
		return this.value;
	}

	public boolean shouldHighlight() {
		return highlight;
	}
}
