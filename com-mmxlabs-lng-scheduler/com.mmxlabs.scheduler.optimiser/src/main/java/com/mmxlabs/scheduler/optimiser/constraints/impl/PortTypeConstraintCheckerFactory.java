/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.constraints.impl;

import com.mmxlabs.optimiser.core.constraints.IConstraintChecker;
import com.mmxlabs.optimiser.core.constraints.IConstraintCheckerFactory;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.scheduler.optimiser.providers.IPortTypeProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;

/**
 * {@link IConstraintCheckerFactory} implementation to create {@link PortTypeConstraintChecker} instances.
 * 
 * @author Simon Goodall
 * 
 */
public final class PortTypeConstraintCheckerFactory implements IConstraintCheckerFactory {

	public static final String NAME = "PortTypeConstraintChecker";

	private final String portTypeKey;
	private final String portSlotKey;

	private final String vesselKey;

	/**
	 * Constructor taking the keys to use in {@link IOptimisationData} to find the {@link IPortTypeProvider} and {@link IVesselProvider}.
	 * 
	 * @param key
	 */
	public PortTypeConstraintCheckerFactory(final String portTypeKey, final String portSlotKey, final String vesselKey) {
		this.portSlotKey = portSlotKey;
		this.portTypeKey = portTypeKey;
		this.vesselKey = vesselKey;
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public IConstraintChecker instantiate() {
		return new PortTypeConstraintChecker(NAME, portTypeKey, vesselKey, portSlotKey);
	}
}
