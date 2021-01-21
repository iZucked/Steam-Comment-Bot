/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.standard.econs;

import java.util.Collection;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

public interface IEconsRowFactory {

	enum RowType {
		COST, // Lower values are better
		REVENUE, // Higher values are better
		OTHER // Other data types
	}

	default Collection<CargoEconsReportRow> createRows(@NonNull EconsOptions options) {
		return createRows(options, null);
	}

	Collection<CargoEconsReportRow> createRows(@NonNull EconsOptions options, @Nullable Collection<Object> targets);
}
