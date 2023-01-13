/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.fleet.extpoint;

import static org.ops4j.peaberry.Peaberry.service;
import static org.ops4j.peaberry.util.TypeLiterals.iterable;

import com.google.inject.AbstractModule;
import com.mmxlabs.lingo.reports.views.fleet.ConfigurableVesselSummaryReport;
import com.mmxlabs.lingo.reports.views.fleet.VesselSummaryReportBuilder;

/**
 * A module to define extension points and other injectables for {@link ConfigurableVesselSummaryReport} instances.
 * 
 * @author Simon Goodall
 * 
 */
public class FleetBasedReportModule extends AbstractModule {

	@Override
	protected void configure() {

		bind(VesselSummaryReportBuilder.class);

		// Extension points
		bind(iterable(IFleetBasedColumnFactoryExtension.class)).toProvider(service(IFleetBasedColumnFactoryExtension.class).multiple());
		bind(iterable(IFleetBasedColumnExtension.class)).toProvider(service(IFleetBasedColumnExtension.class).multiple());
		bind(iterable(IFleetBasedReportInitialStateExtension.class)).toProvider(service(IFleetBasedReportInitialStateExtension.class).multiple());
	}
}
