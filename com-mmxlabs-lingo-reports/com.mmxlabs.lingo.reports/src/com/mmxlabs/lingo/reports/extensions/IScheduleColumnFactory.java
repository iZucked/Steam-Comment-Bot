package com.mmxlabs.lingo.reports.extensions;

import com.mmxlabs.lingo.reports.components.ScheduleBasedReportBuilder;

public interface IScheduleColumnFactory {

	void registerColumn(String columnID, EMFReportColumnManager manager, ScheduleBasedReportBuilder builder);
}
