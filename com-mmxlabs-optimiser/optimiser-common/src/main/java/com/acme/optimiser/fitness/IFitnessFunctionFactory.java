package com.acme.optimiser.fitness;

public interface IFitnessFunctionFactory {

	String getName();

	<T> IFitnessFunction<T> instantiate();
}
