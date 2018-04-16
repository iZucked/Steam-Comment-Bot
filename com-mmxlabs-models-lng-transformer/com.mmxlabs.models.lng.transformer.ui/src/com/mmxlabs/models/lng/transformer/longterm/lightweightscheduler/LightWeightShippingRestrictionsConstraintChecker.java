package com.mmxlabs.models.lng.transformer.longterm.lightweightscheduler;

import java.util.List;

public class LightWeightShippingRestrictionsConstraintChecker implements ILightWeightConstraintChecker {

	List<List<Integer>> cargoVesselRestrictions;
	
	public LightWeightShippingRestrictionsConstraintChecker(List<List<Integer>> cargoVesselRestrictions) {
		this.cargoVesselRestrictions = cargoVesselRestrictions;
	}
	
	private boolean checkRestrictions(List<Integer> sequence, int vessel, List<List<Integer>> cargoVesselRestrictions) {
		for (int c : sequence) {
			if (cargoVesselRestrictions.get(c).contains(vessel)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean checkSequence(List<Integer> sequence, int vessel) {
		return checkRestrictions(sequence, vessel, cargoVesselRestrictions);
	}
}
