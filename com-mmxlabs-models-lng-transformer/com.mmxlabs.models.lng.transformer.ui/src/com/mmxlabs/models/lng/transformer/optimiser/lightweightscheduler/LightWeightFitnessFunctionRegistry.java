package com.mmxlabs.models.lng.transformer.optimiser.lightweightscheduler;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

public class LightWeightFitnessFunctionRegistry {
	List<ILightWeightFitnessFunctionFactory> fitnessFunctionFactories = new LinkedList<>();
	
	public void registerFitnessFunctionFactory(@NonNull ILightWeightFitnessFunctionFactory factory) {
		fitnessFunctionFactories.add(factory);
	}
	
	public Collection<ILightWeightFitnessFunctionFactory> getFitnessFunctionFactories() {
		return fitnessFunctionFactories;
	}
}
