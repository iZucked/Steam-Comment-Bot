/**
 * Copyright (C) Minimaxlabs, 2010
 * All rights reserved.
 */

package com.mmxlabs.scheduler.optimiser.constraints.impl;

import com.mmxlabs.optimiser.core.constraints.IConstraintChecker;
import com.mmxlabs.optimiser.core.constraints.IConstraintCheckerFactory;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.scheduler.optimiser.providers.IPortTypeProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;

/**
 * {@link IConstraintCheckerFactory} implementation to create
 * {@link PortTypeConstraintChecker} instances.
 * 
 * @author Simon Goodall
 * 
 */
public final class PortTypeConstraintCheckerFactory implements
		IConstraintCheckerFactory {

	public static final String NAME = "PortTypeConstraintChecker";

	private final String key;

	private String vesselKey;

	/**
	 * Constructor taking the keys to use in {@link IOptimisationData} to find
	 * the {@link IPortTypeProvider} and {@link IVesselProvider}.
	 * 
	 * @param key
	 */
	public PortTypeConstraintCheckerFactory(final String key,
			final String vesselKey) {
		this.key = key;
		this.vesselKey = vesselKey;
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public <T> IConstraintChecker<T> instantiate() {
		return new PortTypeConstraintChecker<T>(NAME, key, vesselKey);
	}
}
