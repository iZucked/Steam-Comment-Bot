/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.standard.pnlcalcs;

import java.util.Collection;

import org.eclipse.jdt.annotation.Nullable;

public interface IPNLCalcsRowFactory {

	default Collection<PNLCalcsReportRow> createRows() {
		return createRows(null);
	}

	Collection<PNLCalcsReportRow> createRows(@Nullable Collection<Object> targets);
}
