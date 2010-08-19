package com.mmxlabs.scheduler.optimiser.constraints.impl;

import com.mmxlabs.optimiser.core.constraints.IConstraintChecker;
import com.mmxlabs.optimiser.core.constraints.IConstraintCheckerFactory;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.scheduler.optimiser.providers.IPortTypeProvider;

/**
 * {@link IConstraintCheckerFactory} implementation to create
 * {@link PortTypeConstraintChecker} instances.
 * 
 * @author Simon Goodall
 * 
 */
public final class PortTypeConstraintCheckerFactory implements
		IConstraintCheckerFactory {

	private static final String name = "PortTypeConstraintChecker";

	private final String key;

	/**
	 * Constructor taking the key to use in {@link IOptimisationData} to find
	 * the {@link IPortTypeProvider}.
	 * 
	 * @param key
	 */
	public PortTypeConstraintCheckerFactory(final String key) {
		this.key = key;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public <T> IConstraintChecker<T> instantiate() {
		return new PortTypeConstraintChecker<T>(name, key);
	}
}
