/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.lightweightscheduler.optimiser.impl;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.transformer.lightweightscheduler.optimiser.IFullLightWeightConstraintCheckerFactory;
import com.mmxlabs.models.lng.transformer.lightweightscheduler.optimiser.ILightWeightConstraintCheckerFactory;

public class FullLightWeightConstraintCheckerRegistry {
	List<IFullLightWeightConstraintCheckerFactory> constraintCheckerFactories = new LinkedList<>();
	
	public void registerConstraintCheckerFactory(@NonNull IFullLightWeightConstraintCheckerFactory factory) {
		constraintCheckerFactories.add(factory);
	}
	
	public Collection<IFullLightWeightConstraintCheckerFactory> getConstraintCheckerFactories() {
		return constraintCheckerFactories;
	}
}
