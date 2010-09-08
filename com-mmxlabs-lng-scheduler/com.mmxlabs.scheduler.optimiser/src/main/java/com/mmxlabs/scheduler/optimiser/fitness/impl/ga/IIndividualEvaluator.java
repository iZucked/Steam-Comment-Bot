package com.mmxlabs.scheduler.optimiser.fitness.impl.ga;

public interface IIndividualEvaluator<T> {

	long evaluate(Individual individual);

	void dispose();

}
