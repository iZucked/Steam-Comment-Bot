/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.parametermodes.impl;

import org.eclipse.emf.common.util.EList;

import com.mmxlabs.models.lng.parameters.Constraint;
import com.mmxlabs.models.lng.parameters.ParametersFactory;
import com.mmxlabs.models.lng.transformer.ui.parametermodes.IParameterModeExtender;

public abstract class AContraintParameterModeExtender implements IParameterModeExtender {

	protected boolean isConstraintInSettings(EList<Constraint> constraints, String constraintName) {
		for (Constraint constraint : constraints) {
			if (constraint.getName().equals(constraintName)) {
				return true;
			}
		}
		return false;
	}
	
	protected static Constraint createConstraint(final String name, final boolean enabled) {
		final Constraint c = ParametersFactory.eINSTANCE.createConstraint();
		c.setName(name);
		c.setEnabled(enabled);
		return c;
	}

}
