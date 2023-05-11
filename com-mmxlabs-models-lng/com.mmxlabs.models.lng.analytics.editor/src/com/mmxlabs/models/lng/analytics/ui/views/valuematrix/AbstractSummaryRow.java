/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.ui.views.valuematrix;

import org.eclipse.jdt.annotation.NonNull;

public abstract class AbstractSummaryRow implements ISummaryRow {

	@NonNull
	private final String rowHeader;

	protected AbstractSummaryRow(@NonNull final String rowHeader) {
		this.rowHeader = rowHeader;
	}

	@Override
	public String getRowHeader() {
		return this.rowHeader;
	}

}
