/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.util;

import com.mmxlabs.models.lng.adp.ADPFactory;
import com.mmxlabs.models.lng.adp.ADPModel;
import com.mmxlabs.models.lng.adp.IntervalType;
import com.mmxlabs.models.lng.adp.TargetCargoesOnVesselConstraint;
import com.mmxlabs.models.lng.fleet.Vessel;

public class TargetCargoesOnVesselConstraintMaker {
	private ADPModel model;
	private TargetCargoesOnVesselConstraint constraint;

	public TargetCargoesOnVesselConstraintMaker(ADPModel model, TargetCargoesOnVesselConstraint constraint) {
		this.model = model;
		this.constraint = constraint;
	}
	
	public static TargetCargoesOnVesselConstraintMaker makeTargetCargoesOnVesselConstraint(ADPModel model) {
		TargetCargoesOnVesselConstraintMaker fleetConstraintMaker = new TargetCargoesOnVesselConstraintMaker(model,
				ADPFactory.eINSTANCE.createTargetCargoesOnVesselConstraint());
		
		return fleetConstraintMaker;
	}
	
	public TargetCargoesOnVesselConstraintMaker withVessel(Vessel vessel) {
		constraint.setVessel(vessel);
		return this;
	}
	
	public TargetCargoesOnVesselConstraintMaker withTargetNumberOfCargoes(int target) {
		constraint.setTargetNumberOfCargoes(target);;
		return this;
	}

	public TargetCargoesOnVesselConstraintMaker withIntervalType(IntervalType intervalType) {
		constraint.setIntervalType(intervalType);
		return this;
	}

	public TargetCargoesOnVesselConstraintMaker withWeight(int weight) {
		constraint.setWeight(weight);
		return this;
	}
	
	public TargetCargoesOnVesselConstraint build() {
		model.getFleetProfile().getConstraints().add(constraint);
		return constraint;
	}
}
