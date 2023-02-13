package com.mmxlabs.models.lng.analytics.ui.views.valuematrix;

import org.eclipse.jdt.annotation.NonNull;

public class CombinedSummaryRow extends AbstractUnitSummaryRow {

	private final double baseValue;
	private final double swapValue;
	private final boolean highlightBase;
	private final boolean highlightSwap;

	public CombinedSummaryRow(@NonNull final String rowHeader, @NonNull final String units, final double baseValue, final double swapValue, final boolean highlightBase, final boolean highlightSwap) {
		super(rowHeader, units);
		this.baseValue = baseValue;
		this.swapValue = swapValue;
		this.highlightBase = highlightBase;
		this.highlightSwap = highlightSwap;
	}

	public double getBaseValue() {
		return this.baseValue;
	}

	public double getSwapValue() {
		return this.swapValue;
	}

	public boolean shouldHighlightBase() {
		return this.highlightBase;
	}

	public boolean shouldHighlightSwap() {
		return this.highlightSwap;
	}

}
