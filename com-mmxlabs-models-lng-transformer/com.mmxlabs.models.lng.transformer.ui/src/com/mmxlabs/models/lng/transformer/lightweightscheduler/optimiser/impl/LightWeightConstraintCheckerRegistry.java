/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.lightweightscheduler.optimiser.impl;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.transformer.lightweightscheduler.optimiser.ILightWeightConstraintCheckerFactory;

public class LightWeightConstraintCheckerRegistry {
	List<ILightWeightConstraintCheckerFactory> constraintCheckerFactories = new LinkedList<>();
	
	public void registerConstraintCheckerFactory(@NonNull ILightWeightConstraintCheckerFactory factory) {
		constraintCheckerFactories.add(factory);
	}
	
	public Collection<ILightWeightConstraintCheckerFactory> getConstraintCheckerFactories() {
		return constraintCheckerFactories;
	}
}
