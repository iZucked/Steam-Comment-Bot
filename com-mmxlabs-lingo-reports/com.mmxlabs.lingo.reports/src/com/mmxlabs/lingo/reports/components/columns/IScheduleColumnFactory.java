package com.mmxlabs.lingo.reports.components.columns;

import com.mmxlabs.lingo.reports.components.ScheduleBasedReportBuilder;
import com.mmxlabs.lingo.reports.extensions.EMFReportColumnManager;

public interface IScheduleColumnFactory {

	void registerColumn(String columnID, EMFReportColumnManager manager, ScheduleBasedReportBuilder builder);
}
