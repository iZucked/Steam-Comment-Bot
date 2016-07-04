/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.scheduleview.internal;

import static org.ops4j.peaberry.Peaberry.service;
import static org.ops4j.peaberry.util.TypeLiterals.iterable;

import com.google.inject.AbstractModule;
import com.mmxlabs.lingo.reports.scheduleview.views.colourschemes.ISchedulerViewColourSchemeExtension;

/**
 * An activation module.
 * 
 * @author Simon Goodall
 * 
 */
public class ActivatorModule extends AbstractModule {

	@Override
	protected void configure() {

		bind(iterable(ISchedulerViewColourSchemeExtension.class)).toProvider(service(ISchedulerViewColourSchemeExtension.class).multiple());
	}
}
