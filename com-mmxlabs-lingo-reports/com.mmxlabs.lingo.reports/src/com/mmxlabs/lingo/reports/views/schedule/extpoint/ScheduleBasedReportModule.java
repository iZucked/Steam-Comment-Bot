/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.schedule.extpoint;

import static org.ops4j.peaberry.Peaberry.service;
import static org.ops4j.peaberry.util.TypeLiterals.iterable;

import com.google.inject.AbstractModule;
import com.mmxlabs.lingo.reports.utils.ICustomRelatedSlotHandlerExtension;
import com.mmxlabs.lingo.reports.views.schedule.ScheduleBasedReportBuilder;

/**
 * A module to define extension points and other injectables for ConfigurableScheduleReportView instances.
 * 
 * @author Simon Goodall
 * 
 */
public class ScheduleBasedReportModule extends AbstractModule {

	@Override
	protected void configure() {

		bind(ScheduleBasedReportBuilder.class);

		// Extension points
		bind(iterable(ICustomRelatedSlotHandlerExtension.class)).toProvider(service(ICustomRelatedSlotHandlerExtension.class).multiple());
		bind(iterable(IScheduleBasedColumnFactoryExtension.class)).toProvider(service(IScheduleBasedColumnFactoryExtension.class).multiple());
		bind(iterable(IScheduleBasedColumnExtension.class)).toProvider(service(IScheduleBasedColumnExtension.class).multiple());
		bind(iterable(IScheduleBasedReportInitialStateExtension.class)).toProvider(service(IScheduleBasedReportInitialStateExtension.class).multiple());
	}
}
