/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.components;

import com.google.inject.AbstractModule;
import com.mmxlabs.lingo.reports.views.portrotation.extpoint.PortRotationBasedReportModule;
import com.mmxlabs.lingo.reports.views.schedule.extpoint.ScheduleBasedReportModule;

public class ReportComponentModule extends AbstractModule {

	@Override
	protected void configure() {

		install(new ScheduleBasedReportModule());
		install(new PortRotationBasedReportModule());
	}
}
