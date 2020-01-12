/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.ext.impl;

import com.mmxlabs.models.lng.adp.ADPFactory;
import com.mmxlabs.models.lng.adp.TargetCargoesOnVesselConstraint;
import com.mmxlabs.models.lng.adp.ext.IFleetConstraintFactory;

public class TargetCargoOnVesselProfileConstraintFactory implements IFleetConstraintFactory {


	@Override
	public String getName() {
		return "Target number of cargoes";
	}

	@Override
	public TargetCargoesOnVesselConstraint createInstance() {
		return ADPFactory.eINSTANCE.createTargetCargoesOnVesselConstraint();
	}
}
