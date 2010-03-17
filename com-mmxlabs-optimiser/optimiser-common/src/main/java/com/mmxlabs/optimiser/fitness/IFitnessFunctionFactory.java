package com.mmxlabs.optimiser.fitness;

public interface IFitnessFunctionFactory {

	String getName();

	<T> IFitnessComponent<T> instantiate();
}
