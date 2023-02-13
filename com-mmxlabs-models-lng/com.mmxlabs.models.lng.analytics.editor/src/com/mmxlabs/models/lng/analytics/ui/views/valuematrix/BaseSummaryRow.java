package com.mmxlabs.models.lng.analytics.ui.views.valuematrix;

import org.eclipse.jdt.annotation.NonNull;

public class BaseSummaryRow extends AbstractUnitSummaryRow {

	private final double baseValue;
	private final boolean highlight;

	public BaseSummaryRow(@NonNull final String rowHeader, @NonNull final String units, final double baseValue, final boolean highlight) {
		super(rowHeader, units);
		this.baseValue = baseValue;
		this.highlight = highlight;
	}

	public double getBaseValue() {
		return this.baseValue;
	}

	public boolean shouldHighlight() {
		return this.highlight;
	}
}
