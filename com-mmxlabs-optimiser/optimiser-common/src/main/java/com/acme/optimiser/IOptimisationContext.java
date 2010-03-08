package com.acme.optimiser;

import java.util.Collection;

import com.acme.optimiser.fitness.IFitnessFunction;

public interface IOptimisationContext {

	Collection<IFitnessFunction> getFitnessFunctions();

}
