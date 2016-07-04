/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.peaberry;

import org.ops4j.peaberry.Peaberry;
import org.ops4j.peaberry.util.TypeLiterals;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationProcessFactory;
import com.mmxlabs.scheduler.optimiser.evaluation.SchedulerEvaluationProcessFactory;

/**
 * Guice module using Peaberry to register {@link IEvaluationProcessFactory} instances as services.
 * 
 * @author Simon Goodall
 * 
 */
public class EvaluationProcessServiceModule extends AbstractModule {

	@Override
	protected void configure() {

		bind(TypeLiterals.export(IEvaluationProcessFactory.class)).annotatedWith(Names.named(SchedulerEvaluationProcessFactory.class.getCanonicalName())).toProvider(
				Peaberry.service(new SchedulerEvaluationProcessFactory()).export());
	}
}
