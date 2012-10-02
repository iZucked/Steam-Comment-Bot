package com.mmxlabs.optimiser.common.fitness;

import java.util.Collection;
import java.util.Collections;

import com.mmxlabs.optimiser.core.fitness.IFitnessCore;
import com.mmxlabs.optimiser.core.fitness.IFitnessCoreFactory;

/**
 * 
 * @author Simon Goodall
 * @since 2.0
 */
public class NonOptionalSlotFitnessCoreFactory implements IFitnessCoreFactory {

	public static final String NAME = "NonOptionalSlotFitness";

	@Override
	public String getFitnessCoreName() {
		return NAME;
	}

	@Override
	public Collection<String> getFitnessComponentNames() {
		return Collections.singleton(NAME);
	}

	@Override
	public IFitnessCore instantiate() {
		return new NonOptionalSlotFitnessCore(NAME);
	}
}
