/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.lightweightscheduler.optimiser.impl;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.transformer.lightweightscheduler.optimiser.ILightWeightFitnessFunctionFactory;

public class LightWeightFitnessFunctionRegistry {
	List<ILightWeightFitnessFunctionFactory> fitnessFunctionFactories = new LinkedList<>();
	
	public void registerFitnessFunctionFactory(@NonNull ILightWeightFitnessFunctionFactory factory) {
		fitnessFunctionFactories.add(factory);
	}
	
	public Collection<ILightWeightFitnessFunctionFactory> getFitnessFunctionFactories() {
		return fitnessFunctionFactories;
	}
}
