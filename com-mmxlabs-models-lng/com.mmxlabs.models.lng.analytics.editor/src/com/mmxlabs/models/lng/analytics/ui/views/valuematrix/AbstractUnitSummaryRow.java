/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.ui.views.valuematrix;

import org.eclipse.jdt.annotation.NonNull;

public abstract class AbstractUnitSummaryRow extends AbstractSummaryRow implements IUnitSummaryRow {

	@NonNull
	private final String units;

	protected AbstractUnitSummaryRow(@NonNull final String rowHeader, @NonNull final String units) {
		super(rowHeader);
		this.units = units;
	}

	@Override
	public String getUnits() {
		return this.units;
	}

}
