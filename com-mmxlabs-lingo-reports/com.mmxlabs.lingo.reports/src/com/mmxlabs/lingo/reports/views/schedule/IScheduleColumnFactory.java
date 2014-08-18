package com.mmxlabs.lingo.reports.views.schedule;

import com.mmxlabs.lingo.reports.extensions.EMFReportColumnManager;

public interface IScheduleColumnFactory {

	void registerColumn(String columnID, EMFReportColumnManager manager, ScheduleBasedReportBuilder builder);
}
