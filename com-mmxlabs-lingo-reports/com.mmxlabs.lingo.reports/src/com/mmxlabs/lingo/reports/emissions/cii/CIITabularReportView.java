/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.emissions.cii;

import com.mmxlabs.lingo.reports.components.AbstractSimpleTabularReportTransformer;
import com.mmxlabs.lingo.reports.components.SimpleTabularReportContentProvider;
import com.mmxlabs.lingo.reports.views.standard.SimpleTabularReportView;

public class CIITabularReportView extends SimpleTabularReportView<CIIGradesData> {

	public CIITabularReportView() {
		super("com.mmxlabs.lingo.reports.emissions.cii.CIITabularReportView");
	}

	@Override
	protected SimpleTabularReportContentProvider createContentProvider() {
		return new SimpleTabularReportContentProvider();
	}

	@Override
	protected AbstractSimpleTabularReportTransformer<CIIGradesData> createTransformer() {
		return new CIIReportTransformer(pinImage);
	}
}
