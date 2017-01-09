/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.peaberry;

import org.ops4j.peaberry.Peaberry;
import org.ops4j.peaberry.util.TypeLiterals;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import com.mmxlabs.optimiser.core.constraints.IEvaluatedStateConstraintCheckerFactory;
import com.mmxlabs.scheduler.optimiser.constraints.impl.CapacityEvaluatedStateCheckerFactory;
import com.mmxlabs.scheduler.optimiser.constraints.impl.LatenessEvaluatedStateCheckerFactory;

/**
 * Guice module using Peaberry to register {@link IEvaluatedStateConstraintCheckerFactory} instances as services.
 * 
 * @author Simon Goodall
 * 
 */
public class EvaluatedStateConstraintCheckerServiceModule extends AbstractModule {
	@Override
	protected void configure() {

		bind(TypeLiterals.export(IEvaluatedStateConstraintCheckerFactory.class)).annotatedWith(Names.named(LatenessEvaluatedStateCheckerFactory.class.getCanonicalName()))
				.toProvider(Peaberry.service(new LatenessEvaluatedStateCheckerFactory()).export());

		bind(TypeLiterals.export(IEvaluatedStateConstraintCheckerFactory.class)).annotatedWith(Names.named(CapacityEvaluatedStateCheckerFactory.class.getCanonicalName()))
				.toProvider(Peaberry.service(new CapacityEvaluatedStateCheckerFactory()).export());
	}
}
