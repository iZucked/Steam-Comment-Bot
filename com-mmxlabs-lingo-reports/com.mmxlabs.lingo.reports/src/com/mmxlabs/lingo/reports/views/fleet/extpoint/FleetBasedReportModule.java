/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.fleet.extpoint;

import static org.ops4j.peaberry.Peaberry.service;
import static org.ops4j.peaberry.util.TypeLiterals.iterable;

import com.google.inject.AbstractModule;
import com.mmxlabs.lingo.reports.views.fleet.ConfigurableFleetReportView;
import com.mmxlabs.lingo.reports.views.fleet.FleetBasedReportBuilder;

/**
 * A module to define extension points and other injectables for {@link ConfigurableFleetReportView} instances.
 * 
 * @author Simon Goodall
 * 
 */
public class FleetBasedReportModule extends AbstractModule {

	@Override
	protected void configure() {

		bind(FleetBasedReportBuilder.class);

		// Extension points
		bind(iterable(IFleetBasedColumnFactoryExtension.class)).toProvider(service(IFleetBasedColumnFactoryExtension.class).multiple());
		bind(iterable(IFleetBasedColumnExtension.class)).toProvider(service(IFleetBasedColumnExtension.class).multiple());
		bind(iterable(IFleetBasedReportInitialStateExtension.class)).toProvider(service(IFleetBasedReportInitialStateExtension.class).multiple());
	}
}
