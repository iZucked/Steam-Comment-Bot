/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.parametermodes.impl;

import java.util.List;
import java.util.function.Predicate;

import org.eclipse.emf.common.util.EList;

import com.mmxlabs.models.lng.parameters.Constraint;
import com.mmxlabs.models.lng.parameters.Objective;
import com.mmxlabs.models.lng.parameters.ParametersFactory;
import com.mmxlabs.models.lng.transformer.ui.parametermodes.IParameterModeExtender;

public abstract class AObjectiveParameterModeExtender implements IParameterModeExtender {

	protected boolean isObjectiveInSettings(EList<Objective> objectives, String objectiveName) {
		for (Objective objective : objectives) {
			if (objective.getName().equals(objectiveName)) {
				return true;
			}
		}
		return false;
	}
	
	protected void removeExistingObjective (EList<Objective> objectives, String name) {
		if (isObjectiveInSettings(objectives, name)) {
			objectives.removeIf(new Predicate<Objective>() {
				@Override
				public boolean test(Objective t) {
					return t.getName().equals(name);
				}
			});
		}
	}
	
	protected static Objective createObjective(final String name, final double weight) {
		final Objective o = ParametersFactory.eINSTANCE.createObjective();
		o.setName(name);
		o.setWeight(weight);
		return o;
	}

}
