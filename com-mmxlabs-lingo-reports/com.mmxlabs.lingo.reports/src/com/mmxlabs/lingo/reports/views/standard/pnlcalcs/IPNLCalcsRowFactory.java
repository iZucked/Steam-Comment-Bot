/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.standard.pnlcalcs;

import java.util.Collection;

import org.eclipse.jdt.annotation.Nullable;

public interface IPNLCalcsRowFactory {

	default Collection<PNLCalcsReportRow> createRows(PNLCalcsOptions options) {
		return createRows(options, null);
	}

	Collection<PNLCalcsReportRow> createRows(PNLCalcsOptions options, @Nullable Collection<Object> targets);
}
