/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.constraints.impl;

import com.mmxlabs.optimiser.core.constraints.IConstraintChecker;
import com.mmxlabs.optimiser.core.constraints.IConstraintCheckerFactory;

public class PortExclusionConstraintCheckerFactory implements IConstraintCheckerFactory {

	public static final String NAME = "PortExclusionConstraintChecker";
	private final String exclusionProviderKey;
	private final String vesselProviderKey;
	private final String portProviderKey;

	public PortExclusionConstraintCheckerFactory(final String exclusionProviderKey, final String vesselProviderKey, final String portProviderKey) {
		super();
		this.exclusionProviderKey = exclusionProviderKey;
		this.vesselProviderKey = vesselProviderKey;
		this.portProviderKey = portProviderKey;
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public IConstraintChecker instantiate() {
		return new PortExclusionConstraintChecker(NAME, exclusionProviderKey, vesselProviderKey, portProviderKey);
	}
}
