/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.constraints.impl;

import com.mmxlabs.optimiser.core.constraints.IConstraintChecker;
import com.mmxlabs.optimiser.core.constraints.IConstraintCheckerFactory;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;

/**
 * {@link IConstraintCheckerFactory} implementation to create {@link VirtualVesselConstraintChecker} instances.
 * 
 * @author Tom Hinton
 * 
 */
public final class VirtualVesselConstraintCheckerFactory implements IConstraintCheckerFactory {

	public static final String NAME = "VirtualVesselConstraintChecker";

	private final String vesselProviderKey;
	private final String portSlotProviderKey;

	/**
	 * Constructor taking the keys to use in {@link IOptimisationData} to find the {@link IVesselProvider}.
	 * 
	 * @param portSlotProviderKey
	 * 
	 * @param key
	 */
	public VirtualVesselConstraintCheckerFactory(final String vesselKey, final String portSlotProviderKey) {
		this.vesselProviderKey = vesselKey;
		this.portSlotProviderKey = portSlotProviderKey;
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public IConstraintChecker instantiate() {
		return new VirtualVesselConstraintChecker(NAME, vesselProviderKey, portSlotProviderKey);
	}
}
