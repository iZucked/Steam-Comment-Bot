package com.mmxlabs.models.lng.transformer.optimiser.lightweightscheduler;

public interface ILightWeightConstraintCheckerFactory {
	String getName();
	ILightWeightConstraintChecker createConstraintChecker();
}
