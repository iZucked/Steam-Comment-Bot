package com.mmxlabs.models.lng.transformer.longterm.lightweightscheduler;

import java.util.List;
import java.util.Set;

import com.google.inject.Inject;

public class LightWeightShippingRestrictionsConstraintChecker implements ILightWeightConstraintChecker {

	@Inject
	ILightWeightOptimisationData lightWeightOptimisationData;
	
	public LightWeightShippingRestrictionsConstraintChecker() {
	}
	
	private boolean checkRestrictions(List<Integer> sequence, int vessel, List<Set<Integer>> cargoVesselRestrictions) {
		for (int c : sequence) {
			if (cargoVesselRestrictions.get(c).contains(vessel)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean checkSequence(List<Integer> sequence, int vessel) {
		return checkRestrictions(sequence, vessel, lightWeightOptimisationData.getCargoVesselRestrictions());
	}
}
