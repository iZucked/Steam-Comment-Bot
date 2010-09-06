package com.mmxlabs.scheduler.optimiser.constraints.impl;

import com.mmxlabs.optimiser.core.constraints.IConstraintChecker;
import com.mmxlabs.optimiser.core.constraints.IConstraintCheckerFactory;

public class PortExclusionConstraintCheckerFactory implements
		IConstraintCheckerFactory {

	public static final String NAME = "PortExclusionConstraintChecker";
	private final String exclusionProviderKey;
	private final String vesselProviderKey;
	private final String portProviderKey;
	
	
	
	public PortExclusionConstraintCheckerFactory(String exclusionProviderKey,
			String vesselProviderKey, String portProviderKey) {
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
	public <T> IConstraintChecker<T> instantiate() {
		return new PortExclusionConstraintChecker<T>(NAME, exclusionProviderKey, vesselProviderKey, portProviderKey);
	}

}
