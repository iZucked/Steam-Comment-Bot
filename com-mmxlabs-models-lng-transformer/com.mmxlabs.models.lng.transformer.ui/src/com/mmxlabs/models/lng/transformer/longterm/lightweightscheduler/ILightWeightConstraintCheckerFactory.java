package com.mmxlabs.models.lng.transformer.longterm.lightweightscheduler;

public interface ILightWeightConstraintCheckerFactory {
	String getName();
	ILightWeightConstraintChecker createConstraintChecker();
}
