/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.portrotation.extpoint;

import static org.ops4j.peaberry.Peaberry.service;
import static org.ops4j.peaberry.util.TypeLiterals.iterable;

import com.google.inject.AbstractModule;
import com.mmxlabs.lingo.reports.views.portrotation.PortRotationBasedReportBuilder;

/**
 * A module to define extension points and other injectables for PortRotationReportView instances.
 * 
 * @author Simon Goodall
 * 
 */
public class PortRotationBasedReportModule extends AbstractModule {

	@Override
	protected void configure() {

		bind(PortRotationBasedReportBuilder.class);

		// Extension points
		bind(iterable(IPortRotationBasedColumnFactoryExtension.class)).toProvider(service(IPortRotationBasedColumnFactoryExtension.class).multiple());
		bind(iterable(IPortRotationBasedColumnExtension.class)).toProvider(service(IPortRotationBasedColumnExtension.class).multiple());
		bind(iterable(IPortRotationBasedReportInitialStateExtension.class)).toProvider(service(IPortRotationBasedReportInitialStateExtension.class).multiple());
	}
}
