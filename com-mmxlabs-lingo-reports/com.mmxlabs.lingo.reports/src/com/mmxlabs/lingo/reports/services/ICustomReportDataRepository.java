package com.mmxlabs.lingo.reports.services;

import java.io.IOException;
import java.util.List;

import com.mmxlabs.lingo.reports.views.schedule.ScheduleSummaryReportDefinition;

public interface ICustomReportDataRepository {
	boolean publishReport(final ScheduleSummaryReportDefinition reportDefinition) throws Exception;
	List<ScheduleSummaryReportDefinition> getTeamReports() throws IOException;
	void refresh() throws IOException;
	void removeReport(final ScheduleSummaryReportDefinition reportDefinition) throws IOException;
	void startListenForNewLocalVersions();
	void stopListeningForNewLocalVersions();
	void registerLocalVersionListener(final Runnable versionConsumer);
}
