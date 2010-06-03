package com.mmxlabs.optimiser.constraints;

/**
 * {@link IConstraintCheckerFactory} implementation to construct
 * {@link ResourceAllocationConstraintChecker} instances.
 * 
 * @author Simon Goodall
 * 
 */
public final class ResourceAllocationConstraintCheckerFactory implements
		IConstraintCheckerFactory {

	public static final String NAME = "ResourceAllocationConstraintChecker";

	private final String key;

	public ResourceAllocationConstraintCheckerFactory(final String key) {
		this.key = key;
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public <T> ResourceAllocationConstraintChecker<T> instantiate() {
		return new ResourceAllocationConstraintChecker<T>(NAME, key);
	}

}
