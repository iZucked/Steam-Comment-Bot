/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.components;

import static org.ops4j.peaberry.Peaberry.service;

import com.google.inject.AbstractModule;
import com.mmxlabs.lingo.reports.services.ScenarioComparisonService;
import com.mmxlabs.lingo.reports.services.SelectedScenariosService;
import com.mmxlabs.lingo.reports.views.fleet.extpoint.FleetBasedReportModule;
import com.mmxlabs.lingo.reports.views.portrotation.extpoint.PortRotationBasedReportModule;
import com.mmxlabs.lingo.reports.views.schedule.extpoint.ScheduleBasedReportModule;

public class ReportComponentModule extends AbstractModule {

	@Override
	protected void configure() {

		bind(SelectedScenariosService.class).toProvider(service(SelectedScenariosService.class).single().direct());
		bind(ScenarioComparisonService.class).toProvider(service(ScenarioComparisonService.class).single().direct());

		install(new ScheduleBasedReportModule());
		install(new FleetBasedReportModule());
		install(new PortRotationBasedReportModule());
	}
}
