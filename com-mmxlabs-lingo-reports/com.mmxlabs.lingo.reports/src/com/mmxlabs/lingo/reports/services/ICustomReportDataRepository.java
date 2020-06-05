/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.services;

import java.io.IOException;
import java.util.List;

import com.mmxlabs.lingo.reports.customizable.CustomReportDefinition;

public interface ICustomReportDataRepository {
	boolean publishReport(final CustomReportDefinition reportDefinition) throws Exception;
	List<CustomReportDefinition> getTeamReports() throws IOException;
	void refresh() throws IOException;
	void removeReport(final CustomReportDefinition reportDefinition) throws IOException;
	void startListenForNewLocalVersions();
	void stopListeningForNewLocalVersions();
	void registerLocalVersionListener(final Runnable versionConsumer);
}
