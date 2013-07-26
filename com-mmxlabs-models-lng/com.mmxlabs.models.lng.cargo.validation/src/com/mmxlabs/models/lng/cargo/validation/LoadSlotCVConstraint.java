/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.validation;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.validation.internal.Activator;
import com.mmxlabs.models.ui.validation.AbstractFeatureRangeConstraint;

public class LoadSlotCVConstraint extends AbstractFeatureRangeConstraint {

	@Override
	protected boolean shouldValidateFeature(EObject object,
			EStructuralFeature feature) {
		return object instanceof LoadSlot;
	}

	@Override
	protected String getPluginId() {
		return Activator.PLUGIN_ID;
	}

	@Override
	protected void createConstraints() {
		setRange(CargoPackage.Literals.LOAD_SLOT__CARGO_CV, 1.0, 40.0, "CV");	
	}
}
