package com.mmxlabs.models.lng.transformer.longterm.lightweightscheduler;

public class LightWeightShippingRestrictionsConstraintCheckerFactory implements ILightWeightConstraintCheckerFactory {
	final String NAME = "LightWeightShippingRestrictionsConstraintChecker";
	
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return NAME;
	}

	@Override
	public ILightWeightConstraintChecker createConstraintChecker() {
		return new LightWeightShippingRestrictionsConstraintChecker();
	}

}
