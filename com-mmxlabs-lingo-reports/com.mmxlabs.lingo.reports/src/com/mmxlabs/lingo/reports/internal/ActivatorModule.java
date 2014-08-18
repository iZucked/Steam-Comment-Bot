/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.internal;

import static org.ops4j.peaberry.Peaberry.service;
import static org.ops4j.peaberry.util.TypeLiterals.iterable;

import com.google.inject.AbstractModule;
import com.mmxlabs.lingo.reports.extensions.IScheduleBasedColumnExtension;
import com.mmxlabs.lingo.reports.extensions.IScheduleBasedColumnFactoryExtension;
import com.mmxlabs.lingo.reports.extensions.IScheduleBasedReportInitialStateExtension;

/**
 * An activation module.
 * 
 * @author Simon Goodall
 * 
 */
public class ActivatorModule extends AbstractModule {

	@Override
	protected void configure() {

		bind(iterable(IScheduleBasedColumnFactoryExtension.class)).toProvider(service(IScheduleBasedColumnFactoryExtension.class).multiple());
		bind(iterable(IScheduleBasedColumnExtension.class)).toProvider(service(IScheduleBasedColumnExtension.class).multiple());
		bind(iterable(IScheduleBasedReportInitialStateExtension.class)).toProvider(service(IScheduleBasedReportInitialStateExtension.class).multiple());
	}
}
