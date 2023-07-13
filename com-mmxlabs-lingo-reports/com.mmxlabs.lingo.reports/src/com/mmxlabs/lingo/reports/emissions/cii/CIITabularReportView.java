package com.mmxlabs.lingo.reports.emissions.cii;

import com.mmxlabs.lingo.reports.components.AbstractSimpleTabularReportTransformer;
import com.mmxlabs.lingo.reports.components.SimpleTabularReportContentProvider;
import com.mmxlabs.lingo.reports.views.standard.SimpleTabularReportView;

public class CIITabularReportView extends SimpleTabularReportView<CIIGradesData> {

	protected CIITabularReportView() {
		super("CII_Grades_ReportView");
	}

	@Override
	protected SimpleTabularReportContentProvider createContentProvider() {
		return new SimpleTabularReportContentProvider();
	}

	@Override
	protected AbstractSimpleTabularReportTransformer<CIIGradesData> createTransformer() {
		return new CIIReportTransformer();
	}
}
