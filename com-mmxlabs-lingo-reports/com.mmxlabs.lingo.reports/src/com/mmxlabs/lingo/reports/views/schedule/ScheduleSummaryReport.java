package com.mmxlabs.lingo.reports.views.schedule;

import com.google.inject.Inject;

public class ScheduleSummaryReport extends AbstractConfigurableScheduleReportView {
	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "com.mmxlabs.shiplingo.platform.reports.views.SchedulePnLReport";

	@Inject
	public ScheduleSummaryReport(final ScheduleBasedReportBuilder builder) {
		super("com.mmxlabs.lingo.doc.Reports_ScheduleSummary", builder);
	}
}
